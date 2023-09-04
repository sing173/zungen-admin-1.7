package cn.iocoder.yudao.module.trade.framework.aftersalelog.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

// TODO @puhui999：这个是不是应该搞成 vo 啊？
/**
 * 贸易售后日志详情 DTO
 *
 * @author HUIHUI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeAfterSaleLogRespDTO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "20669")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "22634")
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    @Schema(description = "用户类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "用户类型不能为空")
    private Integer userType;

    @Schema(description = "售后编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3023")
    @NotNull(message = "售后编号不能为空")
    private Long afterSaleId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25870")
    @NotNull(message = "订单编号不能为空")
    private Long orderId;

    @Schema(description = "订单项编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23154")
    @NotNull(message = "订单项编号不能为空")
    private Long orderItemId;

    @Schema(description = "售后状态（之前）", example = "2")
    private Integer beforeStatus;

    @Schema(description = "售后状态（之后）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "售后状态（之后）不能为空")
    private Integer afterStatus;

    @Schema(description = "操作明细", requiredMode = Schema.RequiredMode.REQUIRED, example = "维权完成，退款金额：¥37776.00")
    @NotNull(message = "操作明细不能为空")
    private String content;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
