package cn.iocoder.yudao.module.bpm.framework.flowable.core.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class BpmDecisionServiceTaskDelegateListener implements JavaDelegate {

    /**
     * 决策平台业务领域id
     */
    private String businessAreaId;

    /**
     * 决策平台决策事件id
     */
    private String businessEventId;



    @Override
    public void execute(DelegateExecution delegateExecution) {

    }
}
