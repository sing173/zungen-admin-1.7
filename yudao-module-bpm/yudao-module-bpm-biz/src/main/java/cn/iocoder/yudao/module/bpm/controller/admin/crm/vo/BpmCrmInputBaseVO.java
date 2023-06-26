package cn.iocoder.yudao.module.bpm.controller.admin.crm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 进件工单 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BpmCrmInputBaseVO {

    @Schema(description = "客户标识", required = true, example = "5718")
    @NotNull(message = "客户标识不能为空")
    private Long crmCustomerId;

    @Schema(description = "客户图像标识", example = "7300")
    private Long crmCustomerImageId;

    @Schema(description = "产品标识", example = "11669")
    private Long crmProductId;

    @Schema(description = "审批类型(自动、人工)", required = true, example = "2")
    @NotNull(message = "审批类型不能为空")
    private Byte auditType;
}
