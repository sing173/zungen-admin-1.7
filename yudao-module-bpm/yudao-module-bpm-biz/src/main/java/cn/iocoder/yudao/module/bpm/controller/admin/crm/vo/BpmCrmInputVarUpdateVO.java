package cn.iocoder.yudao.module.bpm.controller.admin.crm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Schema(description = "管理后台 - 进件工单更新业务表单变量 Request VO")
@Data
@ToString(callSuper = true)
public class BpmCrmInputVarUpdateVO {
    @Schema(description = "数据表名")
    @NotNull(message = "数据表名不能为空")
    private String dataTable;

    @Schema(description = "数据主键")
    @NotNull(message = "数据主键不能为空")
    private Long dataId;

    @Schema(description = "流程活动id")
    @NotNull(message = "流程活动id不能为空")
    private String activityId;

    @Schema(description = "任务id")
    @NotNull(message = "任务id不能为空")
    private String taskId;

    @Schema(description = "流程实例id")
    @NotNull(message = "流程实例id不能为空")
    private String processInstanceId;

    @Schema(description = "变量集合")
    @NotNull(message = "变量集合不能为空")
    private Map<String, Object> data;
}
