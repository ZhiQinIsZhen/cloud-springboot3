# Spring Cloud Project By Jdk 21

[![Build Status](https://img.shields.io/badge/Build-ZhiQinlsZhen-red)](https://github.com/ZhiQinIsZhen)
[![License](https://img.shields.io/badge/License-MIT-yellow)](https://github.com/ZhiQinIsZhen/springcloud-liyz/blob/master/LICENSE)
![JDK Version](https://img.shields.io/badge/JDK-21-brightgreen)
![Springboot Version](https://img.shields.io/badge/Springboot-3.3.7-brightgreen)
[![Spring Cloud](https://img.shields.io/badge/Springcloud-2023.0.4-brightgreen)](https://spring.io/projects/spring-cloud)
![Gateway Version](https://img.shields.io/badge/Gateway-4.1.5-brightgreen)
![Springboot-Admin Version](https://img.shields.io/badge/Admin-3.3.6-brightgreen)
![jjwt Version](https://img.shields.io/badge/jjwt-0.12.6-brightgreen)
![Mybatis-plus Version](https://img.shields.io/badge/MybatisPlus-3.5.9-brightgreen)
![Sharding-jdbc Version](https://img.shields.io/badge/ShardingJdbc-5.5.1-brightgreen)
![Swagger Version](https://img.shields.io/badge/knife4j-4.5.0-brightgreen)
![Xxl-job Version](https://img.shields.io/badge/xxljob-2.4.2-brightgreen)
![Elasticjob Version](https://img.shields.io/badge/elasticjob-3.0.4-brightgreen)

这是一个基于Jdk21，框架是SpringCloud + Springboot3 + Mybatis-plus的脚手架。

- 主框架基于：Spring Cloud、Spring Boot
- 网关基于：Spring-cloud-gateway
- 数据ORM基于：Mybatis-plus
- 注册中心基于：Eureka
- 监控中心基于：Springboot Admin
- 登陆安全基于：Spring Security、jjwt
- 接口文档基于：knife4j
- 分库分表读写分离基于：Sharding-jdbc
- 分布式定时任务基于：Xxl-job


## 目录结构说明
1. `cloud-dependencies-bom`：Maven Pom版本管理文件
2. `cloud-gateway`：前置网关层，集成限流与JWT验证
3. `cloud-api`：后置网关层，即真正的入口
4. `cloud-common`：基础包的框架
5. `cloud-service`：Feign的服务提供者，即业务服务
6. `cloud-gateway`：统一网关层，鉴权限流都会在这步做，[接口文档地址](http://127.0.0.1:8080/doc.html)
    ```text
    6.1 GlobalJWTFilter：认证全局过滤器
    6.2 GlobalAuthorityFilter：鉴权全局过滤器
    6.3 GlobalAuthInfoHeaderFilter：认证信息header透传过滤器
    ```

## api结构说明
1. `cloud-api-staff`: 管理后台网关层，鉴权基于spring-security，[接口文档地址](http://127.0.0.1:7071/staff/doc.html)
2. `cloud-api-monitor`: 监控平台 [监控地址](http://127.0.0.1:7060)

## common结构说明
1. `cloud-common-api`: 通用web或者网关层框架，以及SpringSecurity鉴权实现
2. `dubbo-common-dao`: 通用DAO层的框架(基于Mybatis-plus)
3. `dubbo-common-base`: 业务通用核心框架
4. `dubbo-common-exception`: 业务异常通用框架
5. `dubbo-common-feign`: SpringCloud的feign通用框架
6. `dubbo-common-util`: 通用工具类框架
7. `dubbo-common-xxl-job`: 通用定时任务框架

## service结构说明

1. `cloud-service-auth`: 认证资源服务，基于SpringSecurity以及jwt
2. `cloud-service-staff`: 员工信息服务
    ```text
    1.1 使用shardingsphere-jdbc对登录登出日志进行分表
    ```

## 开源共建
1. 如有问题可以提交[issue](https://github.com/ZhiQinIsZhen/cloud-springboot3/issues)
2. 如有需要Dubbo项目，请点击[Spring Boot + Dubbo](https://github.com/ZhiQinIsZhen/dubbo-springboot-project)