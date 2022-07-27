package com.hp.article.utils;

import lombok.Data;

@Data
public class ResultUtil {
    private boolean isSuccess;
    private Object result;
    private String message;


    public ResultUtil(){
    }
    public ResultUtil(boolean isSuccess){
        this.isSuccess=isSuccess;
    }
    public ResultUtil(boolean isSuccess2,Object result){
        this.isSuccess=isSuccess2;
        this.result=result;
    }
    public ResultUtil(boolean isSuccess,String message){
        this.isSuccess=isSuccess;
        this.message=message;
    }
}
