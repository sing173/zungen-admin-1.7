package cn.iocoder.yudao.module.bpm.controller.admin.crm.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 进件工单 Excel 导出 Request VO，参数和 BpmCrmInputPageReqVO 是一致的")
@Data
public class BpmCrmInputExportReqVO {

    @Schema(description = "工单编号")
    private String orderNo;

    @Schema(description = "客户标识", example = "5718")
    private Long crmCustomerId;

    @Schema(description = "客户图像标识", example = "7300")
    private Long crmCustomerImageId;

    @Schema(description = "产品标识", example = "11669")
    private Long crmProductId;

    @Schema(description = "流程实例的编号", example = "12092")
    private String processInstanceId;

    @Schema(description = "工单状态", example = "1")
    private Byte status;

    @Schema(description = "审批时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] auditTime;

    @Schema(description = "审批类型(自动、人工)", example = "2")
    private String auditType;

    @Schema(description = "审批意见", example = "随便")
    private String auditRemark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
