/*
 * 文件名：SecurityUtil.java
 * 版权：
 * 描述：
 * 修改人：estn.zuo
 * 修改时间：2013-7-24 上午12:51:48
 */
package com.pemass.cloudmoney.api.common.security;

import com.pemass.persist.domain.redis.Token;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @description SecurityUtil 工具类
 * @author estn.zuo
 * @date 2013-7-24
 */
public class SecurityUtil {

	public static Token getCurrentToken(){
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(obj instanceof Token){
			return (Token) obj;
		}
		return null;
	}
}
