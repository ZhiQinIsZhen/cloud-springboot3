package com.liyz.cloud.service.third.core.impl.qcc.page;

import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.service.third.constant.ThirdType;
import com.liyz.cloud.service.third.core.PageThirdService;
import com.liyz.cloud.service.third.core.abs.qcc.AbstractQccCacheThirdService;
import com.liyz.cloud.service.third.dto.qcc.page.QccCompanySearchPageDTO;
import com.liyz.cloud.service.third.vo.qcc.page.QccCompanySearchVO;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-25 10:09
 */
@Service
public class QccCompanySearchPageServiceImpl extends AbstractQccCacheThirdService<QccCompanySearchPageDTO, QccCompanySearchVO>
        implements PageThirdService<QccCompanySearchPageDTO, QccCompanySearchVO> {

    @Override
    public ThirdType getThirdType() {
        return ThirdType.QCC_PAGE_COMPANY_SEARCH;
    }

    @Override
    public RemotePage<QccCompanySearchVO> queryPage(QccCompanySearchPageDTO req) {
        return page(req);
    }

    @Override
    protected MultiValueMap<String, String> addQueryParams(QccCompanySearchPageDTO req) {
        MultiValueMap<String, String> queryParams = super.addQueryParams(req);
        if (Objects.nonNull(req.getPageNum())) {
            queryParams.add("pageNum", req.getPageNum().toString());
        }
        if (Objects.nonNull(req.getPageSize())) {
            queryParams.add("pageSize", req.getPageSize().toString());
        }
        queryParams.add("searchKey", req.getSearchKey());
        queryParams.add("provinceCode", req.getProvinceCode());
        queryParams.add("cityCode", req.getCityCode());
        return queryParams;
    }
}
