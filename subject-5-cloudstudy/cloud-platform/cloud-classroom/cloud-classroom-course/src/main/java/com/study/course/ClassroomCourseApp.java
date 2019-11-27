package com.study.course;

import com.study.security.auth.client.EnableClientAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan(basePackages = {"com.study.security", "com.study.course"})
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients({"com.study.security.auth.client.feign", "com.study.course.client"})
@EnableClientAuthClient
@MapperScan("com.study.course.mapper")
public class ClassroomCourseApp {

	public static void main(String[] args) {
		SpringApplication.run(ClassroomCourseApp.class, args);
	}
}
