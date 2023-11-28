package cn.iocoder.yudao.module.crm.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 客户信息更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmCustomerUpdateReqVO extends CrmCustomerBaseVO {

    @Schema(description = "主键", required = true, example = "8279")
    @NotNull(message = "主键不能为空")
    private Long id;

    @Schema(description = "家庭地址")
    private String address;

    @Schema(description = "工作单位")
    private String myWork;

    @Schema(description = "工作电话")
    private String workPhone;

    @Schema(description = "家庭地址")
    private String workAddress;

    @Schema(description = "邮箱地址")
    private String email;

//    @Schema(description = "状态", required = true, example = "1")
//    @NotNull(message = "状态不能为空")
//    private Byte status;

    @Schema(description = "年龄")
    private Integer age;

//    @Schema(description = "证件类型", example = "2")
//    private Byte cerType;

    @Schema(description = "性别")
    private Byte sex;

//    @Schema(description = "民族")
//    private Byte ethnic;
//
//    @Schema(description = "出生日期")
//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
//    private LocalDateTime birthDay;
//
//    @Schema(description = "证件签发机构")
//    private String department;
//
//    @Schema(description = "证件有效期")
//    private String expirationDate;
//
//    @Schema(description = "证件像地址")
//    private String avatar;

}
