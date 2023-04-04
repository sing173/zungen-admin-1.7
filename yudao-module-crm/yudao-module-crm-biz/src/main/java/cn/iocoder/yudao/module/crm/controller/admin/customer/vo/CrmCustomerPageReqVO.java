package cn.iocoder.yudao.module.crm.controller.admin.customer.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 客户信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmCustomerPageReqVO extends PageParam {

    @Schema(description = "客户的用户编号", example = "2170")
    private Long userId;

    @Schema(description = "担保人的身份认证id", example = "13023")
    private Long identityId;

    @Schema(description = "姓名", example = "王五")
    private String name;

    @Schema(description = "手机号码")
    private String mobile;

    @Schema(description = "身份编号")
    private String idCard;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
