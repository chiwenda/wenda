package com.flipped.security.grant;

import cn.hutool.extra.spring.SpringUtil;
import com.flipped.security.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

/**
 * 自定义授权
 *
 * @author cwd
 * @date 2022/1/26 下午3:14
 */
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private UserDetailsChecker preAuthenticationChecks = new AccountStatusUserDetailsChecker();

    /**
     * The plaintext password used to perform PasswordEncoder#matches(CharSequence,
     * String)} on when the user is not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private PasswordEncoder passwordEncoder;

    /**
     * The password used to perform {@link PasswordEncoder#matches(CharSequence, String)}
     * on when the user is not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private volatile String userNotFoundEncodedPassword;

    /**
     * 对验证请求的返回的UserDetails进行额外的检查
     *
     * @param userDetails    用户信息
     * @param authentication 认证信息
     * @throws AuthenticationException 异常
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.log.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            log.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException("Bad credentials");
        }

        //已获取认证
        Authentication clientAuthenticate = SecurityContextHolder.getContext().getAuthentication();
        String clientId = clientAuthenticate.getName();
        Map<String, CustomUserDetailsService> userDetailsServiceMap = SpringUtil.getBeansOfType(CustomUserDetailsService.class);
        Optional<CustomUserDetailsService> optional = userDetailsServiceMap.values().stream()
                .filter(service -> service.support(clientId,null)).max(Comparator.comparingInt(Ordered::getOrder));

        if (!optional.isPresent()) {
            throw new InternalAuthenticationServiceException("UserDetailsService error , not register");
        }

        //手机号
        String phone = authentication.getName();
        UserDetails userDetails = optional.get().loadUserByUsername(phone);

        //userDetails校验
        preAuthenticationChecks.check(userDetails);

        CustomAppAuthenticationToken token = new CustomAppAuthenticationToken(userDetails);
        token.setDetails(authentication.getDetails());
        return token;
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
//        return authentication.isAssignableFrom(CustomAppAuthenticationToken.class);
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
     * not set, the password will be compared using
     * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
     *                        types.
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }
}
