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

该项目基于JDK21 + Springboot(3.4.x) + SpringCloud来进行搭建的。注册中心使用Nacos，认证鉴权使用SpringSecurity，分库分表使用ShardingJDBC，接口文档使用Knife4j，定时任务使用XxlJob。

## 项目搭建

- JDK：选择JDK21。`VirtualThreadUtil`：虚拟线程工具类
- 主框架：Springboot + SpringCloud，最新版本
- [Spring Security](https://spring.io/projects/spring-security)：认证框架选用SpringSecurity
- [Mybatis-plus](https://baomidou.com/)：ORM选择Mybatis-plus
- [knife4j](https://doc.xiaominfo.com)：接口文档
- [Xxl-Job](https://www.xuxueli.com/xxl-job/)：定时任务
- [ShardingJDBC](https://shardingsphere.apache.org/index_zh.html)：分库分表
- Springboot-Admin：监控中心

## 目录结构说明

1. `cloud-dependencies-bom`：Maven Pom版本管理文件
2. `cloud-api`：后置网关层，即真正的入口
- 2.1 `cloud-api-monitor` : 监控平台 [监控地址](http://127.0.0.1:7060)
![监控中心](/document/monitor.jpg)
![cloud-service-auth](/document/cloud-service-auth.jpg)
- 2.2 `cloud-api-staff`：员工服务业务网关层
3. `cloud-common`：基础包的框架
- 3.1 `cloud-common-api`: 通用Web或者网关层框架，以及SpringSecurity鉴权实现
  ```text
  AuthExceptionHandleAdvice：全局异常Advice(认证鉴权)
  Anonymous：匿名访问注解 注：加在Mapping的类或者方法上，折该方法不会进行认证处理
  JwtAuthenticationTokenFilter：JWT认证Filter
  UserDetailsServiceImpl：SpringSecurity(UserDetailsService)实现类
  ```
- 3.2 `dubbo-common-dao`: 通用DAO层的框架(基于Mybatis-plus)
  ```text
  MapperParamInterceptor：入参Interceptor(加密入口)
  MapperResultInterceptor：出参Interceptor(解密入口) 
  ```
- 3.3 `dubbo-common-base`: 业务通用核心框架
  ```text
  GlobalControllerExceptionAdvice：全局异常Advice(业务)
  ResultFeignDecoderAdvice：Feign的编码器
  ResultResponseBodyAdvice：ResponseBody Advice(也可以换成Feign的解码器)
  ```
- 3.4 `dubbo-common-exception`: 业务异常通用框架
- 3.5 `dubbo-common-feign`: SpringCloud的feign通用框架
- 3.6 `dubbo-common-util`: 通用工具类框架
- 3.7 `dubbo-common-xxl-job`: 通用定时任务框架
4 `cloud-service`：Feign的服务提供者，即业务服务
- 4.1 `cloud-service-auth`: 认证资源服务，基于SpringSecurity以及JWT
- 4.2 `cloud-service-staff`: 员工信息服务
    ```text
    4.2.1 使用shardingsphere-jdbc对登录登出日志进行分表
    ```
5 `cloud-gateway`：统一网关层，鉴权限流都会在这步做，[接口文档地址](http://127.0.0.1:8080/doc.html)
- 6.1 `GlobalRequestTimeFilter`：打印每个请求时间的耗时(生产可以加上配置，只打印耗时超过ms的请求)
- 6.2 `GlobalJWTFilter`：认证全局过滤器
- 6.2 `GlobalAuthorityFilter`：鉴权全局过滤器
- 6.3 `GlobalAuthInfoHeaderFilter`：认证信息header透传过滤器
![接口文档](/document/gateway-doc.jpg)

## 开源共建
1. 如有问题可以提交[issue](https://github.com/ZhiQinIsZhen/cloud-springboot3/issues)
2. 如有需要Dubbo项目，请点击[Spring Boot + Dubbo](https://github.com/ZhiQinIsZhen/dubbo-springboot-project)