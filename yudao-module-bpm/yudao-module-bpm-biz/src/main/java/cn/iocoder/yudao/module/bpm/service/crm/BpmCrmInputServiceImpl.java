package cn.iocoder.yudao.module.bpm.service.crm;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.BpmCrmInputCreateReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.BpmCrmInputExportReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.BpmCrmInputPageReqVO;
import cn.iocoder.yudao.module.bpm.controller.admin.crm.vo.BpmCrmInputVarUpdateVO;
import cn.iocoder.yudao.module.bpm.convert.crm.BpmCrmInputConvert;
import cn.iocoder.yudao.module.bpm.dal.dataobject.crm.BpmCrmInputDO;
import cn.iocoder.yudao.module.bpm.dal.mysql.crm.BpmCrmInputMapper;
import cn.iocoder.yudao.module.bpm.service.task.BpmProcessInstanceService;
import cn.iocoder.yudao.module.crm.api.CrmCustomerApi;
import cn.iocoder.yudao.module.crm.api.dto.CrmCustomerDTO;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.CRM_INPUT_NOT_EXISTS;
import static cn.iocoder.yudao.module.bpm.framework.flowable.core.behavior.script.impl.BpmTaskAssignUserScript.ASSIGN_USER_KEY;
import static cn.iocoder.yudao.module.crm.enums.ErrorCodeConstants.CUSTOMER_NOT_EXISTS;

/**
 * 进件工单 Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class BpmCrmInputServiceImpl implements BpmCrmInputService {

    /**
     * 客户进件工单对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "crm_customer_input";
    public static final String ORDER_NO_PRE = "C01";

    @Resource
    private BpmCrmInputMapper crmInputMapper;

    @Resource
    private BpmProcessInstanceService processInstanceService;

    @Resource
    private TaskService taskService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private CrmCustomerApi crmCustomerApi;

    @Override
    public Long createCrmInput(Long userId, BpmCrmInputCreateReqVO createReqVO) {
        //验证客户是否存在
        if(!crmCustomerApi.validateCustomerExists(createReqVO.getCrmCustomerId())) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }

        //创建进件工单
        BpmCrmInputDO crmInput = BpmCrmInputConvert.INSTANCE.convert(createReqVO);
        //生成工单编号、设置状态
        crmInput.setOrderNo(ORDER_NO_PRE + DateUtil.format(DateUtil.date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_NO_BLOCK));
        crmInput.setStatus(Byte.valueOf("1"));
        //插入工单
        crmInputMapper.insert(crmInput);

        //发起工作流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        //写入审批类型作为流程变量，流程根据此变量进入不同的分支
        processInstanceVariables.put("auditType", createReqVO.getAuditType());
        processInstanceVariables.put("customerId", createReqVO.getCrmCustomerId());
        processInstanceVariables.put("customerImageId", createReqVO.getCrmCustomerImageId());
        processInstanceVariables.put("crmInputId", crmInput.getId());

        String processInstanceId = processInstanceService.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(crmInput.getId())));

        // 将流程实例的编号，更新到进件工单中
        crmInputMapper.updateById(new BpmCrmInputDO().setId(crmInput.getId()).setProcessInstanceId(processInstanceId));
        // 返回
        return crmInput.getId();
    }

    @Override
    public void updateCrmInputStatus(Long id, Integer result) {
        // 校验存在
        validateCrmInputExists(id);
        // 更新
        crmInputMapper.updateById(new BpmCrmInputDO().setId(id)
                .setAuditTime(DateUtil.toLocalDateTime(new Date()))
                .setStatus(result.byteValue()));
    }

    private void validateCrmInputExists(Long id) {
        if (crmInputMapper.selectById(id) == null) {
            throw exception(CRM_INPUT_NOT_EXISTS);
        }
    }

    @Override
    public BpmCrmInputDO getCrmInput(Long id) {
        return crmInputMapper.selectById(id);
    }

    @Override
    public PageResult<BpmCrmInputDO> getCrmInputPage(BpmCrmInputPageReqVO pageReqVO) {
        return crmInputMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BpmCrmInputDO> getCrmInputList(BpmCrmInputExportReqVO exportReqVO) {
        return crmInputMapper.selectList(exportReqVO);
    }

    @Override
    public Map<String, Object> getBpmVars(String dataTable, Long dataId) {
        switch (dataTable) {
            case "crm_customer":
                CrmCustomerDTO customerDTO = crmCustomerApi.getCustomer(dataId);
                return BeanUtil.beanToMap(customerDTO);
            case "bpm_crm_input":
                BpmCrmInputDO crmInputDO = getCrmInput(dataId);
                if(crmInputDO == null) {
                    throw exception(CRM_INPUT_NOT_EXISTS);
                }

                return BeanUtil.beanToMap(crmInputDO);

            default:
                throw new IllegalStateException("Unexpected value: " + dataTable);
        }
    }

    @Override
    public Boolean updateBpmVars(BpmCrmInputVarUpdateVO varUpdateVO) {
        switch (varUpdateVO.getActivityId()) {
            case "Activity_paidan"://进件工单-人工派单任务
                Execution execution = runtimeService.createExecutionQuery()
                        .processInstanceId(varUpdateVO.getProcessInstanceId())
                        .activityId(varUpdateVO.getActivityId())
                        .singleResult();
                Assert.notNull(execution, "任务不存在({}) 不存在", varUpdateVO.getTaskId());
                //从前端的数据找到动态分派人id（分配规则选择了动态指派人，则前一个任务一定要配置一个ASSIGN_USER_KEY流程变量）
                String assignUserValue = StrUtil.toString(varUpdateVO.getData().get(ASSIGN_USER_KEY));
                //设置动态分派人流程变量
                runtimeService.setVariable(execution.getId(), ASSIGN_USER_KEY, assignUserValue);

                return true;
            case "Activity_shouli"://进件工单-受理审批任务
                BpmCrmInputDO crmInputDO = crmInputMapper.selectById(varUpdateVO.getDataId());
                crmInputDO.setAuditRemark(StrUtil.toString(varUpdateVO.getData().get("auditRemark")));
                crmInputMapper.updateById(crmInputDO);

                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + varUpdateVO.getDataTable());
        }

    }
}
