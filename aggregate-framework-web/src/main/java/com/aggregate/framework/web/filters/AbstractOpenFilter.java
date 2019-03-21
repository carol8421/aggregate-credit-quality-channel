package com.aggregate.framework.web.filters;

import com.aggregate.framework.entity.CodeMessage;
import com.aggregate.framework.exception.BusinessException;
import com.aggregate.framework.utils.codec.Sm3Util;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;


@Slf4j
public abstract class AbstractOpenFilter{

	//错误异常码
	protected static final CodeMessage SIGN_FAIL = new CodeMessage(400, "请求参数验签失败,请检查参数");

	/**
	 * 请求包装类
	 */
	@Getter
	private ParameterRequestWrapper requestWrapper;

	/**
	 * 返回应用的appId
	 */
	@Getter
	private String appId;

	/**
	 * 根据appId查询key
	 * @param appId 应用的ID
	 * @return appKEY
	 */
	abstract String getAppKey(String appId);

	/**
	 * OPENAPI 接口验签
	 * @param request request对象
	 */
	public void parameterSing(HttpServletRequest request){
		String requestURI = request.getRequestURI();
		String[] uriArray = requestURI.split("/");
		//从请求路径中获取系统参数appId和sign值
		String sign = uriArray[uriArray.length - 1];
		appId = uriArray[uriArray.length - 2];
		log.debug("load appId:{},sign:{}", appId, sign);
		ServletInputStream inputStream = null;
		String requestParam = null;
		try {
			inputStream = request.getInputStream();
			requestParam = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		try {
			String param = JSON.parseObject(requestParam).getString("param");
			//customer user appId
			String key = getAppKey(appId);
			//生成16进制签名串
			String hmac = Hex.toHexString(Sm3Util.hmac(key.getBytes(), param.getBytes()));
			log.debug("request url:{}, param :{} ,new sign :{}", requestURI, param, hmac);
			if (!sign.equals(hmac)) {
				throw new BusinessException(SIGN_FAIL);
			}
			//reconstruct request, make request can route into controller
			this.requestWrapper = new ParameterRequestWrapper(request, param);
		}catch (Exception e){
			log.error(e.getMessage(),e);
			throw new BusinessException(SIGN_FAIL);
		}
	}

}
