package cn.iocoder.yudao.module.bpm.framework.flowable.core.behavior;

import com.zungen.wb.api.design.BusinessEventFeign;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.MapExceptionEntry;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.helper.ClassDelegate;
import org.flowable.engine.impl.bpmn.parser.FieldDeclaration;

import java.util.List;

@Slf4j
public class BpmDecisionServiceTaskActivityBehavior extends ClassDelegate {

    /**
     * 决策平台业务领域id
     */
    private Expression businessAreaId;

    /**
     * 决策平台决策事件id
     */
    private Expression businessEventId;

    private Expression dataTableName;

    private Expression dataId;

    @Setter
    private BusinessEventFeign businessEventApi;


    public BpmDecisionServiceTaskActivityBehavior(String id, String implementation, List<FieldDeclaration> fieldDeclarations, boolean triggerable, Expression skipExpressionFromServiceTask, List<MapExceptionEntry> mapExceptions) {
        super(id, implementation, fieldDeclarations, triggerable, skipExpressionFromServiceTask, mapExceptions);
    }


    @Override
    public void execute(DelegateExecution delegateExecution) {
        //获取数据源
        log.info("BpmDecisionServiceTaskActivityBehavior execute in:" + businessAreaId.getExpressionText());

        //查询决策事件信息


        //发起决策请求


    }

}
