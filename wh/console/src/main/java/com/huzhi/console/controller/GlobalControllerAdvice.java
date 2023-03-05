package com.huzhi.console.controller;

import com.huzhi.module.response.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;


/**
 * 全局异常捕获
 *@RestControllerAdvice都是对Controller进行增强的，可以全局捕获spring mvc抛的异常。
 *
 * ExceptionHandler 可以全局仅捕获一种异常，也可以全局捕获多种异常，从上到下 依次处理
 *
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    /**
     * ExceptionHandler的作用是用来捕获指定的异常
     * 这里示例 捕获 Exception异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Response handleStoreAuthException(Exception e) {
        log.error(e.getMessage(),e);
        return new Response(4004,e.toString());
    }
}