package com.liyz.cloud.api.staff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Desc:启动类
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/4/30 9:25
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.liyz.cloud.service.*.feign"})
@SpringBootApplication(exclude = {EurekaClientAutoConfiguration.class, EurekaDiscoveryClientConfiguration.class})
public class ApiStaffApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiStaffApplication.class, args);
    }
}