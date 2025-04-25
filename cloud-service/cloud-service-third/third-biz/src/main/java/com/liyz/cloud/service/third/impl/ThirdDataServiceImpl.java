package com.liyz.cloud.service.third.impl;

import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.common.util.AssertUtil;
import com.liyz.cloud.service.third.biz.ThirdServiceFactory;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import com.liyz.cloud.service.third.dto.ThirdBasePageDTO;
import com.liyz.cloud.service.third.service.ThirdDataService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-25 10:30
 */
@Service
public class ThirdDataServiceImpl<Q extends ThirdBaseDTO, T> implements ThirdDataService<Q, T> {

    @Resource
    private ThirdServiceFactory thirdServiceFactory;

    @Override
    public T getOne(Q req) {
        thirdServiceFactory.getOneThirdService(req.getThirdType());
        return (T) thirdServiceFactory.getOneThirdService(req.getThirdType()).queryOne(req);
    }

    @Override
    public List<T> getList(Q req) {
        return (List<T>) thirdServiceFactory.getListThirdService(req.getThirdType()).queryList(req);
    }

    @Override
    public RemotePage<T> getPage(Q req) {
        AssertUtil.isTure(req instanceof ThirdBasePageDTO, CommonExceptionCodeEnum.PARAMS_VALIDATED);
        return (RemotePage<T>) thirdServiceFactory.getPageThirdService(req.getThirdType()).queryPage((ThirdBasePageDTO) req);
    }
}
