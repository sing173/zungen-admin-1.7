package com.zungen.mqtt.auth.service;

/**
 * 用户和密码认证服务接口
 */
public interface AuthService {
    /**
     * 验证用户名和密码是否正确
     */
    boolean checkValid(String username, String password);
}
