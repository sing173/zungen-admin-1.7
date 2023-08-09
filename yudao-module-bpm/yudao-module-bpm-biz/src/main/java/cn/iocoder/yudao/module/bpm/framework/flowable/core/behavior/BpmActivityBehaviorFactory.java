package cn.iocoder.yudao.module.bpm.framework.flowable.core.behavior;

import cn.iocoder.yudao.module.bpm.service.definition.BpmTaskAssignRuleService;
import com.zungen.wb.api.design.BusinessEventFeign;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.flowable.bpmn.model.Activity;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.flowable.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.flowable.engine.impl.bpmn.helper.ClassDelegate;
import org.flowable.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

/**
 * 自定义的 ActivityBehaviorFactory 实现类，目的如下：
 * 1. 自定义 {@link #createUserTaskActivityBehavior(UserTask)}：实现自定义的流程任务的 assignee 负责人的分配
 *
 * @author 芋道源码
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmActivityBehaviorFactory extends DefaultActivityBehaviorFactory {

    @Setter
    private BpmTaskAssignRuleService bpmTaskRuleService;

    @Setter
    private BusinessEventFeign businessEventApi;

    @Override
    public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
        return new BpmUserTaskActivityBehavior(userTask)
                .setBpmTaskRuleService(bpmTaskRuleService);
    }

    @Override
    public ClassDelegate createClassDelegateServiceTask(ServiceTask serviceTask) {

        return new BpmDecisionServiceTaskActivityBehavior(serviceTask.getId(), serviceTask.getImplementation(),
                this.createFieldDeclarations(serviceTask.getFieldExtensions()), serviceTask.isTriggerable(),
                this.getSkipExpressionFromServiceTask(serviceTask),
                serviceTask.getMapExceptions()).setBusinessEventApi(businessEventApi);
    }

    @Override
    public ParallelMultiInstanceBehavior createParallelMultiInstanceBehavior(Activity activity,
                                                                             AbstractBpmnActivityBehavior innerActivityBehavior) {
        return new BpmParallelMultiInstanceBehavior(activity, innerActivityBehavior)
                .setBpmTaskRuleService(bpmTaskRuleService);
    }

    // TODO @ke：SequentialMultiInstanceBehavior 这个抽空也可以看看

}
