package com.flipped.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author cwd
 * @date 2022/2/1614:51
 **/
@EnableConfigurationProperties(PermitAllUrlProperties.class)
public class CustomResourceServerAutoConfiguration {
    @Bean("pms")
    public PermissionService permissionService() {
        return new PermissionService();
    }

    @Bean
    public CustomBearerTokenExtractor pigBearerTokenExtractor(PermitAllUrlProperties urlProperties) {
        return new CustomBearerTokenExtractor(urlProperties);
    }

    @Bean
    public ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint(ObjectMapper objectMapper) {
        return new ResourceAuthExceptionEntryPoint(objectMapper);
    }

    @Bean
    @Primary
    public ResourceServerTokenServices resourceServerTokenServices(TokenStore tokenStore) {
        return new CustomLocalResourceServerTokenServices(tokenStore);
    }
}
