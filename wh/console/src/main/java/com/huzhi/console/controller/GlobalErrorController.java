package com.huzhi.console.controller;

import com.huzhi.module.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GlobalErrorController implements ErrorController {
    @RequestMapping("/error")
    public Response error(){
        return new Response<>(4004);
    }
}
