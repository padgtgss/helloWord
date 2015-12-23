/*
 * 文件名：PaginationInterceptor.java
 * 版权：Copyright (c) 2014, doit.com All Rights Reserved. 
 * 描述：TODO
 * 修改人：estn.zuo
 * 修改时间：2014-2-28 下午1:45:37
 */
package com.pemass.cloudpos.api.common.interceptor;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.service.exception.SignError;
import com.pemass.service.pemass.sys.ServiceControllService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author estn.zuo
 * @description 签名拦截器
 * @date 2014-2-28
 */
public class AppAuthInterceptor extends HandlerInterceptorAdapter {

    private Log logger = LogFactory.getLog(getClass());

    @Resource
    private ServiceControllService serviceControllService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (logger.isDebugEnabled()) {
            logger.debug("come in AppAuthInterceptor");
        }

        /**1. 获取 请求方式/接口地址  Eg：GET/ticcket/search  **/

        Method method = ((HandlerMethod) handler).getMethod();
        RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        String methodString = methodRequestMapping.method()[0].toString();
        String methodRequestMappingString = methodRequestMapping.value().length >0?methodRequestMapping.value()[0]:"";

        RequestMapping clazzRequestMapping = ((HandlerMethod) handler).getBeanType().getAnnotation(RequestMapping.class);
        String clazzRequestMappingString = clazzRequestMapping.value()[0];

        String appid = request.getParameter("appid");
        String url = "B/";

        if (!clazzRequestMappingString.contains("/")) {
            clazzRequestMappingString = "/" + clazzRequestMappingString;
        }

        if (!methodRequestMappingString.contains("/") && !"".equals(methodRequestMappingString)) {
            methodRequestMappingString = "/" + methodRequestMappingString;
        }

        url += methodString + clazzRequestMappingString + methodRequestMappingString;
        if (!serviceControllService.hasAuth(appid, url)) {
            throw new BaseBusinessException(SignError.TOKEN_IS_NOT_ALLOW);
        }
        return true;
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
