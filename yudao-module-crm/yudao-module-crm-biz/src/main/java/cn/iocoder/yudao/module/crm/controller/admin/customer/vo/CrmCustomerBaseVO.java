package cn.iocoder.yudao.module.crm.controller.admin.customer.vo;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 客户信息 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class CrmCustomerBaseVO {

    @Schema(description = "姓名", required = true, example = "王五")
    @NotNull(message = "姓名不能为空")
    private String name;

    @Schema(description = "手机号码", required = true)
    @NotNull(message = "手机号码不能为空")
    @Mobile
    private String mobile;

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

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "证件类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1 身份证")
    @NotNull(message = "证件类型不能为空")
    private Byte cerType;

    @Schema(description = "教育程度", example = "1 小学")
    private Byte eduType;

    @Schema(description = "职业类型", example = "7 军人")
    private Byte professionType;

    @Schema(description = "性别")
    private Byte sex;

    @Schema(description = "民族")
    private Byte ethnic;

}
