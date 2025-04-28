package com.liyz.cloud.service.third.feign;

import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.service.third.constant.ThirdConstant;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import com.liyz.cloud.service.third.dto.ThirdBasePageDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-25 10:55
 */
@FeignClient(value = ThirdConstant.APPLICATION_NAME, contextId = "ThirdDataFeignService",
        path = ThirdConstant.CONTEXT_PATH + ThirdConstant.THIRD_DATA_URL)
public interface ThirdDataFeignService {

    /**
     * 查询单条
     *
     * @param req 请求参数
     * @return T
     */
    @Operation(summary = "查询单条")
    @PostMapping("/getOne")
    <T> T getOne(@Valid @RequestBody ThirdBaseDTO req);

    /**
     * 查询列表
     *
     * @param req 请求参数
     * @return List
     */
    @Operation(summary = "查询列表")
    @PostMapping("/getList")
    <T> List<T> getList(@Valid @RequestBody ThirdBaseDTO req);

    /**
     * 查询分页
     *
     * @param req 请求参数
     * @return RemotePage
     */
    @Operation(summary = "查询分页")
    @PostMapping("/getPage")
    <T> RemotePage<T> getPage(@Valid @RequestBody ThirdBasePageDTO req);
}
