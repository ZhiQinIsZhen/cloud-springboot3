package com.liyz.cloud.api.staff.controller.staff;

import com.liyz.cloud.api.staff.vo.staff.StaffInfoApiVO;
import com.liyz.cloud.common.api.context.AuthContext;
import com.liyz.cloud.common.base.util.BeanUtil;
import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.common.feign.dto.PageDTO;
import com.liyz.cloud.common.feign.result.PageResult;
import com.liyz.cloud.common.feign.result.Result;
import com.liyz.cloud.service.staff.dto.log.StaffLogPageDTO;
import com.liyz.cloud.service.staff.feign.StaffInfoFeignService;
import com.liyz.cloud.service.staff.vo.info.StaffInfoVO;
import com.liyz.cloud.service.staff.vo.log.StaffLoginLogVO;
import com.liyz.cloud.service.staff.vo.log.StaffLogoutLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/6/16 16:38
 */
@Tag(name = "员工信息")
@ApiResponses(value = {
        @ApiResponse(responseCode = "0", description = "成功"),
        @ApiResponse(responseCode = "1", description = "失败")
})
@Slf4j
@RestController
@RequestMapping("/staff")
public class StaffInfoController {

    @Resource
    private StaffInfoFeignService staffInfoFeignService;

    @Operation(summary = "查询当前登录员工信息")
    @GetMapping("/current")
    public Result<StaffInfoApiVO> userInfo() {
        AuthUserBO authUser = AuthContext.getAuthUser();
        Result<StaffInfoVO> result = staffInfoFeignService.getByStaffId(authUser.getAuthId());
        return Result.result(result, BeanUtil.copyProperties(result.getData(), StaffInfoApiVO::new));
    }

    @Operation(summary = "分页查询员工信息")
    @GetMapping("/page")
    public PageResult<StaffInfoApiVO> page(PageDTO page) {
        Result<RemotePage<StaffInfoVO>> pageResult = staffInfoFeignService.page(page);
        return PageResult.result(pageResult, BeanUtil.copyRemotePage(pageResult.getData(), StaffInfoApiVO::new));
    }

    @Operation(summary = "分页查询员工登录信息")
    @GetMapping("/login/page")
    public PageResult<StaffLoginLogVO> loginPage(@Valid PageDTO page) {
        StaffLogPageDTO pageDTO = BeanUtil.copyProperties(page, StaffLogPageDTO::new, (s, t) -> {
            t.setStaffId(AuthContext.getAuthUser().getAuthId());
        });
        Result<RemotePage<StaffLoginLogVO>> pageResult = staffInfoFeignService.loginPage(pageDTO);
        return PageResult.result(pageResult, pageResult.getData());
    }

    @Operation(summary = "分页查询员工登出信息")
    @GetMapping("/logout/page")
    public PageResult<StaffLogoutLogVO> logoutPage(@Valid PageDTO page) {
        StaffLogPageDTO pageDTO = BeanUtil.copyProperties(page, StaffLogPageDTO::new, (s, t) -> {
            t.setStaffId(AuthContext.getAuthUser().getAuthId());
        });
        Result<RemotePage<StaffLogoutLogVO>> pageResult = staffInfoFeignService.logoutPage(pageDTO);
        return PageResult.result(pageResult, pageResult.getData());
    }
}
