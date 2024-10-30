package com.liyz.cloud.service.staff.feign;

import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.common.feign.dto.PageDTO;
import com.liyz.cloud.common.feign.result.Result;
import com.liyz.cloud.service.staff.bo.info.StaffInfoBO;
import com.liyz.cloud.service.staff.constants.StaffConstants;
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
 * @date 2024/10/30 15:23
 */
@FeignClient(value = StaffConstants.APPLICATION_NAME, contextId = "StaffInfoFeign",
        path = StaffConstants.CONTEXT_PATH + StaffConstants.STAFF_INFO_URL)
public interface StaffInfoFeignService {

    @Operation(summary = "根据staffId获取用户信息")
    @GetMapping( "/getByStaffId")
    Result<StaffInfoBO> getByStaffId(@RequestParam("staffId") Long staffId);

    @Operation(summary = "分页查询员工信息")
    @PostMapping("/page")
    Result<RemotePage<StaffInfoBO>> page(@RequestBody PageDTO pageDTO);
}