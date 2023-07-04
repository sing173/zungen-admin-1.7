package com.zungen.mqtt.protocol;

import com.zungen.mqtt.auth.service.AuthService;
import com.zungen.mqtt.store.message.*;
import com.zungen.mqtt.store.session.SessionStoreService;
import com.zungen.mqtt.store.subscribe.SubscribeStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 协议处理
 */
@Component
public class ProtocolProcess {
    @Autowired
    private SessionStoreService sessionStoreService;
    @Autowired
    private SubscribeStoreService subscribeStoreService;
    @Autowired
    private AuthService authService;
    @Autowired
    private MessageIdService messageIdService;
    @Autowired
    private RetainMessageStoreService retainMessageStoreService;
    @Autowired
    private DupPublishMessageStoreService dupPublishMessageStoreService;
    @Autowired
    private DupPubRelMessageStoreService dupPubRelMessageStoreService;

    private Connect connect;

    private Subscribe subscribe;

    private UnSubscribe unSubscribe;

    private Publish publish;

    private DisConnect disConnect;

    private PingReq pingReq;

    private PubRel pubRel;

    private PubBack pubBack;

    private PubRec pubRec;

    private PubComp pubComp;

    public Connect connect(){
        if (connect == null){
            connect = new Connect(authService, sessionStoreService, dupPublishMessageStoreService, dupPubRelMessageStoreService, subscribeStoreService);
        }
        return connect;
    }
    public Subscribe subscribe(){
        if (subscribe == null){
            subscribe = new Subscribe(subscribeStoreService, messageIdService, retainMessageStoreService);
        }
        return subscribe;
    }
    public UnSubscribe unSubscribe() {
        if (unSubscribe == null) {
            unSubscribe = new UnSubscribe(subscribeStoreService);
        }
        return unSubscribe;
    }

    public Publish publish() {
        if (publish == null) {
            publish = new Publish(sessionStoreService, subscribeStoreService, messageIdService, retainMessageStoreService, dupPublishMessageStoreService);
        }
        return publish;
    }

    public DisConnect disConnect() {
        if (disConnect == null) {
            disConnect = new DisConnect(sessionStoreService, subscribeStoreService, dupPublishMessageStoreService, dupPubRelMessageStoreService);
        }
        return disConnect;
    }

    public PingReq pingReq() {
        if (pingReq == null) {
            pingReq = new PingReq();
        }
        return pingReq;
    }

    public PubRel pubRel() {
        if (pubRel == null) {
            pubRel = new PubRel();
        }
        return pubRel;
    }

    public PubBack pubBack() {
        if (pubBack == null) {
            pubBack = new PubBack(dupPublishMessageStoreService);
        }
        return pubBack;
    }

    public PubRec pubRec() {
        if (pubRec == null) {
            pubRec = new PubRec(dupPublishMessageStoreService, dupPubRelMessageStoreService);
        }
        return pubRec;
    }

    public PubComp pubComp() {
        if (pubComp == null) {
            pubComp = new PubComp(dupPubRelMessageStoreService);
        }
        return pubComp;
    }

    public SessionStoreService getSessionStoreService() {
        return sessionStoreService;
    }
}
