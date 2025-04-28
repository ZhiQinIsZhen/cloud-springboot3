package com.liyz.cloud.service.third.parse.uniview;

import com.liyz.cloud.service.third.vo.uniview.page.UniViewDeviceVO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-28 14:46
 */
@Getter
@Setter
public class UniViewDevicePageResponse {

    /**
     * 总数
     */
    private Long total;

    /**
     * 设备列表
     */
    private List<UniViewDeviceVO> deviceList;
}
