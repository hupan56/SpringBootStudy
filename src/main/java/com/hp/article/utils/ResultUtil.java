package com.hp.article.utils;

//import com.hp.article.shiro.JwtToken;
import com.hp.article.pojo.Article;
import lombok.Data;

import java.util.List;

@Data
public class ResultUtil {
    private Article data;
    private String message;
    private int code;
    private String  token;
    private boolean isSuccess;
    private List datas;
   private ResultUtil(){
    }
    public ResultUtil( boolean isSuccess,String message, Article data,String token, int code){
        this.token=token;
        this.data=data;
        this.code=code;
        this.isSuccess=isSuccess;
        this.message=message;
    }
    public ResultUtil( boolean isSuccess,String message,String token, int code){
        this.token=token;
        this.data=data;
        this.code=code;
        this.isSuccess=isSuccess;
        this.message=message;
    }
    public ResultUtil( boolean isSuccess,String message, Article data, int code){
        this.data=data;
        this.code=code;
        this.message=message;
        this.isSuccess=isSuccess;
    }
    public ResultUtil(boolean isSuccess, String message, int code){
        this.data=data;
        this.code=code;
        this.message=message;
        this.isSuccess=isSuccess;
    }
    public ResultUtil(boolean isSuccess, String message, List datas, int code){
        this.datas=datas;
        this.code=code;
        this.message=message;
        this.isSuccess=isSuccess;

    }




}
