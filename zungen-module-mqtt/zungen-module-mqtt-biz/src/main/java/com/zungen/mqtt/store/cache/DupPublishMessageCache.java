package com.zungen.mqtt.store.cache;

import cn.hutool.core.util.StrUtil;
import com.zungen.mqtt.store.message.DupPublishMessageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class DupPublishMessageCache {
    private final static String CACHE_PRE = "groza:publish:";

    @Autowired
    private RedisTemplate<String, Object> redisCacheTemplate;

    public DupPublishMessageStore put(String clientId, Integer messageId, DupPublishMessageStore dupPublishMessageStore){
        redisCacheTemplate.opsForHash().put(CACHE_PRE + clientId, StrUtil.toString(messageId),dupPublishMessageStore);
        return dupPublishMessageStore;
    }

    public ConcurrentHashMap<Integer,DupPublishMessageStore> get(String clientId){
        ConcurrentHashMap<Integer, DupPublishMessageStore> map = new ConcurrentHashMap<>();
        Map<Object,Object> map1 = redisCacheTemplate.opsForHash().entries(CACHE_PRE + clientId);
        if (map1 != null && !map1.isEmpty()) {
            map1.forEach((k, v) -> {
                map.put((Integer)k, (DupPublishMessageStore)v);
            });
        }
        return map;
    }
    public boolean containsKey(String clientId){
        return redisCacheTemplate.hasKey(CACHE_PRE + clientId);
    }

    public void remove(String clientId,Integer messageId){
        redisCacheTemplate.opsForHash().delete(CACHE_PRE + clientId,StrUtil.toString(messageId));
    }
    public void remove(String clientId){
        redisCacheTemplate.delete(CACHE_PRE + clientId);
    }

}
