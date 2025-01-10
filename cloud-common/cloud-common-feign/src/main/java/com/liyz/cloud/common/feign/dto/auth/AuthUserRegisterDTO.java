package com.liyz.cloud.common.feign.dto.auth;

import com.liyz.cloud.common.feign.validation.AnyNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/6/14 9:50
 */
@Getter
@Setter
public class AuthUserRegisterDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户手机号码")
    @AnyNotBlank(groups = {Register.class}, fields = {"mobile", "email"}, message = "请输入注册的手机号码或邮箱地址")
    private String mobile;

    @Schema(description = "用户邮箱地址")
    @AnyNotBlank(groups = {Register.class}, fields = {"mobile", "email"}, message = "请输入注册的手机号码或邮箱地址")
    private String email;

    @Schema(description = "密码")
    @NotBlank(groups = {Register.class}, message = "请输入8到20位数字和字母组合")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$", groups = {Register.class}, message = "请输入8到20位数字和字母组合")
    private String password;

    @Schema(description = "加密盐")
    private String salt;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户真实名称")
    private String realName;

    @Schema(description = "客户端ID")
    private String clientId;

    public interface Register{}
}
