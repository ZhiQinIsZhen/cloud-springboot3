package com.liyz.cloud.api.staff.dto.staff;

import com.liyz.cloud.common.feign.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/3/11 10:29
 */
@Getter
@Setter
public class StaffLogPageDTO extends PageDTO {
    @Serial
    private static final long serialVersionUID = -4827292438113353379L;

    @Schema(description = "员工ID")
    private Long staffId;
}
