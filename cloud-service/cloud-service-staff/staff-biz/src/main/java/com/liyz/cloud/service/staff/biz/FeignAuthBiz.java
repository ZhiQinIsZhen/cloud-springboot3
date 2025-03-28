package com.liyz.cloud.service.staff.biz;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.liyz.cloud.common.base.util.BeanUtil;
import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.exception.RemoteServiceException;
import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.common.feign.constant.LoginType;
import com.liyz.cloud.common.feign.dto.auth.AuthUserDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserLoginDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserLogoutDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserRegisterDTO;
import com.liyz.cloud.common.util.DateUtil;
import com.liyz.cloud.common.util.PatternUtil;
import com.liyz.cloud.common.util.VirtualThreadUtil;
import com.liyz.cloud.service.staff.model.*;
import com.liyz.cloud.service.staff.model.base.StaffAuthBaseDO;
import com.liyz.cloud.service.staff.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/4/30 14:03
 */
@Slf4j
@Service
public class FeignAuthBiz {

    @Resource
    private StaffInfoService staffInfoService;
    @Resource
    private StaffAuthMobileService staffAuthMobileService;
    @Resource
    private StaffAuthEmailService staffAuthEmailService;
    @Resource
    private StaffLoginLogService staffLoginLogService;
    @Resource
    private StaffLogoutLogService staffLogoutLogService;
    @Resource
    private StaffAuthorityService staffAuthorityService;
    @Resource
    private StaffRoleService staffRoleService;
    @Resource
    private SystemRoleAuthorityService systemRoleAuthorityService;
    @Resource
    private SystemAuthorityService systemAuthorityService;

