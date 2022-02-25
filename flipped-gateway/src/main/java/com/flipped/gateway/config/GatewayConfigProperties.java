package com.flipped.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * 网关配置文件
 * <p>
 * RefreshScope 配置动态刷新
 *
 * @author cwd
 * @date 2022/1/14 下午4:27
 */
@RefreshScope
@ConfigurationProperties("gateway")
public class GatewayConfigProperties {
    /**
     * 网关解密登录前端密码 秘钥 {@link com.pig4cloud.pig.gateway.filter.PasswordDecoderFilter}
     */
    private String encodeKey;

    /**
     * 网关不需要校验验证码的客户端 {@link com.flipped.gateway.filter.ValidateCodeGatewayFilter}
     */
    private List<String> ignoreClients;

    //region  getter and setter
    public String getEncodeKey() {
        return encodeKey;
    }

    public void setEncodeKey(String encodeKey) {
        this.encodeKey = encodeKey;
    }

    public List<String> getIgnoreClients() {
        return ignoreClients;
    }

    public void setIgnoreClients(List<String> ignoreClients) {
        this.ignoreClients = ignoreClients;
    }
//endregion

}
