package com.liyz.cloud.service.third.constant;

import com.liyz.cloud.service.third.dto.qcc.page.QccCompanySearchPageDTO;
import com.liyz.cloud.service.third.dto.uniview.one.UniViewLoginDTO;
import com.liyz.cloud.service.third.dto.uniview.page.UniViewDevicePageDTO;
import com.liyz.cloud.service.third.vo.qcc.page.QccCompanySearchVO;
import com.liyz.cloud.service.third.vo.uniview.one.UniViewLoginVO;
import com.liyz.cloud.service.third.vo.uniview.page.UniViewDeviceVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 9:25
 */
@Getter
@AllArgsConstructor
public enum ThirdType {
    QCC_PAGE_COMPANY_SEARCH(10010001, QccCompanySearchPageDTO.class, QccCompanySearchVO.class, "/FuzzySearch/GetList",
            "qcc_company_search", "企查查-企业模糊搜索"),


    UNIVIEW_ONE_LOGIN(10100001, UniViewLoginDTO.class, UniViewLoginVO.class, "/openapi/user/app/token/get",
            null, "宇视-登录"),
    UNIVIEW_PAGE_DEVICE(10100002, UniViewDevicePageDTO.class, UniViewDeviceVO.class, "/openapi/device/list",
            null, "宇视-设备列表"),
    ;

    /**
     * 接口类型
     * 十进制高四位代表第三方类别，低四位代表具体类型
     */
    private final Integer code;

    /**
     * 请求对象
     */
    private final Class<?> reqClazz;

    /**
     * 返回对象
     */
    private final Class<?> resClazz;

    /**
     * 子路径
     */
    private final String subUrl;

    /**
     * es索引
     */
    private final String esIndex;

    /**
     * 第三方名称
     */
    private final String thirdName;
}
