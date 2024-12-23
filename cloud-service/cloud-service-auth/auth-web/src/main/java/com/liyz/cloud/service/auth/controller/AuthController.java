package com.liyz.cloud.service.auth.controller;

import com.liyz.cloud.common.api.annotation.Anonymous;
import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.exception.RemoteServiceException;
import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserLoginDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserLogoutDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserRegisterDTO;
import com.liyz.cloud.common.feign.result.Result;
import com.liyz.cloud.common.util.RandomUtil;
import com.liyz.cloud.service.auth.feign.AuthFeignService;
import com.liyz.cloud.service.auth.model.AuthJwtDO;
import com.liyz.cloud.service.auth.model.AuthSourceDO;
import com.liyz.cloud.service.auth.service.AuthJwtService;
import com.liyz.cloud.service.auth.service.AuthSourceService;
import com.liyz.cloud.service.auth.util.RedisUtil;
import com.liyz.cloud.service.staff.feign.StaffAuthFeignService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/4/30 17:47
 */
@Slf4j
@Anonymous
@Tag(name = "客户鉴权")
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthFeignService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private AuthSourceService authSourceService;
    @Resource
    private AuthJwtService authJwtService;
    @Resource
    private StaffAuthFeignService staffAuthFeignService;
    @Resource
    private RedissonClient redissonClient;


    @Override
    public Result<Boolean> registry(AuthUserRegisterDTO authUserRegister) {
        if (StringUtils.isBlank(authUserRegister.getClientId())) {
            throw new RemoteServiceException(CommonExceptionCodeEnum.LACK_SOURCE_ID);
        }
        AuthSourceDO authSourceDO = authSourceService.getByClientId(authUserRegister.getClientId());
        if (Objects.isNull(authSourceDO)) {
            throw new RemoteServiceException(CommonExceptionCodeEnum.NON_SET_SOURCE_ID);
        }
        authUserRegister.setPassword(passwordEncoder.encode(authUserRegister.getPassword()));
        authUserRegister.setSalt(RandomUtil.randomChars(16));
        return staffAuthFeignService.registry(authUserRegister);
    }

    @Override
    public Result<AuthUserBO> login(AuthUserLoginDTO authUserLogin) {
        final String clientId = authUserLogin.getClientId();
        if (StringUtils.isBlank(clientId)) {
            log.warn("用户登录错误，原因 : clientId is blank");
            throw new RemoteServiceException(CommonExceptionCodeEnum.LOGIN_ERROR);
        }
        AuthSourceDO authSourceDO = authSourceService.getByClientId(clientId);
        if (Objects.isNull(authSourceDO)) {
            log.warn("查询资源客户端ID失败，原因没有找到对应的配置信息，clientId : {}", clientId);
            throw new RemoteServiceException(CommonExceptionCodeEnum.LOGIN_ERROR);
        }
        AuthJwtDO authJwtDO = authJwtService.getByClientId(clientId);
        if (Objects.isNull(authJwtDO)) {
            log.error("解析token失败, 没有找到该应用下jwt配置信息，clientId：{}", clientId);
            throw new RemoteServiceException(CommonExceptionCodeEnum.AUTHORIZATION_FAIL);
        }
        Result<AuthUserBO> result = staffAuthFeignService.login(authUserLogin);
        if (CommonExceptionCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
            String loginKey = RandomUtil.randomChars(32);
            RSet<String> set = redissonClient.getSet(RedisUtil.getRedisKey(clientId, result.getData().getAuthId().toString()));
            if (set.isExists() && authJwtDO.getOneOnline()) {
                set.clear();
            }
            set.add(loginKey);
            set.expire(Duration.of(7, ChronoUnit.DAYS));
            result.getData().setLoginKey(loginKey);
        }
        return result;
    }

    @Override
    public Result<Boolean> logout(AuthUserLogoutDTO authUserLogout) {
        if (StringUtils.isBlank(authUserLogout.getClientId())) {
            return Result.success(Boolean.FALSE);
        }
        AuthSourceDO authSourceDO = authSourceService.getByClientId(authUserLogout.getClientId());
        if (Objects.isNull(authSourceDO)) {
            return Result.success(Boolean.FALSE);
        }
        RSet<String> set = redissonClient.getSet(RedisUtil.getRedisKey(authSourceDO.getClientId(), authUserLogout.getAuthId().toString()));
        if (set.isExists()) {
            set.remove(authUserLogout.getLoginKey());
        }
        return staffAuthFeignService.logout(authUserLogout);
    }

    @Override
    public Result<List<AuthUserBO.AuthGrantedAuthorityBO>> authorities(AuthUserDTO authUser) {
        if (StringUtils.isBlank(authUser.getClientId())) {
            log.warn("获取权限错误，原因 : client is blank");
            return Result.success(new ArrayList<>());
        }
        AuthSourceDO authSourceDO = authSourceService.getByClientId(authUser.getClientId());
        if (Objects.isNull(authSourceDO)) {
            return Result.success(new ArrayList<>());
        }
        return staffAuthFeignService.authorities(authUser);
    }
}
