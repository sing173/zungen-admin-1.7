package cn.iocoder.yudao.module.crm.controller.admin.customer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 客户信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmCustomerRespVO extends CrmCustomerBaseVO {

    @Schema(description = "主键", required = true, example = "8279")
    private Long id;

    @Schema(description = "家庭地址")
    private String address;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;

}
