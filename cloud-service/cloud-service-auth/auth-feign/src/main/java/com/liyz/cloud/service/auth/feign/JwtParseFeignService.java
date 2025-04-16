package com.liyz.cloud.service.auth.feign;

import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.common.feign.bo.jwt.AuthJwtBO;
import com.liyz.cloud.service.auth.constant.AuthConstants;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/10/30 19:28
 */
@FeignClient(value = AuthConstants.APPLICATION_NAME, contextId = "JwtParseFeignService",
        path = AuthConstants.CONTEXT_PATH + AuthConstants.AUTH_JWT_URL)
public interface JwtParseFeignService {

    @Operation(summary = "解析token")
    @GetMapping("/parseToken")
    AuthUserBO parseToken(@RequestParam(value = "token", required = false) String token,
                          @RequestParam(value = "clientId", required = false) String clientId);

    @Operation(summary = "生成token")
    @PostMapping("/generateToken")
    AuthJwtBO generateToken(@RequestBody AuthUserBO authUser);

    @Operation(summary = "获取失效时间")
    @GetMapping("/getExpiration")
    Long getExpiration(@RequestParam("token") final String token);
}
