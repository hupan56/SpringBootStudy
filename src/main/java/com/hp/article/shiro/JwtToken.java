package com.hp.article.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 这个就类似UsernamePasswordToken
 */
public class JwtToken implements AuthenticationToken {
    //返回值都是jwt

    private String jwt;

    public JwtToken(String jwt) {
        this.jwt = jwt;
    }

    /**
     * 类似是用户名
     * @return
     */
    @Override
    public Object getPrincipal() {
        return jwt;
    }

    /**
     * 类似密码
     * @return
     */
    @Override
    public Object getCredentials() {
        return jwt;
    }

}