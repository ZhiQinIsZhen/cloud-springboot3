package com.liyz.cloud.service.staff.controller.auth;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyz.cloud.common.util.DateUtil;
import com.liyz.cloud.common.util.JsonUtil;
import com.liyz.cloud.service.staff.model.StaffInfoDO;
import com.liyz.cloud.service.staff.model.StaffLoginLogDO;
import com.liyz.cloud.service.staff.service.StaffInfoService;
import com.liyz.cloud.service.staff.service.StaffLoginLogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/4/30 13:38
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StaffAuthControllerTest {

    @Resource
    private StaffLoginLogService staffLoginLogService;
    @Resource
    private StaffInfoService staffInfoService;

    @Test
    public void batchInsertTest() {
        List<StaffLoginLogDO> doList = new ArrayList<>();

        StaffLoginLogDO staffLoginLogDO = new StaffLoginLogDO();
        staffLoginLogDO.setStaffId(1L);
        staffLoginLogDO.setLoginTime(DateUtil.date());
        staffLoginLogDO.setLoginType(1);
        staffLoginLogDO.setDevice(2);
        staffLoginLogDO.setIp("127.0.0.1");
        doList.add(staffLoginLogDO);

        staffLoginLogDO = new StaffLoginLogDO();
        staffLoginLogDO.setStaffId(1L);
        staffLoginLogDO.setLoginTime(DateUtil.date());
        staffLoginLogDO.setLoginType(1);
        staffLoginLogDO.setDevice(2);
        staffLoginLogDO.setIp("127.0.0.1");
        doList.add(staffLoginLogDO);
        boolean saveBatch = staffLoginLogService.saveBatch(doList);
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void queryTest() {
        StaffInfoDO staffInfoDO = new StaffInfoDO();
        staffInfoDO.setStaffId(1L);
        log.info(JsonUtil.toJSONString(staffInfoService.getOne(Wrappers.lambdaQuery(staffInfoDO))));
        log.info("2222");
        log.info(JsonUtil.toJSONString(staffInfoService.getOne(Wrappers.lambdaQuery(staffInfoDO))));
    }
}