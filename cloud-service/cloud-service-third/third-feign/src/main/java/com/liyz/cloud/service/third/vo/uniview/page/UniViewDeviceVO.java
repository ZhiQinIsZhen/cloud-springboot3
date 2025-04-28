package com.liyz.cloud.service.third.vo.uniview.page;

import lombok.Getter;
import lombok.Setter;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-28 14:40
 */
@Getter
@Setter
public class UniViewDeviceVO {

    /**
     * 设备序列号
     */
    private String deviceSerial;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备型号
     */
    private String deviceMode;

    /**
     * 设备版本
     */
    private String deviceVersion;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 在线状态（0-离线，1-在线）
     */
    private Integer status;

    /**
     * 最近上线时间，UTC时间戳
     */
    private Long latestOnline;

    /**
     * 是否是共享设备（0-非共享，1-通过组织共享，2-通过设备共享）
     */
    private Integer isShared;
}
