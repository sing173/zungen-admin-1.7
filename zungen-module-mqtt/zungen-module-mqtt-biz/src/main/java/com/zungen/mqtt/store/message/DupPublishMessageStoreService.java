package com.zungen.mqtt.store.message;

import java.util.List;

/**
 * @author admin
 * PUBLISH重发消息存储服务接口, 当QoS=1和QoS=2时存在该重发机制
 *
 */
public interface DupPublishMessageStoreService {
    void put(String clientId, DupPublishMessageStore dupPublishMessageStore);

    List<DupPublishMessageStore> get(String clientId);

    void remove(String clientId, int messageId);

    void removeByClient(String clientId);
}
