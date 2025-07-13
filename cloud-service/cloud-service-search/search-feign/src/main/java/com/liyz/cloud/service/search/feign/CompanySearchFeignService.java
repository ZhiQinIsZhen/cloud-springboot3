package com.liyz.cloud.service.search.feign;

import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.common.feign.dto.PageDTO;
import com.liyz.cloud.service.search.constants.SearchConstants;
import com.liyz.cloud.service.search.vo.CompanySearchVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-07-13 16:17
 */
@FeignClient(value = SearchConstants.APPLICATION_NAME, contextId = "CompanySearchFeign",
        path = SearchConstants.CONTEXT_PATH + SearchConstants.COMPANY_SEARCH_URL)
public interface CompanySearchFeignService {

    @Operation(summary = "分页搜索公司信息")
    @PostMapping("/page")
    RemotePage<CompanySearchVO> page(@Validated @RequestBody PageDTO pageDTO);
}
