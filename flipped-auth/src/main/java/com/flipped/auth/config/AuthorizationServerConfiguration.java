package com.flipped.auth.config;

import com.flipped.common.core.contants.SecurityConstants;
import com.flipped.security.component.CustomWebResponseExceptionTranslator;
import com.flipped.security.grant.CustomAuthenticationProvider;
import com.flipped.security.grant.CustomResourceOwnerAppTokenGranter;
import com.flipped.security.service.CustomClientDetailsService;
import com.flipped.security.service.CustomTokenServices;
import com.flipped.security.service.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.*;


/**
 * 授权服务器配置
 *
 * @author chiwenda
 * @date 2022/1/29 下午4:23
 * <p>
 * EnableAuthorizationServer 开启授权服务器
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;

    private final AuthenticationManager authenticationManager;

    private final TokenStore redisTokenStore;

    @Override
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        clients.withClientDetails(pigClientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.allowFormAuthenticationForClients().checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).tokenServices(tokenServices())
                .tokenStore(redisTokenStore).tokenEnhancer(tokenEnhancer()).authenticationManager(authenticationManager)
                .reuseRefreshTokens(false).pathMapping("/oauth/confirm_access", "/token/confirm_access")
                .exceptionTranslator(new CustomWebResponseExceptionTranslator());
        setTokenGranter(endpoints);
    }

    /**
     * 自定义 APP 认证类型
     *
     * @param endpoints AuthorizationServerEndpointsConfigurer
     */
    private void setTokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        // 获取默认授权类型
        TokenGranter tokenGranter = endpoints.getTokenGranter();
        ArrayList<TokenGranter> tokenGranters = new ArrayList<>(Arrays.asList(tokenGranter));
        CustomResourceOwnerAppTokenGranter resourceOwnerCustomeAppTokenGranter = new CustomResourceOwnerAppTokenGranter(
                authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());
        tokenGranters.add(resourceOwnerCustomeAppTokenGranter);
        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(tokenGranters);
        endpoints.tokenGranter(compositeTokenGranter);
    }

    /**
     * token 生成接口输出增强
     *
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>(4);
            additionalInfo.put(SecurityConstants.DETAILS_LICENSE, SecurityConstants.PROJECT_LICENSE);
            String clientId = authentication.getOAuth2Request().getClientId();
            additionalInfo.put(SecurityConstants.CLIENT_ID, clientId);

            // 客户端模式不返回具体用户信息
            if (SecurityConstants.CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
                return accessToken;
            }

            User pigUser = (User) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put(SecurityConstants.DETAILS_USER, pigUser);
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    /**
     * 客户端信息加载处理
     *
     * @return ClientDetailsService
     */
    @Bean
    public ClientDetailsService pigClientDetailsService() {
        CustomClientDetailsService clientDetailsService = new CustomClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
        return clientDetailsService;
    }

    /**
     * token 核心处理
     *
     * @return tokenServices
     */
    @Bean
    public CustomTokenServices tokenServices() {
        CustomTokenServices tokenServices = new CustomTokenServices();
        tokenServices.setTokenStore(redisTokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setClientDetailsService(pigClientDetailsService());
        tokenServices.setTokenEnhancer(tokenEnhancer());
        return tokenServices;
    }


}
