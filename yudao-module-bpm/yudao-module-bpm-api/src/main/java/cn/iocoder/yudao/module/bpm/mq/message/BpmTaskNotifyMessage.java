package cn.iocoder.yudao.module.bpm.mq.message;

import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.mq.core.stream.AbstractStreamMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 工单任务系统消息（如进件工单-抢单模式-进入工单池提醒）
 * 发送内部信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BpmTaskNotifyMessage extends AbstractStreamMessage {

    /**
     * 工单id
     */
    @NotNull(message = "工单id不能为空")
    private Long id;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 流程任务id
     */
    private String activityId;

    /**
     * 内部信模板编号
     */
    @NotNull(message = "站内信模板编号不能为空")
    private String templateCode;

    /**
     * 模板参数
     */
    private List<KeyValue<String, Object>> templateParams;

    /**
     * 发送的人员id
     */
    private List<Long> userIds;

    @Override
    public String getStreamKey() {
        return "bpm.order.notify";
    }
}
