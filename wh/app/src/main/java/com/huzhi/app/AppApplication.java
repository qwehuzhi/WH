package com.huzhi.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages={"com.huzhi.app","com.huzhi.module"})
@MapperScan({"com.huzhi.module.**.mapper","com.huzhi.module.*.*.mapper"})
public class AppApplication {

    public static void main(String[] args) {

        try {
            SpringApplication.run(AppApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
