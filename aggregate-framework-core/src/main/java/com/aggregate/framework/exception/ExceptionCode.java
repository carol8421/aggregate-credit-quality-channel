package com.aggregate.framework.exception;

import com.aggregate.framework.entity.CodeMessage;

/**
 * 应用异常编码
 *
 * @author
 * @since
 */
public class ExceptionCode {

    /**
     * 服务器异常
     */
    public static final CodeMessage SERVICE_BUSY = new CodeMessage(000000, "服务器繁忙，请稍后重试");

    /**
     * 系统异常
     */
    public static final CodeMessage LOW_VERSION = new CodeMessage(010000, "版本过低，当前功能不支持");

    /**
     * 接口调用异常
     */
    public static final CodeMessage INTERFACE_USE_FAILURE = new CodeMessage(100000, "接口调用失败");

    public static final CodeMessage METHOD_FAILURE = new CodeMessage(100001, "执行操作失败");


    /**
     * 基础校验--参数错误
     */
    public static final CodeMessage PARAM_IS_ILLEGAL = new CodeMessage(101001, "参数非法");
    public static final CodeMessage PARAM_CODE_EMPTY = new CodeMessage(101002, "code参数为空");


    /**
     * 签名校验
     */
    public static final CodeMessage SECURITY_UNKONW = new CodeMessage(11000, "安全验证：未知错误");
    public static final CodeMessage SECURITY_SIGN = new CodeMessage(11001, "安全验证：sign签名不匹配");


}