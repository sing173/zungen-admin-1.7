package cn.iocoder.yudao.module.bpm.framework.flowable.core.listener;

import cn.iocoder.yudao.module.bpm.api.feign.ZDEFeignClient;
import cn.iocoder.yudao.module.bpm.service.proxy.BpmVarServiceProxy;
import com.alibaba.fastjson.JSON;
import com.zungen.common.message.EventOutData;
import com.zungen.common.message.JsonMessage;
import com.zungen.common.message.JsonMessageHeader;
import com.zungen.wb.api.design.BusinessEventFeign;
import com.zungen.wb.model.business.BusinessEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.FormService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.form.StartFormData;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 工作流Java任务-调用决策引擎前置服务
 * 查询当前任务关联的表单，发送到决策引擎获取决策结果后设置到工作流变量
 */
@Slf4j
@Component
public class BpmDecisionServiceTaskDelegate implements JavaDelegate {
    /**
     * 决策平台业务领域id
     */
    private Expression businessAreaId;

    /**
     * 决策平台决策事件id
     */
    private Expression businessEventId;

    /**
     * 决策数据来源的表名
     */
    private Expression dataTableName;

    /**
     * 数据源id
     */
    private Expression dataId;

    @DubboReference
    private BusinessEventFeign businessEventApi;

    @Resource
    private ZDEFeignClient zdeFeignClient;

    @Resource
    @Lazy // 解决循环依赖
    private FormService formService;

    @Override
    public void execute(DelegateExecution execution) {
        String dataTableNameTxt = dataTableName.getExpressionText();
        //通过代理获取工作流当前节点绑定表单的数据接口
        BpmVarServiceProxy bpmVarServiceProxy = BpmVarServiceProxy.getInstance(dataTableNameTxt);
        Long dataIdL = (Long) execution.getVariable(dataId.getExpressionText());
        //获取表单数据作为决策的数据输入源
        Map<String, Object> dataSourceVarMap = bpmVarServiceProxy.getBpmVars(dataTableNameTxt, dataIdL);
        //获取决策事件
        BusinessEventDTO businessEventDTO = businessEventApi.findById(businessEventId.getExpressionText());
        //构建决策引擎-前置的消息请求对象
        JsonMessageHeader header = new JsonMessageHeader();
        header.setEventCode(businessEventDTO.getCode());
        header.setDataClassFullName(bpmVarServiceProxy.classFullName);
        JsonMessage<Map<String, Object>> message = new JsonMessage<>(header, dataSourceVarMap);
        //通过feign客户端发送http请求到决策前置服务，同步获取决策结果
        JsonMessage<EventOutData> eventOutMessage = zdeFeignClient.decision(JSON.toJSONString(message));
        log.info("BpmDecisionServiceTaskDelegate out:" + eventOutMessage.toString());
        //把决策结果设置到流程变量中
        EventOutData eventOutData = eventOutMessage.getBody();
        StartFormData formData = formService.getStartFormData(execution.getProcessDefinitionId());
        formData.getFormProperties().forEach(formProperty -> {
            execution.setVariable(formProperty.getId(), eventOutData.getResultVars().get(formProperty.getId()));
        });

    }
}
