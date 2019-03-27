package com.aggregate.framework.open.exception;

import com.aggregate.framework.entity.CodeMessage;

/**
 * 应用异常编码
 *
 * @author
 * @since
 */
public class ExceptionChannelCode {

    /**
     * 服务器异常
     */
    public static final CodeMessage SERVICE_BUSY = new CodeMessage(000000, "服务器繁忙，请稍后重试");

    /**
     * 该通道没有开启
     */
    public static final CodeMessage CHANNEL_NOT_OPEN = new CodeMessage(100001, "该通道没有开启");

    /**
     * 账户余额不足
     */
    public static final CodeMessage BALANCE_ENOUGH = new CodeMessage(100002, "账户余额不足");


}