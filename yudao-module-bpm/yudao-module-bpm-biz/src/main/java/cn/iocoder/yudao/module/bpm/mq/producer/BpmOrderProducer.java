package cn.iocoder.yudao.module.bpm.mq.producer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import cn.iocoder.yudao.module.bpm.mq.message.BpmTakeTaskMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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


}
