package cn.iocoder.yudao.module.bpm.controller.admin.crm.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 进件工单创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmCrmInputCreateReqVO extends BpmCrmInputBaseVO {

}
