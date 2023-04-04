package cn.iocoder.yudao.module.crm.controller.admin.customer.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 客户信息 Excel 导出 Request VO，参数和 CrmCustomerPageReqVO 是一致的")
@Data
public class CrmCustomerExportReqVO {

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
