package cn.iocoder.yudao.module.crm.controller.admin.customer.vo;

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
    private String mobile;

    @Schema(description = "身份编号")
    @NotNull(message = "身份编号不能为空")
    private String idCard;

}
