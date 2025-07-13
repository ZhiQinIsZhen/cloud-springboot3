package com.liyz.cloud.service.search.controller;

import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.common.feign.dto.PageDTO;
import com.liyz.cloud.service.search.constants.SearchConstants;
import com.liyz.cloud.service.search.es.service.CompanySearchEsService;
import com.liyz.cloud.service.search.feign.CompanySearchFeignService;
import com.liyz.cloud.service.search.vo.CompanySearchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-07-13 16:25
 */
@Tag(name = "公司搜索")
@RestController
@RequestMapping(SearchConstants.COMPANY_SEARCH_URL)
public class CompanySearchController implements CompanySearchFeignService {

    @Resource
    private CompanySearchEsService companySearchEsService;

    @Operation(summary = "分页搜索公司信息")
    @Override
    public RemotePage<CompanySearchVO> page(PageDTO pageDTO) {
        return null;
    }
}
