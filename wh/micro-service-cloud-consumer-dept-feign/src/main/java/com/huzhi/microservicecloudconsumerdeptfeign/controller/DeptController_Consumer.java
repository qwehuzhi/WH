package com.huzhi.microservicecloudconsumerdeptfeign.controller;

import com.huzhi.microservicecloudconsumerdeptfeign.service.DeptFeignService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DeptController_Consumer {
    @Resource
    private DeptFeignService deptFeignService;
    @RequestMapping("/ceshi/builder")
    public String ceshiBuilder(){
        return deptFeignService.ceshiBuilder();
    }
}