    /**
     * 用户注册
     *
     * @param authUserRegister 注册参数
     * @return True：注册成功；false：注册失败
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean registry(AuthUserRegisterDTO authUserRegister) {
        StaffInfoDO staffInfoDO = BeanUtil.copyProperties(authUserRegister, StaffInfoDO::new, (s, t) -> {
            t.setRegistryTime(DateUtil.date());
        });
        staffInfoService.save(staffInfoDO);
        if (StringUtils.isNotBlank(staffInfoDO.getMobile())) {
            StaffAuthMobileDO mobileDO = StaffAuthMobileDO.builder().mobile(staffInfoDO.getMobile()).build();
            mobileDO.setStaffId(staffInfoDO.getStaffId());
            mobileDO.setPassword(authUserRegister.getPassword());
            try {
                staffAuthMobileService.save(mobileDO);
            } catch (DuplicateKeyException e) {
                throw new RemoteServiceException(CommonExceptionCodeEnum.MOBILE_EXIST);
            }
        }
        if (StringUtils.isNotBlank(staffInfoDO.getEmail())) {
            StaffAuthEmailDO emailDO = StaffAuthEmailDO.builder().email(staffInfoDO.getEmail()).build();
            emailDO.setStaffId(staffInfoDO.getStaffId());
            emailDO.setPassword(authUserRegister.getPassword());
            try {
                staffAuthEmailService.save(emailDO);
            } catch (DuplicateKeyException e) {
                throw new RemoteServiceException(CommonExceptionCodeEnum.EMAIL_EXIST);
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 登录
     *
     * @param authUserLogin 登录参数
     * @return 当前登录时间
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"auth"}, key = "'lastLoginTime:' + #p0.authId + ':' + #p0.device.name()")
    public AuthUserBO login(AuthUserLoginDTO authUserLogin) {
        AuthUserBO authUser = AuthUserBO.builder()
                .clientId(authUserLogin.getClientId())
                .username(authUserLogin.getUsername())
                .loginType(authUserLogin.getLoginType())
                .device(authUserLogin.getDevice())
                .authorities(List.of())
                .build();
        Long staffId = this.getStaffId(authUserLogin.getUsername(), authUser);
        if (Objects.isNull(staffId)) {
            throw new RemoteServiceException(CommonExceptionCodeEnum.USER_NOT_EXIST);
        }
        StaffInfoDO staffInfoDO = staffInfoService.getById(staffId);
        if (Objects.isNull(staffInfoDO)) {
            throw new RemoteServiceException(CommonExceptionCodeEnum.USER_NOT_EXIST);
        }
        authUser.setAuthId(staffId);
        authUser.setSalt(staffInfoDO.getSalt());
        //查询角色信息
        List<StaffRoleDO> roles = staffRoleService.list(Wrappers.query(StaffRoleDO.builder().staffId(staffId).build()));
        authUser.setRoleIds(CollectionUtils.isEmpty(roles) ? Lists.newArrayList() : roles.stream().map(StaffRoleDO::getRoleId).collect(Collectors.toList()));
        StaffLoginLogDO staffLoginLogDO = BeanUtil.copyProperties(authUserLogin, StaffLoginLogDO::new, (s, t) -> {
            t.setStaffId(staffId);
            t.setLoginTime(DateUtil.date());
            t.setLoginType(s.getLoginType().getType());
            t.setDevice(s.getDevice().getType());
        });
        staffLoginLogService.save(staffLoginLogDO);
        return authUser;
    }

    /**
     * 登出
     *
     * @param authUserLogout 登出参数
     * @return True：登出成功；false：登出失败
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"auth"}, key = "'lastLogoutTime:' + #p0.authId + ':' + #p0.device.name()")
    public Boolean logout(AuthUserLogoutDTO authUserLogout) {
        StaffLogoutLogDO staffLogoutLogDO = BeanUtil.copyProperties(authUserLogout, StaffLogoutLogDO::new, (s, t) -> {
            t.setStaffId(s.getAuthId());
            t.setDevice(s.getDevice().getType());
            t.setLogoutTime(DateUtil.date());
        });
        return staffLogoutLogService.save(staffLogoutLogDO);
    }

    /**
     * 获取权限列表
     * todo 父角色功能暂未实现，即只有一层关系
     *
     * @param authUser 认证用户信息
     * @return 权限列表
     */
    @Cacheable(cacheNames = {"auth"}, key = "'authorities:' + #p0.authId", unless = "#result == null")
    public List<AuthUserBO.AuthGrantedAuthorityBO> authorities(AuthUserDTO authUser) {
        Set<Integer> authorityIdSet = new HashSet<>();
        VirtualThreadUtil.submit(() -> {
            //查询角色拥有的权限
            if (!CollectionUtils.isEmpty(authUser.getRoleIds())) {
                List<SystemRoleAuthorityDO> roleAuthorityList = systemRoleAuthorityService.list(Wrappers.lambdaQuery(SystemRoleAuthorityDO.class)
                        .in(SystemRoleAuthorityDO::getRoleId, authUser.getRoleIds()));
                if (!CollectionUtils.isEmpty(roleAuthorityList)) {
                    roleAuthorityList.forEach(item -> authorityIdSet.add(item.getAuthorityId()));
                }
            }
        }, () -> {
            //查询临时权限
            List<StaffAuthorityDO> list = staffAuthorityService.list(Wrappers.lambdaQuery(StaffAuthorityDO.class)
                    .eq(StaffAuthorityDO::getStaffId, authUser.getAuthId())
                    .le(StaffAuthorityDO::getAuthorityEndTime, DateUtil.date()));
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(item -> authorityIdSet.add(item.getAuthorityId()));
            }
        });
        if (CollectionUtils.isEmpty(authorityIdSet)) {
            return new ArrayList<>();
        }
        //查询权限列表
        List<SystemAuthorityDO> authorityList = systemAuthorityService.list(Wrappers.lambdaQuery(SystemAuthorityDO.class)
                .in(SystemAuthorityDO::getAuthorityId, authorityIdSet));
        return BeanUtil.copyList(authorityList, AuthUserBO.AuthGrantedAuthorityBO::new, (s, t) -> t.setAuthorityCode(s.getAuthority()));
    }

    /**
     * 根据username获取对应用户id
     *
     * @param username 用户名
     * @param authUser 认证用户信息
     * @return 用户ID
     */
    private Long getStaffId(String username, AuthUserBO authUser) {
        LoginType loginType = LoginType.getByType(PatternUtil.checkMobileEmail(username));
        if (Objects.isNull(loginType)) {
            log.warn("username is not email or mobile");
            return null;
        }
        authUser.setLoginType(loginType);
        LoginTypeService loginTypeService = LoginTypeService.LOGIN_TYPE_MAP.get(loginType);
        if (Objects.isNull(loginTypeService)) {
            log.warn("{} can not find LoginTypeService", loginType.name());
            return null;
        }
        StaffAuthBaseDO staffAuthBaseDO = loginTypeService.getByUsername(username);
        if (Objects.isNull(staffAuthBaseDO)) {
            return null;
        }
        authUser.setPassword(staffAuthBaseDO.getPassword());
        return staffAuthBaseDO.getStaffId();
    }
}
