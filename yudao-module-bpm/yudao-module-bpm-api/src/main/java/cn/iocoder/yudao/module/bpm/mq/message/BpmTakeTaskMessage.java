package cn.iocoder.yudao.module.bpm.mq.message;

import cn.iocoder.yudao.framework.mq.core.stream.AbstractStreamMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 工单任务抢单消息
 * 客户端抢单成功后发送抢单消息到队列，由队列消费者异步完成抢单后续操作
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BpmTakeTaskMessage extends AbstractStreamMessage {

    /**
     * 工单id
     */
    @NotNull(message = "工单id不能为空")
    private Long orderId;

    @NotNull(message = "工单类型不能为空")
    private String orderType;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 流程任务id
     */
    private String processTaskId;

    /**
     * 指派审批的人员id
     */
    private Long userId;


    @Override
    public String getStreamKey() {
        return "bpm.order.streams";
    }
}
