package com.flipped;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证授权中心
 * <p>
 * EnableFeignClients 容器中注入feign接口否则会报错
 *
 * @author cwd
 * @date 2022/1/25 上午9:33
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
