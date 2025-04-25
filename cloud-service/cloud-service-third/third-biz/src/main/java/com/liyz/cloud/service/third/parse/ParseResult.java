package com.liyz.cloud.service.third.parse;

import com.liyz.cloud.common.feign.bo.RemotePage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 17:02
 */
@Getter
@Setter
public class ParseResult<T> {

    /**
     * 是否还需要调用第三方接口
     */
    private boolean callThird;

    /**
     * 三方状态码
     */
    private String thirdStatus;

    /**
     * 三方返回信息
     */
    private String thirdMessage;

    /**
     * 结果字符串
     */
    private String response;

    /**
     * 结果对象
     */
    private List<T> dataList;

    /**
     * 分页对象
     */
    private RemotePage<?> page;
}
