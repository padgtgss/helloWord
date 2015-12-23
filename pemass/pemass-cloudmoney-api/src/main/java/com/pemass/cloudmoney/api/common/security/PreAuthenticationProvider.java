/*
 * 文件名：PreAuthenticationProvider.java
 * 版权：
 * 描述：
 * 修改人：estn.zuo
 * 修改时间：2013-7-11 下午11:44:51
 */
package com.pemass.cloudmoney.api.common.security;

import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.persist.domain.redis.Token;
import com.pemass.service.exception.SignError;
import com.pemass.service.pemass.sys.TokenService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author estn.zuo
 * @description 根据过滤器链中的Authentication，从中取出token并且认证该token
 * @date 2013-7-11
 */
@Component("preAuthenticationProvider")
public class PreAuthenticationProvider implements AuthenticationProvider {

    private Log logger = LogFactory.getLog(getClass());

    @Resource
    private TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication ---> start");
        }

        String tokenValue = (String) authentication.getCredentials();

        /** 判断客户端是否上传token */
        if (tokenValue == null) {
            throw new BaseBusinessException(SignError.TOKEN_NOT_FOUND);
        }

        Token token = tokenService.retrieveToken(tokenValue);
        /** 判断token是否为空,空表示该token无效 */
        if (token == null) {
            throw new BaseBusinessException(SignError.TOKEN_ERROR);
        }

        /** 验证通过后生成Token */
        PreAuthenticatedAuthenticationToken preToken = generate(token);

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication ---> end");
        }

        return preToken;
    }

    /**
     * 生成AuthenticationToken
     *
     * @param token token对象
     * @return PreAuthenticatedAuthenticationToken对象
     */
    private PreAuthenticatedAuthenticationToken generate(Token token) {

        PreAuthenticatedAuthenticationToken preAuthenticationToken = new PreAuthenticatedAuthenticationToken(token,
                token.getToken(), this.getAuthorities(token.getAuthority()));

        return preAuthenticationToken;
    }

    /**
     * 将数据库的字符串权限转换为GrantedAuthority集合
     *
     * @param authoritys
     * @return
     */
    private Collection<? extends GrantedAuthority> getAuthorities(String authoritys) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        String strArray[] = authoritys.split(SystemConst.SEPARATOR_SYMBOL);
        for (String s : strArray) {
            grantedAuthorities.add(new SimpleGrantedAuthority(s));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}
