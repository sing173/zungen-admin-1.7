package com.zungen.mqtt.mq;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.mq.core.stream.AbstractStreamMessageListener;
import cn.iocoder.yudao.module.bpm.mq.message.BpmTaskMqttMessage;
import cn.iocoder.yudao.module.system.api.notify.NotifyTemplateApi;
import com.zungen.mqtt.protocol.ProtocolProcess;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * 针对 {@link BpmTaskMqttMessage} 的消费者
 * 发送工单系统任务的消息(如抢单任务创建，提示员工抢单）
 *
 */
@Component
@Slf4j
public class BpmMqttSendConsumer extends AbstractStreamMessageListener<BpmTaskMqttMessage> {

    @Resource
    NotifyTemplateApi notifyTemplateApi;

    @Resource
    private ProtocolProcess protocolProcess;

    @Override
    public void onMessage(BpmTaskMqttMessage message) {
        log.info("[onMessage][消息内容({})]", message);

        //格式化消息内容
        String content = notifyTemplateApi.formatNotifyTemplateContent(message.getTemplateCode(), MapUtils.convertMap(message.getTemplateParams()));
        String topic = message.getTopic();

        JSONObject jsonObject =JSONUtil.createObj()
                .append("message", content)
                .append("orderId", message.getId())
                .append("orderType", message.getTemplateParams().get(2).getValue());

        message.getUserIds().forEach(userId -> {

            protocolProcess.publish().sendPublishMessage(topic + userId,
                    MqttQoS.AT_LEAST_ONCE, jsonObject.toString().getBytes(StandardCharsets.UTF_8),
                    false, false);
        });

    }
}
