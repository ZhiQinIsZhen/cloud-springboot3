package com.liyz.cloud.common.exception;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/11/13 10:36
 */
public enum CommonExceptionCodeEnum implements IExceptionService{
    SUCCESS("0", "成功"),
    FAIL("1", "失败"),

    FORBIDDEN("401", "登录后进行操作"),
    NO_RIGHT("403", "暂无权限"),
    NOT_FOUND("404", "NOT_FOUND"),

    PARAMS_VALIDATED("10000", "参数校验失败"),
    DATA_NOT_EXIST("10001", "数据不存在"),
    REMOTE_SERVICE_FAIL("10005", "服务异常"),
    OUT_LIMIT_COUNT("10006", "访问频率过快"),
    NON_NO_ARGS_CONSTRUCTOR("10007", "没有无参构造方法"),
    NOT_FIND_CLASS("10008", "找不到指定的class"),
    PARSING_PROPERTY_NAME_ERROR("10009", "解析属性名称错误"),
    REFLECTOR_FAIL("10010", "反射失败"),
    THREAD_INTERRUPTED("10011", "线程中断执行"),
    EXECUTION_EXCEPTION("10012", "线程执行异常"),

    DEC_KEY_LENGTH_ERROR("13001", "DEC加密Key长度不能小于8位"),
    DEC_IV_LENGTH_ERROR("13002", "DEC加密IV长度必须等于8位"),
    AEC_KEY_LENGTH_ERROR("13003", "AEC加密Key长度必须为16位或24位或32位"),
    AEC_IV_LENGTH_ERROR("13004", "AEC加密IV长度必须等于16位"),

    LOGIN_FAIL("20001", "用户名或者密码错误"),
    AUTHORIZATION_FAIL("20002", "认证失败"),
    AUTHORIZATION_TIMEOUT("20003", "认证过期"),
    REGISTRY_ERROR("20004", "注册错误"),
    LACK_SOURCE_ID("20005", "注册错误: 缺少资源客户端ID"),
    NON_SET_SOURCE_ID("20006", "注册错误: 资源服务未配置该资源客户端ID"),
    LOGIN_ERROR("20007", "登录错误"),
    OTHERS_LOGIN("20008", "该账号已在其他地方登录"),
    MOBILE_EXIST("20009", "该手机号码已注册"),
    EMAIL_EXIST("20010", "该邮箱地址已注册"),
    USER_NOT_EXIST("20011", "当前用户不存在"),
    ;

    private final String code;

    private final String message;
    ;

    CommonExceptionCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
