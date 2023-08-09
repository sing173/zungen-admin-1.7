package cn.iocoder.yudao.module.promotion.controller.admin.combination.vo.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 拼团商品创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CombinationProductCreateReqVO extends CombinationProductBaseVO {

}
