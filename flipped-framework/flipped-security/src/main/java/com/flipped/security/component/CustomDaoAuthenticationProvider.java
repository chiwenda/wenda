package com.flipped.security.component;

import cn.hutool.extra.spring.SpringUtil;
import com.flipped.common.core.utils.WebUtils;
import com.flipped.security.service.CustomUserDetailsService;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

/**
 * @author cwd
 * @date 2022/2/169:25
 **/
public class CustomDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private PasswordEncoder passwordEncoder;

    private volatile String userNotFoundEncodedPassword;

    private UserDetailsService userDetailsService;

    private UserDetailsPasswordService userDetailsPasswordService;

    /**
     * 扩展授权认证检查
     *
     * @param userDetails         userDetails
     * @param authenticationToken authenticationToken
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authenticationToken)
            throws AuthenticationException {
        //token中获取不到授权信息
        if (authenticationToken.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(
                    this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"));
        }
        //检查密码是否匹配
        String presentedPassword = authenticationToken.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(
                    this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"));
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        prepareTimingAttackProtection();
        // 此处已获得 客户端认证 获取对应 userDetailsService
        Authentication clientAuthentication = SecurityContextHolder.getContext().getAuthentication();

        // SSO NPE 处理
        String clientId;
        if (clientAuthentication == null) {
            clientId = WebUtils.getRequest().get().getParameter("clientId");
        } else {
            clientId = clientAuthentication.getName();
        }

        Map<String, CustomUserDetailsService> userDetailsServiceMap = SpringUtil
                .getBeansOfType(CustomUserDetailsService.class);

        Optional<CustomUserDetailsService> optional = userDetailsServiceMap.values().stream()
                .filter(service -> service.support(clientId, null)).max(Comparator.comparingInt(Ordered::getOrder));
        if (!optional.isPresent()) {
            throw new InternalAuthenticationServiceException("UserDetailsService error , not register");
        }

        try {
            UserDetails loadedUser = optional.get().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (UsernameNotFoundException ex) {
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        } catch (InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }

    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null
                && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding) {
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
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

    protected PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }

    public void setUserDetailsPasswordService(UserDetailsPasswordService userDetailsPasswordService) {
        this.userDetailsPasswordService = userDetailsPasswordService;
    }
}
