package com.liyz.cloud.service.staff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liyz.cloud.common.feign.constant.Device;
import com.liyz.cloud.service.staff.model.StaffLogoutLogDO;

import java.util.Date;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/6/15 14:00
 */
public interface StaffLogoutLogService extends IService<StaffLogoutLogDO> {

    /**
     * 获取上次登出时间
     *
     * @param staffId 员工ID
     * @return 上次登出时间
     */
    Date lastLogoutTime(Long staffId, Device device);
}
