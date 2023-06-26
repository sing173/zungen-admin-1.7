package cn.iocoder.yudao.module.bpm.framework.flowable.core.behavior.script.impl;

import cn.hutool.core.util.NumberUtil;
import cn.iocoder.yudao.framework.common.util.collection.SetUtils;
import cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import cn.iocoder.yudao.module.bpm.framework.flowable.core.behavior.script.BpmTaskAssignScript;
import cn.iocoder.yudao.module.bpm.service.task.BpmProcessInstanceService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 分配给指定人审批的 Script 实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmTaskAssignUserScript implements BpmTaskAssignScript {

    public static final String ASSIGN_USER_KEY = "ASSIGN_USER_KEY";

    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessInstanceService bpmProcessInstanceService;

    @Override
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        HistoricProcessInstance processInstance = bpmProcessInstanceService.getHistoricProcessInstance(execution.getProcessInstanceId());
        //在流程实例processInstance中找到动态指派的人，在流程变量map中根据活动id找出对应的审批人
        Map<String, Object> varMap = processInstance.getProcessVariables();

        String assignUserId = (String) varMap.get(ASSIGN_USER_KEY);
        if(assignUserId == null) {
            throw exception(ErrorCodeConstants.TASK_ASSIGN_USER_NOT_EXISTS);
        };

        return SetUtils.asSet(NumberUtil.parseLong(assignUserId));
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.ASSIGN_USER;
    }

}
