package com.zungen.mqtt.store.message;
/**
 * @author james
 * 分布式生成报文标识符
 */
public interface MessageIdService {
    /**
     * 获取报文标识符
     */
    int getNextMessageId();
}
