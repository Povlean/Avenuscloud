package com.ean.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description:服务主启动类
 * @author:Povlean
 */

@ComponentScan(basePackages = "com.ean")
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceHospApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class,args);
    }

}
