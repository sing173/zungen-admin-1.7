package cn.iocoder.yudao.module.bpm.service.crm.listener;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.module.bpm.dal.dataobject.crm.BpmCrmInputDO;
import cn.iocoder.yudao.module.bpm.service.crm.BpmCrmInputService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

import java.util.Map;

public class BpmCrmInputSaveDecisionResultListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
        BpmCrmInputService bpmCrmInputService = SpringUtil.getBean(BpmCrmInputService.class);

        Map<String, Object> vars = runtimeService.getVariables(execution.getId());
        long crmInputId = (long) vars.get("crmInputId");
        BpmCrmInputDO crmInputDO = bpmCrmInputService.getCrmInput(crmInputId);

        //此执行监听器与业务强关联，而且是演示demo，所以采用硬编码把信贷决策的结果取部分写入进件工单表中
        String reject_code = "";
        float apply_score = 0f;

        if(!StrUtil.isEmptyIfStr(vars.get("reject_ap"))) {
            reject_code = vars.get("reject_ap").toString();
        } else if(!StrUtil.isEmptyIfStr(vars.get("reject_gg"))) {
            reject_code = vars.get("reject_gg").toString();
        } else if(!StrUtil.isEmptyIfStr(vars.get("reject_ma"))) {
            reject_code = vars.get("reject_ma").toString();
        }else if(!StrUtil.isEmptyIfStr(vars.get("reject_br"))) {
            reject_code = vars.get("reject_br").toString();
        }else if(!StrUtil.isEmptyIfStr(vars.get("reject_qz"))) {
            reject_code = vars.get("reject_qz").toString();
        }else if(!StrUtil.isEmptyIfStr(vars.get("reject_sf"))) {
            reject_code = vars.get("reject_sf").toString();
        }else if(!StrUtil.isEmptyIfStr(vars.get("reject_rg"))) {
            reject_code = vars.get("reject_rg").toString();
        }

        if(vars.get("apply_score") != null) {
            apply_score = (float) vars.get("apply_score");
        }

        crmInputDO.setRejectCode(reject_code);
        crmInputDO.setApplyScore(apply_score);
        bpmCrmInputService.updateCrmInput(crmInputDO);
    }
}
