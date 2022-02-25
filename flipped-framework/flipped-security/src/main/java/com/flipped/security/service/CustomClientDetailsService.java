package com.flipped.security.service;

import com.flipped.common.core.contants.CacheConstants;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * 支持从数据库获取客户端信息
 *
 * @author chiwenda
 * @date 2022/1/29 下午4:14
 */
public class CustomClientDetailsService extends JdbcClientDetailsService {
    /**
     * 初始化数据源
     *
     * @param dataSource 数据源
     */
    public CustomClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 重写原生方法支持redis缓存
     */
    @Override
    @SneakyThrows
    @Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
    public ClientDetails loadClientByClientId(String clientId) {
        return super.loadClientByClientId(clientId);
    }

}
