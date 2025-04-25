package com.liyz.cloud.service.third.controller;

import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.service.third.constant.ThirdConstant;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import com.liyz.cloud.service.third.feign.ThirdDataFeignService;
import com.liyz.cloud.service.third.service.ThirdDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-25 11:00
 */
@Tag(name = "三方服务")
@RestController
@RequestMapping(ThirdConstant.THIRD_DATA_URL)
public class ThirdDataController<Q extends ThirdBaseDTO, T> implements ThirdDataFeignService<Q, T> {

    @Resource
    private ThirdDataService<Q, T> thirdDataService;

    @Override
    public T getOne(Q req) {
        return thirdDataService.getOne(req);
    }

    @Override
    public List<T> getList(Q req) {
        return thirdDataService.getList(req);
    }

    @Override
    public RemotePage<T> getPage(Q req) {
        return thirdDataService.getPage(req);
    }
}
