package com.liyz.cloud.service.staff.controller.info;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyz.cloud.common.base.util.BeanUtil;
import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.exception.RemoteServiceException;
import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.common.feign.dto.PageDTO;
import com.liyz.cloud.service.staff.constants.StaffConstants;
import com.liyz.cloud.service.staff.dto.log.StaffLogPageDTO;
import com.liyz.cloud.service.staff.feign.StaffInfoFeignService;
import com.liyz.cloud.service.staff.model.StaffInfoDO;
import com.liyz.cloud.service.staff.model.StaffLoginLogDO;
import com.liyz.cloud.service.staff.model.StaffLogoutLogDO;
import com.liyz.cloud.service.staff.service.StaffInfoService;
import com.liyz.cloud.service.staff.service.StaffLoginLogService;
import com.liyz.cloud.service.staff.service.StaffLogoutLogService;
import com.liyz.cloud.service.staff.vo.info.StaffInfoVO;
import com.liyz.cloud.service.staff.vo.log.StaffLoginLogVO;
import com.liyz.cloud.service.staff.vo.log.StaffLogoutLogVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/4/30 13:37
 */
@Tag(name = "客户信息")
@RestController
@RequestMapping(StaffConstants.STAFF_INFO_URL)
public class StaffInfoController implements StaffInfoFeignService {

    @Resource
    private StaffInfoService staffInfoService;
    @Resource
    private StaffLoginLogService staffLoginLogService;
    @Resource
    private StaffLogoutLogService staffLogoutLogService;


    @Override
    public StaffInfoVO getByStaffId(Long staffId) {
        StaffInfoDO staffInfoDO = staffInfoService.getById(staffId);
        if (Objects.isNull(staffInfoDO)) {
            throw new RemoteServiceException(CommonExceptionCodeEnum.DATA_NOT_EXIST);
        }
        return BeanUtil.copyProperties(staffInfoDO, StaffInfoVO::new);
    }

    @Override
    public RemotePage<StaffInfoVO> page(PageDTO pageDTO) {
        Page<StaffInfoDO> page = staffInfoService.page(Page.of(pageDTO.getPageNum(), pageDTO.getPageSize()));
        return RemotePage.of(
                BeanUtil.copyList(page.getRecords(), StaffInfoVO::new),
                page.getTotal(),
                pageDTO.getPageNum(),
                pageDTO.getPageSize()
        );
    }

    @Override
    public RemotePage<StaffLoginLogVO> loginPage(StaffLogPageDTO pageDTO) {
        Page<StaffLoginLogDO> page = staffLoginLogService.page(Page.of(pageDTO.getPageNum(), pageDTO.getPageSize()),
                Wrappers.lambdaQuery(StaffLoginLogDO.builder().staffId(pageDTO.getStaffId()).build())
                        .orderByDesc(StaffLoginLogDO::getId));
        return RemotePage.of(
                BeanUtil.copyList(page.getRecords(), StaffLoginLogVO::new),
                page.getTotal(),
                pageDTO.getPageNum(),
                pageDTO.getPageSize()
        );
    }

    @Override
    public RemotePage<StaffLogoutLogVO> logoutPage(StaffLogPageDTO pageDTO) {
        Page<StaffLogoutLogDO> page = staffLogoutLogService.page(Page.of(pageDTO.getPageNum(), pageDTO.getPageSize()),
                Wrappers.lambdaQuery(StaffLogoutLogDO.builder().staffId(pageDTO.getStaffId()).build())
                        .orderByDesc(StaffLogoutLogDO::getId));
        return RemotePage.of(
                BeanUtil.copyList(page.getRecords(), StaffLogoutLogVO::new),
                page.getTotal(),
                pageDTO.getPageNum(),
                pageDTO.getPageSize()
        );
    }
}
