package com.liyz.cloud.common.api.context;

import com.google.common.base.Joiner;
import com.liyz.cloud.common.api.constant.SecurityClientConstant;
import com.liyz.cloud.common.api.user.AuthUserDetails;
import com.liyz.cloud.common.api.util.CookieUtil;
import com.liyz.cloud.common.api.util.HttpServletContext;
import com.liyz.cloud.common.base.util.BeanUtil;
import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.exception.RemoteServiceException;
import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.common.feign.bo.jwt.AuthJwtBO;
import com.liyz.cloud.common.feign.constant.Device;
import com.liyz.cloud.common.feign.constant.LoginType;
import com.liyz.cloud.common.feign.dto.auth.AuthUserDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserLoginDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserLogoutDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserRegisterDTO;
import com.liyz.cloud.common.feign.result.Result;
import com.liyz.cloud.common.util.PatternUtil;
import com.liyz.cloud.common.util.constant.CommonConstant;
import com.liyz.cloud.service.auth.feign.AuthFeignService;
import com.liyz.cloud.service.auth.feign.JwtParseFeignService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/11/24 10:39
 */
@Component
public class AuthContext implements EnvironmentAware, ApplicationContextAware, InitializingBean {

    private static final InheritableThreadLocal<AuthUserBO> innerContext = new InheritableThreadLocal<>();

    private static String clientId;
    private static ApplicationContext applicationContext;
    private static AuthenticationManager authenticationManager;
    private static AuthFeignService authFeignService;
    private static JwtParseFeignService jwtParseFeignService;

    /**
     * 获取认证用户
     *
     * @return 认证用户
     */
    public static AuthUserBO getAuthUser() {
        return innerContext.get();
    }

    /**
     * 设置认证用户
     *
     * @param authUser 认证用户
     */
    public static void setAuthUser(AuthUserBO authUser) {
        innerContext.set(authUser);
    }

    /**
     * 移除认证用户
     */
    public static void remove() {
        innerContext.remove();
    }

    /**
     * 认证服务
     */
    public static class AuthService {

        /**
         * 用户注册
         *
         * @param authUserRegister 用户注册参数
         * @return 是否注册成功
         */
        public static Boolean registry(AuthUserRegisterDTO authUserRegister) {
            authUserRegister.setClientId(clientId);
            Result<Boolean> result = authFeignService.registry(authUserRegister);
            if (CommonExceptionCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                return result.getData();
            }
            throw new RemoteServiceException(result.getCode(), result.getMessage());
        }

