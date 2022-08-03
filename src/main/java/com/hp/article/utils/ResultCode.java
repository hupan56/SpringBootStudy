package com.hp.article.utils;

public enum ResultCode {
    SUCCESS(200,"操作成功"),
    FAILED(500,"操作失败"),
    VALIDATE_FAILED(404,"参数检验失败"),
    UNAUTHORIZED(401,"暂未登陆或相关token已经过期"),
    FORBIDDEN(403,"没有相关权限");

    private Integer code;
    private String msg;

    ResultCode(Integer code, String msg) {
        this.code =  code;
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }


    public String getMessage() {
        return msg;
    }



}
