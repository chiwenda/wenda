package com.flipped.security.component;

import com.flipped.common.core.contants.CacheConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * TokenStore自动配置
 *
 * @author chiwenda
 * @date 2022/2/7 上午9:24
 */
public class TokenStoreAutoConfiguration {

    @Bean
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix(CacheConstants.PROJECT_OAUTH_ACCESS);
        return redisTokenStore;
    }
}
