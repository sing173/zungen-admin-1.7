package cn.iocoder.yudao.module.crm.controller.admin.customer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - 客户信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmCustomerRespVO extends CrmCustomerBaseVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "状态", example = "1")
    private Byte status;

    @Schema(description = "身份编号")
    private String idCard;

    @Schema(description = "出生日期")
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate birthDay;

    @Schema(description = "证件签发机构")
    private String department;

    @Schema(description = "证件有效期")
    private String expirationDate;

    @Schema(description = "证件像地址")
    private String avatar;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
