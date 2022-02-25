package com.flipped.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关入口
 * EnableDiscoveryClient:开启服务注册发现
 *
 * @author cwd
 * @date 2021/11/30 上午10:55
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("=========================================================================================");
        System.out.println("                                                                                       ");
        System.out.println("                                                                                       ");
        System.out.println("                                 网关微服务启动成功                                     ");
        System.out.println("                                                                                       ");
        System.out.println("                                                                                       ");
        System.out.println("=========================================================================================");
    }
}
