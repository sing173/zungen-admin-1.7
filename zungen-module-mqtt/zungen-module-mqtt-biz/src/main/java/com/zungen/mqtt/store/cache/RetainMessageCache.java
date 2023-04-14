package com.zungen.mqtt.store.cache;

import com.zungen.mqtt.store.message.RetainMessageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RetainMessageCache {
    public static final String CACHE_PRE = "groza:retain:";

    @Autowired
    private RedisTemplate<String, Object> redisCacheTemplate;

    public RetainMessageStore put(String topic, RetainMessageStore retainMessageStore){
        redisCacheTemplate.opsForValue().set(CACHE_PRE + topic,retainMessageStore);
        return retainMessageStore;
    }

    public RetainMessageStore get(String topic){
        return (RetainMessageStore) redisCacheTemplate.opsForValue().get(CACHE_PRE + topic);
    }

    public boolean containsKey(String topic){
        return redisCacheTemplate.hasKey(CACHE_PRE + topic);
    }

    public void remove(String topic){
        redisCacheTemplate.delete(CACHE_PRE + topic);
    }

    public Map<String,RetainMessageStore> all(){
        Map<String, RetainMessageStore> map = new HashMap<>();
        Set<String> set = redisCacheTemplate.keys(CACHE_PRE + "*");
        if(set!=null&&!set.isEmpty()) {
            set.forEach(
                    entry -> {
                        map.put(entry.substring(CACHE_PRE.length()), (RetainMessageStore) redisCacheTemplate.opsForValue().get(entry));
                    }
            );
        }
        return map;
    }
}
