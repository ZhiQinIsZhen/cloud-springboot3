package com.liyz.cloud.service.third;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Desc:启动类
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-21 15:18
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.liyz.cloud.service.*.feign"})
@MapperScan(basePackages = {"com.liyz.cloud.service.third.dao"})
@SpringBootApplication
public class ServiceThirdApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceThirdApplication.class, args);
    }
}