        /**
         * 登录
         *
         * @param authUserLoginDTO 登录参数
         * @return 登录用户信息
         */
        public static AuthUserBO login(AuthUserLoginDTO authUserLoginDTO) {
            authUserLoginDTO.setClientId(clientId);
            authUserLoginDTO.setDevice(DeviceContext.getDevice(HttpServletContext.getRequest()));
            authUserLoginDTO.setLoginType(LoginType.getByType(PatternUtil.checkMobileEmail(authUserLoginDTO.getUsername())));
            authUserLoginDTO.setIp(HttpServletContext.getIpAddress());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    Joiner.on(CommonConstant.DEFAULT_JOINER).join(
                            authUserLoginDTO.getDevice().getType(),
                            authUserLoginDTO.getClientId(),
                            authUserLoginDTO.getUsername()),
                    authUserLoginDTO.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));
            AuthUserDetails authUserDetails = (AuthUserDetails) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();
            Result<Date> result = authFeignService.login(
                    authUserLoginDTO.builder()
                            .clientId(authUserLoginDTO.getClientId())
                            .authId(authUserDetails.getAuthUser().getAuthId())
                            .loginType(authUserLoginDTO.getLoginType())
                            .device(authUserLoginDTO.getDevice())
                            .ip(authUserLoginDTO.getIp())
                            .build());
            Date checkTime;
            if (CommonExceptionCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                checkTime = result.getData();
            } else {
                throw new RemoteServiceException(result.getCode(), result.getMessage());
            }
            AuthUserBO authUser = authUserDetails.getAuthUser();
            authUser.setCheckTime(checkTime);
            AuthJwtBO authJwtBO = JwtService.generateToken(authUser);
            AuthUserBO authUserBO = BeanUtil.copyProperties(authUserDetails.getAuthUser(), AuthUserBO::new, (s, t) -> {
                t.setPassword(null);
                t.setSalt(null);
                s.setCheckTime(checkTime);
                t.setToken(authJwtBO.getToken());
            });
            CookieUtil.addCookie(
                    SecurityClientConstant.DEFAULT_TOKEN_HEADER_KEY,
                    authJwtBO.getJwtPrefix() + authUserBO.getToken(),
                    30 * 60,
                    null
            );
            return authUserBO;
        }

        /**
         * 根据登录名查询用户信息
         *
         * @param username 用户名
         * @return 用户信息
         */
        public static AuthUserBO loadByUsername(String username, Device device) {
            AuthUserDTO authUserDTO = new AuthUserDTO();
            authUserDTO.setUsername(username);
            authUserDTO.setDevice(device);
            Result<AuthUserBO> result = authFeignService.loadByUsername(authUserDTO);
            if (CommonExceptionCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                return result.getData();
            }
            throw new RemoteServiceException(result.getCode(), result.getMessage());
        }

        /**
         * 登出
         *
         * @return 是否登出成功
         */
        public static Boolean logout() {
            SecurityContextHolder.clearContext();
            AuthUserBO authUser = getAuthUser();
            if (Objects.isNull(authUser)) {
                return Boolean.FALSE;
            }
            AuthUserLogoutDTO authUserLogoutDTO = BeanUtil.copyProperties(authUser, AuthUserLogoutDTO::new, (s, t) -> {
                t.setLogoutType(s.getLoginType());
                t.setDevice(s.getDevice());
                t.setIp(HttpServletContext.getIpAddress());
            });
            CookieUtil.removeCookie(SecurityClientConstant.DEFAULT_TOKEN_HEADER_KEY);
            Result<Boolean> result = authFeignService.logout(authUserLogoutDTO);
            if (CommonExceptionCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                return result.getData();
            }
            throw new RemoteServiceException(result.getCode(), result.getMessage());
        }
    }

    /**
     * JWT服务
     */
    public static class JwtService {

        /**
         * 解析token
         *
         * @param token jwt
         * @return 用户信息
         */
        public static AuthUserBO parseToken(final String token) {
            Result<AuthUserBO> result = jwtParseFeignService.parseToken(token, clientId);
            if (CommonExceptionCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                return result.getData();
            }
            throw new RemoteServiceException(result.getCode(), result.getMessage());
        }

        /**
         * 创建token
         *
         * @param authUser 认证用户信息
         * @return jwt
         */
        public static AuthJwtBO generateToken(final AuthUserBO authUser) {
            authUser.setClientId(clientId);
            Result<AuthJwtBO> result = jwtParseFeignService.generateToken(authUser);
            if (CommonExceptionCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                return result.getData();
            }
            throw new RemoteServiceException(result.getCode(), result.getMessage());
        }

        /**
         * 获取失效时间
         *
         * @param token jwt
         * @return 失效时间戳
         */
        public static Long getExpiration(final String token) {
            Result<Long> result = jwtParseFeignService.getExpiration(token);
            if (CommonExceptionCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                return result.getData();
            }
            throw new RemoteServiceException(result.getCode(), result.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        authFeignService = applicationContext.getBean(AuthFeignService.class);
        jwtParseFeignService = applicationContext.getBean(JwtParseFeignService.class);
        authenticationManager = applicationContext.getBean(SecurityClientConstant.AUTH_MANAGER_BEAN_NAME,
                AuthenticationManager.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AuthContext.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        clientId = environment.getProperty(SecurityClientConstant.CLIENT_ID);
    }
}
