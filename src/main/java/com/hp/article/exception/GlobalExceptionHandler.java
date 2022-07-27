package com.hp.article.exception;


import com.hp.article.utils.ResultUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler
    public ResultUtil handExpection(Exception e){

        e.printStackTrace();
        return new ResultUtil(false,"服务器内部错误，请求失败");
    }
}
