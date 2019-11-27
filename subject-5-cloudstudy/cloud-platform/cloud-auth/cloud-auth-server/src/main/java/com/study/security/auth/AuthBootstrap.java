package com.study.security.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName AuthBootstrap
 * @Description 授权服务主程序
 * @Author 网易云课堂微专业-java高级开发工程师
 * @Date 2019/6/11 15:39
 * @Version 1.0
 */
@EnableFeignClients
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
// 记住这里如果使用了通用Mapper就要把@MapperScan注解从mybatis的改为Mapper的
// https://github.com/abel533/Mapper/tree/master/spring-boot-starter
@MapperScan("com.study.security.auth.mapper")
public class AuthBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(AuthBootstrap.class, args);
    }
}
