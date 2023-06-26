package cn.iocoder.yudao.module.bpm.mq.consumer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import cn.iocoder.yudao.framework.mq.core.stream.AbstractStreamMessageListener;
import cn.iocoder.yudao.module.bpm.mq.message.BpmTakeTaskMessage;
import cn.iocoder.yudao.module.bpm.service.crm.dto.BpmOrderPoolDTO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

import static cn.iocoder.yudao.module.bpm.enums.BpmOrderRedisKeyConstants.*;
import static cn.iocoder.yudao.module.bpm.framework.flowable.core.behavior.script.impl.BpmTaskAssignUserScript.ASSIGN_USER_KEY;

/**
 * 针对 {@link BpmTakeTaskMessage} 的消费者
 * 消费抢单消息，处理抢单成功后续业务处理
 *
 */
@Component
@Slf4j
public class BpmTakeTaskConsumer extends AbstractStreamMessageListener<BpmTakeTaskMessage> {

    @Resource
    RuntimeService runtimeService;

    @Resource
    private RedisMQTemplate redisMQTemplate;

    private final ExecutorService executorService = ThreadUtil.newExecutor(10, 20, 10);

    public static DefaultRedisScript<String> ORDER_DONE_SCRIPT = getOrderDoneScript();

    private static DefaultRedisScript<String> getOrderDoneScript() {
        ORDER_DONE_SCRIPT = new DefaultRedisScript<>();
        ORDER_DONE_SCRIPT.setLocation(new ClassPathResource("orderDone.lua"));
        ORDER_DONE_SCRIPT.setResultType(String.class);
        return ORDER_DONE_SCRIPT;
    }

    @Override
    public void onMessage(BpmTakeTaskMessage message) {
        //指定任务审批人并触发继续任务
        log.info("[onMessage][消息内容({})]", message);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //执行lua脚本判断抢单是否成功，返回工单池对应的信息 @BpmOrderPoolDTO
                String orderJsonStr = redisMQTemplate.getRedisTemplate().execute(ORDER_DONE_SCRIPT,
                        CollUtil.newArrayList(BPM_ORDER_POOL_KILL_KEY,
                                BPM_ORDER_POOL_SEARCH_KEY,
                                BPM_ORDER_POOL_KILL_AUTH_KEY,
                                BPM_ORDER_POOL_KILL_USERS_KEY),
                        message.getOrderId().toString(),
                        message.getOrderType(),
                        message.getUserId().toString());

                if(StrUtil.isNotEmpty(orderJsonStr)) {
                    //找到工单对应的流程任务
                    BpmOrderPoolDTO bpmOrderPoolDTO = JsonUtils.parseObject(orderJsonStr, BpmOrderPoolDTO.class);
                    Assert.notNull(bpmOrderPoolDTO, "Redis工单池对应的工单记录为空{}.", message.getOrderId());
                    Execution execution = runtimeService.createExecutionQuery()
                            .processInstanceId(bpmOrderPoolDTO.getProcessInstanceId())
                            .activityId(bpmOrderPoolDTO.getActivityId())
                            .singleResult();
                    //设置动态指派人流程变量
                    runtimeService.setVariable(execution.getId(), ASSIGN_USER_KEY, bpmOrderPoolDTO.getAuditor());
                    //触发接收任务，继续流程
                    runtimeService.trigger(execution.getId());

                    //根据工单类型执行业务逻辑（可选，用interceptors）
                }
            }
        });
    }
}
