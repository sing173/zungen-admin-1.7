package com.zungen.mqtt.store.message;

import org.springframework.stereotype.Service;

@Service
public class MessageIdServiceImpl implements MessageIdService {
    @Override
    public int getNextMessageId() {
        return 0;
    }
}
