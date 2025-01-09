package com.liyz.cloud.common.api.user;

import com.liyz.cloud.common.api.constant.SecurityClientConstant;
import com.liyz.cloud.common.api.context.DeviceContext;
import com.liyz.cloud.common.api.util.HttpServletContext;
import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.common.feign.constant.LoginType;
import com.liyz.cloud.common.feign.dto.auth.AuthUserLoginDTO;
import com.liyz.cloud.common.util.PatternUtil;
import com.liyz.cloud.service.auth.feign.AuthFeignService;
import jakarta.annotation.Resource;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/6/14 15:45
 */
public class UserDetailsServiceImpl implements UserDetailsService, EnvironmentAware {

    private static String clientId;

    @Resource
    private AuthFeignService authFeignService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserLoginDTO authUserLogin = AuthUserLoginDTO.builder()
                .username(username)
                .clientId(clientId)
                .loginType(LoginType.getByType(PatternUtil.checkMobileEmail(username)))
                .device(DeviceContext.getDevice(HttpServletContext.getRequest()))
                .ip(HttpServletContext.getIpAddress())
                .build();
        AuthUserBO authUserBO = authFeignService.login(authUserLogin);
        if (Objects.isNull(authUserBO)) {
            throw new UsernameNotFoundException(CommonExceptionCodeEnum.AUTHORIZATION_FAIL.getMessage());
        }
        return AuthUserDetails.build(authUserBO);
    }

    @Override
    public void setEnvironment(Environment environment) {
        clientId = environment.getProperty(SecurityClientConstant.CLIENT_ID);
    }
}
