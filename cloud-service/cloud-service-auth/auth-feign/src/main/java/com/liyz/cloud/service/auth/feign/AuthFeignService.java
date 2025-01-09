package com.liyz.cloud.service.auth.feign;

import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserLoginDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserLogoutDTO;
import com.liyz.cloud.common.feign.dto.auth.AuthUserRegisterDTO;
import com.liyz.cloud.service.auth.constant.AuthConstants;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/10/30 19:41
 */
@FeignClient(value = AuthConstants.APPLICATION_NAME, contextId = "AuthFeignService",
        path = AuthConstants.CONTEXT_PATH + AuthConstants.AUTH_AUTH_URL)
public interface AuthFeignService {

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    void registry(@RequestBody AuthUserRegisterDTO authUserRegister);

    @Operation(summary = "登录")
    @PostMapping("/login")
    AuthUserBO login(@RequestBody AuthUserLoginDTO authUserLogin);

    @Operation(summary = "登出")
    @PostMapping("/logout")
    Boolean logout(@RequestBody AuthUserLogoutDTO authUserLogout);

    @Operation(summary = "获取权限列表")
    @PostMapping("/authorities")
    List<AuthUserBO.AuthGrantedAuthorityBO> authorities(@RequestBody AuthUserDTO authUser);
}
