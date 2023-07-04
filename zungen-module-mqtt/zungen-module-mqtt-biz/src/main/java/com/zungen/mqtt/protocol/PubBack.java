package com.zungen.mqtt.protocol;

import com.zungen.mqtt.store.message.DupPublishMessageStoreService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;


/**
 * PUBACK连接处理
 */
@Slf4j
public class PubBack {

    private DupPublishMessageStoreService dupPublishMessageStoreService;

    public PubBack(DupPublishMessageStoreService dupPublishMessageStoreService){
        this.dupPublishMessageStoreService = dupPublishMessageStoreService;
    }

    public void processPubAck(Channel channel, MqttMessageIdVariableHeader variableHeader){
        int messageId = variableHeader.messageId();
        log.info("PUBACK - clientId: {}, messageId: {}", channel.attr(AttributeKey.valueOf("clientId")).get(), messageId);
        dupPublishMessageStoreService.remove((String) channel.attr(AttributeKey.valueOf("clientId")).get(), messageId);

    }
}
