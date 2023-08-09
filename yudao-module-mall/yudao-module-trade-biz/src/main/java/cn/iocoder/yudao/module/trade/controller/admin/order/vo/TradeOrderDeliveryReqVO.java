package cn.iocoder.yudao.module.trade.controller.admin.order.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.trade.enums.delivery.DeliveryTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - 订单发货 Request VO")
@Data
public class TradeOrderDeliveryReqVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "订单编号不能为空")
    private Long id;

    @Schema(description = "发货类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @InEnum(DeliveryTypeEnum.class)
    @NotNull(message = "发货类型不能为空")
    private Integer type;

    // TODO @puhui999：还是要校验下

    @Schema(description = "发货物流公司编号", example = "1")
    private Long logisticsId;

    @Schema(description = "发货物流单号", example = "SF123456789")
    private String logisticsNo;

    // TODO 订单项商品单独发货；不做单独发

    @Schema(description = "发货订单项", example = "[1,2,3]")
    @NotNull(message = "发货订单项不能为空")
    private List<Long> orderItemIds;

    // =============== 同城配送  ================
    // TODO

}
