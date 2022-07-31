package com.hp.article.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装用户的主要信息，用于放置到token中返回给客户端
 */
@Data
public class AccountProfile implements Serializable {

    private Long id;

    private String adminName;

}
