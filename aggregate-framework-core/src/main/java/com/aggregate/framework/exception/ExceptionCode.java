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
     * 服务调用异常
     */
    public static final CodeMessage CHANNEL_FAIL = new CodeMessage(100000, "服务方法不存在");
    public static final CodeMessage SERVER_EXCEPTION = new CodeMessage(100001, "服务方法执行失败");



    /**
     * 签名校验
     */
    public static final CodeMessage SECURITY_FAIL = new CodeMessage(11000, "安全验证：未知错误");
    public static final CodeMessage SECURITY_SIGN = new CodeMessage(11001, "安全验证：sign签名不匹配");


}