package com.hp.article.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 实现token的认证操作
 * 自定义一个JwtToken，来完成shiro的supports方法。
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String jwt) {
        this.token = jwt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
