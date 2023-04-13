package com.huzhi.microservicecloudconsumerdeptfeign.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

//添加为容器内的一个组件
@Component
// 服务提供者提供的服务名称，即 application.name
@FeignClient(value = "single-app")
public interface DeptFeignService {
    //对应服务提供者（8001、8002、8003）Controller 中定义的方法
    @RequestMapping("/ceshi/builder")
    public String ceshiBuilder();
}
