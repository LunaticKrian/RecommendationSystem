package com.mislab.core.systemcore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.mislab.core", "com.mislab.common"})
@MapperScan("com.mislab.core.systemcore.mapper")
public class SystemCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemCoreApplication.class, args);
    }

}
