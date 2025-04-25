package com.liyz.cloud.service.third.vo.qcc.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Desc:企查查模糊搜索
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 9:42
 */
@Getter
@Setter
public class QccCompanySearchVO {

    /**
     * 主键
     */
    @JsonProperty("KeyNo")
    private String keyNo;

    /**
     * 企业名称
     */
    @JsonProperty("Name")
    private String Name;

    /**
     * 统一社会信用代码
     */
    @JsonProperty("CreditCode")
    private String creditCode;

    /**
     * 成立日期
     */
    @JsonProperty("StartDate")
    private String startDate;

    /**
     * 法人
     */
    @JsonProperty("OperName")
    private String operName;

    /**
     * 企业状态
     */
    @JsonProperty("Status")
    private String status;

    /**
     * 注册号
     */
    @JsonProperty("No")
    private String no;

    /**
     * 注册地址
     */
    @JsonProperty("Address")
    private String address;
}
