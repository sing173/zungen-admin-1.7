package com.zungen.mqtt.protocol;

import com.zungen.mqtt.store.message.DupPubRelMessageStoreService;
import com.zungen.mqtt.store.message.DupPublishMessageStoreService;
import com.zungen.mqtt.store.session.SessionStore;
import com.zungen.mqtt.store.session.SessionStoreService;
import com.zungen.mqtt.store.subscribe.SubscribeStoreService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DisConnect {

    private SessionStoreService sessionStoreService;

    private SubscribeStoreService subscribeStoreService;

    private DupPublishMessageStoreService dupPublishMessageStoreService;

    private DupPubRelMessageStoreService dupPubRelMessageStoreService;

    public DisConnect(SessionStoreService sessionStoreService,
                      SubscribeStoreService grozaSubscribeStoreService,
                      DupPublishMessageStoreService dupPublishMessageStoreService,
                      DupPubRelMessageStoreService dupPubRelMessageStoreService){
        this.sessionStoreService = sessionStoreService;
        this.subscribeStoreService = grozaSubscribeStoreService;
        this.dupPublishMessageStoreService = dupPublishMessageStoreService;
        this.dupPubRelMessageStoreService = dupPubRelMessageStoreService;
    }

    public void processDisConnect(Channel channel,MqttMessage msg){
        String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
        SessionStore sessionStore = sessionStoreService.get(clientId);
        if (sessionStore!=null && sessionStore.isCleanSession()){
            subscribeStoreService.removeForClient(clientId);
            dupPublishMessageStoreService.removeByClient(clientId);
            dupPubRelMessageStoreService.removeByClient(clientId);
        }
        log.info("DISCONNECT - clientId: {}, cleanSession: {}", clientId, sessionStore.isCleanSession());
        sessionStoreService.remove(clientId);
        channel.close();
    }

}
