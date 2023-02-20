package com.huzhi.console;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.huzhi.console","com.huzhi.module"})
@MapperScan("com.huzhi.module.**.mapper")
public class ConsoleApplication {

    public static void main(String[] args) {

        try {
            SpringApplication.run(ConsoleApplication.class, args);
        }catch(Exception e){
            e.printStackTrace();
        }


    }

}
