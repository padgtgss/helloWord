/*
 * 文件名：PaginationInterceptor.java
 * 版权：Copyright (c) 2014, doit.com All Rights Reserved. 
 * 描述：TODO
 * 修改人：estn.zuo
 * 修改时间：2014-2-28 下午1:45:37
 */
package com.pemass.cloudmoney.api.common.interceptor;

import com.pemass.common.core.annotation.Auth;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.redis.Token;
import com.pemass.service.exception.SignError;
import com.pemass.service.pemass.sys.TokenService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UriTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author estn.zuo
 * @description 签名拦截器
 * @date 2014-2-28
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private Log logger = LogFactory.getLog(getClass());

    @Resource
    private TokenService tokenService;

    @Resource
    private BaseDao jpaBaseDao;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (logger.isDebugEnabled()) {
            logger.debug("come in AuthInterceptor");
        }

        /**1. 判断是否有token，如果没有直接过 */
        String tokenString = request.getParameter("token");
        if (StringUtils.isBlank(tokenString)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Without Token Go");
            }
            return true;
        }

        /**2. 判断方法是否有Auth注解，如果没有直接过 */
        Method method = ((HandlerMethod) handler).getMethod();
        Auth auth = method.getAnnotation(Auth.class);
        if (auth == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Without Auth Go");
            }
            return true;
        }

        /**3. 获取Auth注解对应参数的值*/
        Class entityName = auth.entity();
        String parameterName = auth.parameter();
        String parameterValue = this.doGetParameterValue(request, (HandlerMethod) handler, parameterName);
        String fieldName = auth.fieldName();
        if (StringUtils.isBlank(fieldName)) {
            fieldName = "userId";
        }
        /**4. 从实体中获取userId,如果userId = 0 表示数据库没有查询到数据，直接返回*/
        Long userIdFromEntity = this.doGetUserIdFromEntity(entityName, parameterValue, fieldName);
        if (new Long(0).equals(userIdFromEntity)) {
            return true;
        }

        /**5. 从Token中获取userId*/
        Long userIdFromToken = this.doGetUserIdFromToken(tokenString);

        /**6. 对比*/
        if (userIdFromToken.equals(userIdFromEntity)) {
            return true;
        }
        throw new BaseBusinessException(SignError.TOKEN_IS_NOT_ALLOW);
    }

    private Long doGetUserIdFromToken(String tokenString) {
        Token token = tokenService.retrieveToken(tokenString);
        return token.getTargetId();
    }

    private Long doGetUserIdFromEntity(Class entityName, Object parameterValue, String fieldName) throws Exception {

        Class<BaseDomain> clazz = (Class<BaseDomain>) entityName;

        BaseDomain entity = null;
        if (clazz.equals(User.class)) {
            if (StringUtils.isNotBlank(fieldName) && "id".equals(fieldName)){
                return new Long(parameterValue.toString());
            }
            List<BaseDomain> domains = jpaBaseDao.getEntitiesByField(clazz, fieldName, parameterValue);
            if (domains == null || domains.size() == 0) {
                return 0L;
            }
            entity = domains.get(0);
            Method m1 = entity.getClass().getSuperclass().getDeclaredMethod("getId");
            return (Long) m1.invoke(entity);
        } else {
            List<BaseDomain> domains = jpaBaseDao.getEntitiesByField(clazz, fieldName,new Long(parameterValue.toString()) );
            if (domains == null || domains.size() == 0) {
                return 0L;
            }
            entity = domains.get(0);
        }

        Method m1 = entity.getClass().getDeclaredMethod("getUserId");
        return (Long) m1.invoke(entity);
    }

    private String doGetParameterValue(HttpServletRequest request, HandlerMethod handler, String parameterName) {
        String parameterValue = request.getParameter(parameterName);
        if (StringUtils.isBlank(parameterValue)) {
            RequestMapping clazzRequestMapping = handler.getBeanType().getAnnotation(RequestMapping.class);
            String clazzRequestMappingString = clazzRequestMapping.value()[0];

            RequestMapping methodRequestMapping = handler.getMethod().getAnnotation(RequestMapping.class);
            String methodRequestMappingString = methodRequestMapping.value()[0];

            UriTemplate template = new UriTemplate(clazzRequestMappingString + methodRequestMappingString);
            Map<String, String> parameters = template.match(request.getRequestURI());
            parameterValue = parameters.get(parameterName);
        }
        return parameterValue;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}
