package cn.iocoder.yudao.module.bpm.controller.admin.crm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 进件工单 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmCrmInputRespVO extends BpmCrmInputBaseVO {

    @Schema(description = "客户进件工单主键", example = "29107")
    private Long id;

    @Schema(description = "工单编号")
    private String orderNo;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "流程实例的编号", example = "12092")
    private String processInstanceId;

    @Schema(description = "工单状态", example = "1")
    private Byte status;

    @Schema(description = "审批人")
    private Long auditorId;

    @Schema(description = "审批时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime auditTime;

    @Schema(description = "审批意见", example = "随便")
    private String auditRemark;

}
