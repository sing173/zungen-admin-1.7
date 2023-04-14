package com.zungen.mqtt.protocol;

import com.zungen.mqtt.store.subscribe.SubscribeStoreService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UnSubscribe {

    private SubscribeStoreService subscribeStoreService;

    public UnSubscribe(SubscribeStoreService subscribeStoreService){
        this.subscribeStoreService = subscribeStoreService;
    }

    public void processUnSubscribe(Channel channel, MqttUnsubscribeMessage msg) {
        List<String> topicFilters = msg.payload().topics();
        String clinetId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
        topicFilters.forEach(topicFilter -> {
            subscribeStoreService.remove(topicFilter, clinetId);
            log.info("UNSUBSCRIBE - clientId: {}, topicFilter: {}", clinetId, topicFilter);
        });
        MqttUnsubAckMessage unsubAckMessage = (MqttUnsubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(msg.variableHeader().messageId()),
                null);
        channel.writeAndFlush(unsubAckMessage);
    }
}
