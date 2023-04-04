package cn.iocoder.yudao.module.crm.controller.admin.customer.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Length;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 客户信息创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmCustomerCreateReqVO extends CrmCustomerBaseVO {

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

    @Schema(description = "状态", example = "1")
    private Byte status;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "证件类型", example = "1 身份证")
    private Byte cerType;

    @Schema(description = "教育程度", example = "1 小学")
    private Byte eduType;

    @Schema(description = "职业类型", example = "7 军人")
    private Byte professionType;

    @Schema(description = "性别")
    private Byte sex;

    @Schema(description = "民族")
    private Byte ethnic;

    @Schema(description = "出生日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime birthDay;

    @Schema(description = "证件签发机构")
    private String department;

    @Schema(description = "证件有效期")
    private String expirationDate;

    @Schema(description = "证件像地址")
    private String avatar;

    @Schema(description = "手机验证码", required = true, example = "1024")
    @NotEmpty(message = "手机验证码不能为空")
    @Length(min = 4, max = 6, message = "手机验证码长度为 4-6 位")
    @Pattern(regexp = "^[0-9]+$", message = "手机验证码必须都是数字")
    private String code;
}
