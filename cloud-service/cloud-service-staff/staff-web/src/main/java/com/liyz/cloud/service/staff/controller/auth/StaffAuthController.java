package com.liyz.cloud.service.staff.controller.auth;

import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserLoginDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserLogoutDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserRegisterDTO;
import com.liyz.cloud.service.staff.biz.FeignAuthBiz;
import com.liyz.cloud.service.staff.constants.StaffConstants;
import com.liyz.cloud.service.staff.feign.StaffAuthFeignService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/4/30 13:38
 */
@Tag(name = "客户鉴权")
@RestController
@RequestMapping(StaffConstants.STAFF_AUTH_URL)
public class StaffAuthController implements StaffAuthFeignService {

    @Resource
    private FeignAuthBiz feignAuthBiz;


    @Override
    public void registry(AuthUserRegisterDTO authUserRegister) {
        feignAuthBiz.registry(authUserRegister);
    }

    @Override
    public AuthUserBO login(AuthUserLoginDTO authUserLogin) {
        return feignAuthBiz.login(authUserLogin);
    }

    @Override
    public Boolean logout(AuthUserLogoutDTO authUserLogout) {
        return feignAuthBiz.logout(authUserLogout);
    }

    @Override
    public List<AuthUserBO.AuthGrantedAuthorityBO> authorities(AuthUserDTO authUser) {
        return feignAuthBiz.authorities(authUser);
    }
}
