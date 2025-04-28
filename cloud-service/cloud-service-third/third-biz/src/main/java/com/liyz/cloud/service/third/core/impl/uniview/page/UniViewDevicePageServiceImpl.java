package com.liyz.cloud.service.third.core.impl.uniview.page;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.common.util.JsonUtil;
import com.liyz.cloud.service.third.constant.ThirdType;
import com.liyz.cloud.service.third.core.PageThirdService;
import com.liyz.cloud.service.third.core.abs.uniview.AbstractUniViewCacheThirdService;
import com.liyz.cloud.service.third.dto.uniview.page.UniViewDevicePageDTO;
import com.liyz.cloud.service.third.parse.ParseResult;
import com.liyz.cloud.service.third.parse.uniview.UniViewBaseResponse;
import com.liyz.cloud.service.third.parse.uniview.UniViewDevicePageResponse;
import com.liyz.cloud.service.third.vo.uniview.page.UniViewDeviceVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

/**
 * Desc:宇视设备列表
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-28 14:38
 */
@Service
public class UniViewDevicePageServiceImpl extends AbstractUniViewCacheThirdService<UniViewDevicePageDTO, UniViewDeviceVO>
        implements PageThirdService<UniViewDevicePageDTO, UniViewDeviceVO> {

    @Override
    public RemotePage<UniViewDeviceVO> queryPage(UniViewDevicePageDTO req) {
        return page(req);
    }

    @Override
    public ThirdType getThirdType() {
        return ThirdType.UNIVIEW_PAGE_DEVICE;
    }

    @Override
    protected ParseResult<UniViewDeviceVO> parsePageResponse(String response) {
        UniViewDevicePageDTO req = this.getReqByContext();
        UniViewBaseResponse<UniViewDevicePageResponse> devicePageResponse = JsonUtil.readValue(response, new TypeReference<>() {});
        ParseResult<UniViewDeviceVO> parseResult = new ParseResult<>();
        parseResult.setDataList(Lists.newArrayList());
        if (Objects.nonNull(devicePageResponse)) {
            UniViewDevicePageResponse responseData = devicePageResponse.getData();
            if (Objects.nonNull(responseData) && Objects.nonNull(responseData.getTotal())) {
                parseResult.setPage(new RemotePage<>(responseData.getDeviceList(), responseData.getTotal(), req.getPageNum(), req.getPageSize()));
            }
            if (!CollectionUtils.isEmpty(responseData.getDeviceList())) {
                parseResult.setDataList(responseData.getDeviceList());
            }
        }
        return parseResult;
    }
}
