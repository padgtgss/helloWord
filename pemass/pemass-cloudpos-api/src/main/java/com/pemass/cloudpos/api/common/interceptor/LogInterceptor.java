/*
 * 文件名：PaginationInterceptor.java
 * 版权：Copyright (c) 2014, doit.com All Rights Reserved. 
 * 描述：TODO
 * 修改人：estn.zuo
 * 修改时间：2014-2-28 下午1:45:37
 */
package com.pemass.cloudpos.api.common.interceptor;

import com.pemass.common.core.constant.SystemConst;
import com.pemass.persist.domain.mongo.log.LogMessage;
import com.pemass.service.jms.Producer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author estn.zuo
 * @description 日志拦截器
 * @date 2014-2-28
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    private ThreadLocal<LogMessage> logMessageThreadLocal = new ThreadLocal<LogMessage>();

    @Resource
    private Producer logProducer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LogMessage logMessage = new LogMessage();
        logMessage.setStartTime(System.currentTimeMillis());
        logMessage.setParameterMap(request.getParameterMap());
        logMessage.setToken(this.doFetchToken(request));
        logMessage.setPathInfo(request.getPathInfo());
        logMessage.setContextPath(request.getContextPath());
        logMessage.setRemoteAddr(request.getRemoteAddr());
        logMessage.setIsIllegalAccess("0");
        logMessageThreadLocal.set(logMessage);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LogMessage logMessage = logMessageThreadLocal.get();
        logMessage.setEndTime(System.currentTimeMillis());
        logMessage.setElapse(System.currentTimeMillis() - logMessage.getStartTime());

        //send to jms
        logProducer.send(logMessage);
        //**remove threadlocal
        logMessageThreadLocal.remove();
    }

    private String doFetchToken(HttpServletRequest request) {
        String token = request.getParameter(SystemConst.TOKEN_KEY);
        if (token == null) {
            token = request.getHeader(SystemConst.TOKEN_KEY);
        }
        return token;
    }

}
