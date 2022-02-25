package com.flipped.auth.config;

import com.flipped.security.grant.CustomAuthenticationProvider;
import com.flipped.security.handler.FormAuthenticationFailureHandler;
import com.flipped.security.handler.SsoLogoutSuccessHandler;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 认证相关配置
 *
 * @author cwd
 * @date 2022/1/25 上午9:41
 */
@Primary
@Order(90)
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authenticationProvider(new CustomAuthenticationProvider())//
                .formLogin().loginPage("/token/login").loginProcessingUrl("/token/form")
                .failureHandler(authenticationFailureHandler()).and().logout()
                .logoutSuccessHandler(logoutSuccessHandler()).deleteCookies("JSESSIONID").invalidateHttpSession(true)
                .and().authorizeRequests().antMatchers("/token/**", "/actuator/**", "/mobile/**").permitAll()
                .anyRequest().authenticated().and().csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
    }


    @Bean
    @Override
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new FormAuthenticationFailureHandler();
    }
    /**
     * 支持SSO 退出
     * @return LogoutSuccessHandler
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new SsoLogoutSuccessHandler();
    }


    /**
     * 密码加密器
     *
     * @return 加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
