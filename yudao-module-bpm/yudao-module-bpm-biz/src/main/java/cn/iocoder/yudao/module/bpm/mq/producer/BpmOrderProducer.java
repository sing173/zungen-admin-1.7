package cn.iocoder.yudao.module.bpm.mq.producer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import cn.iocoder.yudao.module.bpm.mq.message.BpmTakeTaskMessage;
import cn.iocoder.yudao.module.bpm.mq.producer.dto.BpmOrderPoolDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static cn.iocoder.yudao.module.bpm.enums.BpmOrderRedisKeyConstants.*;

/**
 * 工单 相关消息的 Producer
 *
 */
@Slf4j
@Component
public class BpmOrderProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    public static DefaultRedisScript<Long> ORDER_KILL_SCRIPT = getOrderKillScript();

    private static DefaultRedisScript<Long> getOrderKillScript() {
        ORDER_KILL_SCRIPT = new DefaultRedisScript<>();
        ORDER_KILL_SCRIPT.setLocation(new ClassPathResource("orderKill.lua"));
        ORDER_KILL_SCRIPT.setResultType(Long.class);
        return ORDER_KILL_SCRIPT;
    }

    /**
     * 执行lua抢单脚本 0抢到了，-1 没权限，-2 重复下单，-3 已抢了两张单，需完成至少一张单才能继续抢
     * @param orderType
     * @param orderId
     * @param userId
     * @return
     */
    public Long killBpmOrder(String orderType, String orderId, String userId) {

        Long luaResult = redisMQTemplate.getRedisTemplate().execute(ORDER_KILL_SCRIPT,
                //redis key
                CollUtil.newArrayList(
                        BPM_ORDER_POOL_KILL_KEY,
                        BPM_ORDER_POOL_KILL_AUTH_KEY,
                        BPM_ORDER_POOL_KILL_USERS_KEY
                ),
                //脚本的参数
                orderId, orderType, userId);
        log.info("执行抢单lua脚本结果" + luaResult);

        //成功后创建更新工单异步队列任务
        if(luaResult == 1) {
            sendBpmTakeTaskMessage(NumberUtil.parseLong(orderId), orderType, NumberUtil.parseLong(userId));
        }
        return luaResult;
    }

    /**
     * 发送抢单消息 {@link BpmTakeTaskMessage} 消息
     */
    public void sendBpmTakeTaskMessage(Long orderId, String orderType, Long userId) {
        BpmTakeTaskMessage message = new BpmTakeTaskMessage();
        message.setUserId(userId);
        message.setOrderId(orderId);
        message.setOrderType(orderType);

        redisMQTemplate.send(message);
    }

    /**
     * 工单池列表查询 TODO 后续持久化到数据库，不再查询redis
     * @param userId
     * @return
     */
    public List<BpmOrderPoolDTO> getOrderPoolList(String userId) {
        StringRedisTemplate redisTemplate = (StringRedisTemplate) redisMQTemplate.getRedisTemplate();
        Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions()
                .match(StrUtil.format("{}*",BPM_ORDER_POOL_SEARCH_KEY))
                .count(100)
                .build());
        List<BpmOrderPoolDTO> orderPoolDTOS = new ArrayList<>();
        //RedisTemplate已经封装了scan命令，会按每次返回的游标值继续查找，直到游标返回0
        while (cursor.hasNext()) {
            String orderInfoKey = cursor.next();
            String orderInfoStr = redisTemplate.opsForValue().get(orderInfoKey);
            BpmOrderPoolDTO bpmOrderPoolDTO = JsonUtils.parseObject(orderInfoStr, BpmOrderPoolDTO.class);
            //返回未被抢以及有权限抢单的列表
            if(bpmOrderPoolDTO.getStatus() != 10 && bpmOrderPoolDTO.getUserIds().contains(userId)) {
                orderPoolDTOS.add(bpmOrderPoolDTO);
            }
        }
        cursor.close();

        orderPoolDTOS.sort((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()));
        return orderPoolDTOS;
    }
}
