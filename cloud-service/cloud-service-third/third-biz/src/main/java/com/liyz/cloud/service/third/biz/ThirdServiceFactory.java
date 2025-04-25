package com.liyz.cloud.service.third.biz;

import com.liyz.cloud.common.util.AssertUtil;
import com.liyz.cloud.service.third.constant.ThirdExceptionCodeEnum;
import com.liyz.cloud.service.third.constant.ThirdType;
import com.liyz.cloud.service.third.core.ListThirdService;
import com.liyz.cloud.service.third.core.OneThirdService;
import com.liyz.cloud.service.third.core.PageThirdService;
import com.liyz.cloud.service.third.core.abs.AbstractThirdService;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import com.liyz.cloud.service.third.dto.ThirdBasePageDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-25 10:19
 */
@Service
public class ThirdServiceFactory {

    private final Map<ThirdType, OneThirdService<? extends ThirdBaseDTO, ?>> oneThirdServiceMap = new HashMap<>();
    private final Map<ThirdType, ListThirdService<? extends ThirdBaseDTO, ?>> listThirdServiceMap = new HashMap<>();
    private final Map<ThirdType, PageThirdService<? extends ThirdBasePageDTO, ?>> pageThirdServiceMap = new HashMap<>();

    public ThirdServiceFactory(List<OneThirdService<? extends ThirdBaseDTO, ?>> oneThirdServices,
                               List<ListThirdService<? extends ThirdBaseDTO, ?>> listThirdServices,
                               List<PageThirdService<? extends ThirdBasePageDTO, ?>> pageThirdServices) {
        if (!CollectionUtils.isEmpty(oneThirdServices)) {
            oneThirdServices.forEach(item -> {
                if (item instanceof AbstractThirdService) {
                    oneThirdServiceMap.put(((AbstractThirdService<?>) item).getThirdType(), item);
                }
            });
        }
        if (!CollectionUtils.isEmpty(listThirdServices)) {
            listThirdServices.forEach(item -> {
                if (item instanceof AbstractThirdService) {
                    listThirdServiceMap.put(((AbstractThirdService<?>) item).getThirdType(), item);
                }
            });
        }
        if (!CollectionUtils.isEmpty(pageThirdServices)) {
            pageThirdServices.forEach(item -> {
                if (item instanceof AbstractThirdService) {
                    pageThirdServiceMap.put(((AbstractThirdService<?>) item).getThirdType(), item);
                }
            });
        }
    }

    public OneThirdService getOneThirdService(ThirdType thirdType) {
        OneThirdService oneThirdService = oneThirdServiceMap.get(thirdType);
        AssertUtil.notNull(oneThirdService, ThirdExceptionCodeEnum.THIRD_TYPE_NOT_EXIST);
        return oneThirdService;
    }

    public ListThirdService getListThirdService(ThirdType thirdType) {
        ListThirdService listThirdService = listThirdServiceMap.get(thirdType);
        AssertUtil.notNull(listThirdService, ThirdExceptionCodeEnum.THIRD_TYPE_NOT_EXIST);
        return listThirdService;
    }

    public PageThirdService getPageThirdService(ThirdType thirdType) {
        PageThirdService pageThirdService = pageThirdServiceMap.get(thirdType);
        AssertUtil.notNull(pageThirdService, ThirdExceptionCodeEnum.THIRD_TYPE_NOT_EXIST);
        return pageThirdService;
    }
}
