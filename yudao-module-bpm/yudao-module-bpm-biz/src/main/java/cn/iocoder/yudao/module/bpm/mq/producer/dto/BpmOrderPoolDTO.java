package cn.iocoder.yudao.module.bpm.mq.producer.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Data
public class BpmOrderPoolDTO {

    private Long orderId;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 流程任务id
     */
    private String activityId;

    private String userIds;

    private String orderNo;

    private Byte status;

    private String type;

    private String creator;

    private String auditor;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTime;
}
