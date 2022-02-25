package com.cwd.flipped;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 检索引擎启动程序
 *
 * @author cwd
 * @date 2021/12/2 下午5:37
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FlippedEsApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlippedEsApplication.class, args);
    }
}
