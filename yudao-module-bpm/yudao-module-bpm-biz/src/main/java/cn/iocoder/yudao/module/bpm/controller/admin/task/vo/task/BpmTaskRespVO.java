package cn.iocoder.yudao.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Schema(description = "管理后台 - 流程任务的 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskRespVO extends BpmTaskDonePageItemRespVO {

    @Schema(description = "任务定义的标识", required = true, example = "user-001")
    private String definitionKey;

    /**
     * 审核的用户信息
     */
    private User assigneeUser;

    @Schema(description = "用户信息")
    @Data
    public static class User {

        @Schema(description = "用户编号", required = true, example = "1")
        private Long id;
        @Schema(description = "用户昵称", required = true, example = "芋艿")
        private String nickname;

        @Schema(description = "部门编号", required = true, example = "1")
        private Long deptId;
        @Schema(description = "部门名称", required = true, example = "研发部")
        private String deptName;

    }

    private List<Form> formList;

    @Schema(description = "表单信息")
    @Data
    public static class Form {
        @Schema(description = "表单配置ID")
        private Long id;

        @Schema(description = "表单名称")
        private String name;

        @Schema(description = "表单配置")
        private String conf;

        @Schema(description = "读取表单数据的id")
        private Long dataId;

        @Schema(description = "表单字段配置")
        private List<String> fields;


    }
}
