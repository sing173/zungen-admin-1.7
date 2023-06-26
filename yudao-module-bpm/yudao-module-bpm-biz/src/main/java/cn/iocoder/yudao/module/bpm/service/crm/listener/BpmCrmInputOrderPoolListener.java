package cn.iocoder.yudao.module.bpm.service.crm.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.core.KeyValue;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import cn.iocoder.yudao.module.bpm.convert.crm.BpmTaskMessageConvert;
import cn.iocoder.yudao.module.bpm.dal.dataobject.crm.BpmCrmInputDO;
import cn.iocoder.yudao.module.bpm.mq.message.BpmTaskMqttMessage;
import cn.iocoder.yudao.module.bpm.mq.message.BpmTaskNotifyMessage;
import cn.iocoder.yudao.module.bpm.service.crm.BpmCrmInputService;
import cn.iocoder.yudao.module.bpm.service.crm.dto.BpmOrderPoolDTO;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.module.bpm.enums.BpmOrderRedisKeyConstants.*;

/**
 * 进件工单 - 监听工单池抢单任务创建
 * 任务创建后立刻插入Redis消息队列，由客户端抢单
 */
public class BpmCrmInputOrderPoolListener implements ExecutionListener {

    /**
     * 可以抢单的人员列表(在进件工单-自动派单任务的执行任务监听中设置注入字段)
     */
    private Expression userIds;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        Long crmInputId = (Long) delegateExecution.getVariable("crmInputId");
        Assert.notNull(crmInputId, "进件工单ID不能为空!");
        Assert.notNull(userIds, "可抢单人员列表ID（userIds）不能为空");
        //根据工单执行任务输入参数获取群发人员列表 //TODO 前端改造为可多选人员
        List<String> userIdStrList = StrUtil.split(userIds.getExpressionText(), ",");
        List<Long> userIdList = userIdStrList.stream().map(NumberUtil::parseLong).collect(Collectors.toList());

        //ExecutionListener是由flowable通过反射new的，所以无法通过注入获取spring管理的bean，需要手动获取
        RedisMQTemplate redisMQTemplate = SpringUtil.getBean(RedisMQTemplate.class);
        StringRedisTemplate redisTemplate = (StringRedisTemplate) redisMQTemplate.getRedisTemplate();
        BpmCrmInputService bpmCrmInputService = SpringUtil.getBean(BpmCrmInputService.class);

        //1. 把当前工单信息存入Redis，作为工单池抢单用
        //1.1 获取当前进件工单信息,以map存入redis作为工单池列表查询使用，完成工单后删除该key TODO 后续可优化为持久化到数据库
        BpmCrmInputDO crmInputDO = bpmCrmInputService.getCrmInput(crmInputId);
        BpmOrderPoolDTO bpmOrderPoolDTO = new BpmOrderPoolDTO();
        bpmOrderPoolDTO.setOrderId(crmInputId);
        bpmOrderPoolDTO.setProcessInstanceId(delegateExecution.getProcessInstanceId());
        bpmOrderPoolDTO.setActivityId(delegateExecution.getCurrentActivityId());
        bpmOrderPoolDTO.setOrderNo(crmInputDO.getOrderNo());
        bpmOrderPoolDTO.setUserIds(userIds.getExpressionText());
        bpmOrderPoolDTO.setType(BPM_ORDER_CRM_INPUT_KEY);
        bpmOrderPoolDTO.setStatus(crmInputDO.getStatus());
        bpmOrderPoolDTO.setCreator(crmInputDO.getCreator());
        bpmOrderPoolDTO.setCreateTime(crmInputDO.getCreateTime());
        String orderKey = StrUtil.format("{}:{}", BPM_ORDER_CRM_INPUT_KEY, crmInputId);
        redisTemplate.opsForValue().set(StrUtil.format("{}:{}", BPM_ORDER_POOL_SEARCH_KEY, orderKey),
                JsonUtils.toJsonString(bpmOrderPoolDTO));
        //1.2 把当前工单id放入工单池集合，后续在lua脚本判断是否有工单作为抢单使用，完成工单后删除该key
        redisTemplate.opsForSet().add(BPM_ORDER_POOL_KILL_KEY, orderKey);
        //1.3 把工单对应的可见人员列表存入redis set，完成工单后删除该key
        redisTemplate.opsForSet().add(StrUtil.format("{}:{}", BPM_ORDER_POOL_KILL_AUTH_KEY, orderKey),
                userIdStrList.toArray(new String[0]));

        //2. 发送站内信记录提示消息
        BpmTaskNotifyMessage bpmTaskNotifyMessage = new BpmTaskNotifyMessage();
        bpmTaskNotifyMessage.setId(crmInputId);
        bpmTaskNotifyMessage.setProcessInstanceId(delegateExecution.getProcessInstanceId());
        bpmTaskNotifyMessage.setActivityId(delegateExecution.getCurrentActivityId());
        bpmTaskNotifyMessage.setTemplateCode("notify-template-order-pool");
        bpmTaskNotifyMessage.setUserIds(userIdList);
        bpmTaskNotifyMessage.setTemplateParams(CollUtil.newArrayList(
                new KeyValue<>("orderName", "进件工单"),
                new KeyValue<>("orderNo", crmInputDO.getOrderNo())));
        redisMQTemplate.send(bpmTaskNotifyMessage);
        //2.1 发送MQTT主题消息提示各终端抢单
        BpmTaskMqttMessage bpmTaskMqttMessage = BpmTaskMessageConvert.INSTANCE.convert(bpmTaskNotifyMessage);
        bpmTaskMqttMessage.setTopic("bpm/order/crm_customer_input/");
        redisMQTemplate.send(bpmTaskMqttMessage);
    }
}
