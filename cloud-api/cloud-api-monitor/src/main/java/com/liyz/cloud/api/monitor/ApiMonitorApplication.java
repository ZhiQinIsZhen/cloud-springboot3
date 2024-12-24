package com.liyz.cloud.api.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Desc:启动类
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/1/17 13:27
 */
@EnableDiscoveryClient
@EnableAdminServer
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.liyz.cloud.service.*.feign"})
public class ApiMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiMonitorApplication.class, args);
    }
}