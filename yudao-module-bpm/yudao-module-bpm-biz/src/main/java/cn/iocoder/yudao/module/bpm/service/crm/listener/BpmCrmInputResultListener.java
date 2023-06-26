package cn.iocoder.yudao.module.bpm.service.crm.listener;

import cn.iocoder.yudao.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import cn.iocoder.yudao.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import cn.iocoder.yudao.module.bpm.service.crm.BpmCrmInputService;
import cn.iocoder.yudao.module.bpm.service.crm.BpmCrmInputServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OA 进件工单的结果的监听器实现类
 *
 */
@Component
public class BpmCrmInputResultListener extends BpmProcessInstanceResultEventListener {

    @Resource
    private BpmCrmInputService crmInputService;

    @Override
    protected String getProcessDefinitionKey() {
        return BpmCrmInputServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceResultEvent event) {
        crmInputService.updateCrmInputStatus(Long.parseLong(event.getBusinessKey()), event.getResult());
    }

}
