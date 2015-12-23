/*
 * 文件名：PaginationInterceptor.java
 * 版权：Copyright (c) 2014, doit.com All Rights Reserved. 
 * 描述：TODO
 * 修改人：estn.zuo
 * 修改时间：2014-2-28 下午1:45:37
 */
package com.pemass.cloudpos.api.common.interceptor;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.core.util.SignatureUtil;
import com.pemass.persist.domain.redis.App;
import com.pemass.service.exception.SignError;
import com.pemass.service.pemass.bas.StampService;
import com.pemass.service.pemass.sys.AppService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author estn.zuo
 * @description 签名拦截器
 * @date 2014-2-28
 */
public class SignatureInterceptor extends HandlerInterceptorAdapter {

    private Log logger = LogFactory.getLog(getClass());

    @Resource
    private AppService appService;

    @Resource
    private StampService stampService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //验证签名
        String signStr = this.doAssembleSignString(request);
        if (logger.isDebugEnabled()) {
            logger.debug("signStr:" + signStr);
        }

        String sign = request.getParameter("sign");
        boolean result = MD5Util.verify(signStr, null, sign);

        if (logger.isDebugEnabled()) {
            logger.debug("sign result:" + result);
        }

        if (!result) {
            throw new BaseBusinessException(SignError.SIGNATURE_ERROR);
        }

        //replay
        String stamp = request.getParameter("stamp");
        stampService.verifyApiStamp(stamp);
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

    /**
     * 拼接签名字符串
     *
     * @param request
     * @return
     */
    private String doAssembleSignString(HttpServletRequest request) {
        Map<String, Object> param = request.getParameterMap();

        //获取待签名的字符串
        String newSignString = SignatureUtil.createLinkString(SignatureUtil.parameterFilter(param));

        String appid = request.getParameter("appid");
        //从redis中获取用户Secret
        App app = appService.getAppById(appid);
        if (app == null) {
            throw new BaseBusinessException(SignError.SIGNATURE_ERROR);
        }

        newSignString = newSignString + "&" + app.getKey();

        return newSignString;
    }

}
