package com.zungen.mqtt.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PubRel {

    public void processPubRel(Channel channel, MqttMessageIdVariableHeader variableHeader) {
        MqttMessage pubCompMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBCOMP, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(variableHeader.messageId()),
                null);
        log.info("PUBREL - clientId: {}, messageId: {}", (String) channel.attr(AttributeKey.valueOf("clientId")).get(), variableHeader.messageId());
        channel.writeAndFlush(pubCompMessage);
    }
}
