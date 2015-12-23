/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.mongo.log;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.Map;

/**
 * @Description: LogMessage
 * @Author: estn.zuo
 * @CreateTime: 2014-12-11 18:27
 */
@Entity("log_message")
public class LogMessage implements Serializable {

    @Id
    private ObjectId id;

    //用户ID
    private Long uid;

    //context路径
    private String contextPath;

    //请求API地址
    private String pathInfo;

    //请求参数
    private Map parameterMap;

    private String token;

    //开始时间
    private long startTime;

    //结束时间
    private long endTime;

    //耗时
    private long elapse;

    //用户IP
    private String remoteAddr;

    //是否是非法访问【0-正常访问，1-非法访问】
    private String isIllegalAccess;

    public LogMessage() {
    }

    public LogMessage(ObjectId id, Long uid, String contextPath, String pathInfo, Map parameterMap, String token, long startTime, long endTime, long elapse, String remoteAddr, String isIllegalAccess) {
        this.id = id;
        this.uid = uid;
        this.contextPath = contextPath;
        this.pathInfo = pathInfo;
        this.parameterMap = parameterMap;
        this.token = token;
        this.startTime = startTime;
        this.endTime = endTime;
        this.elapse = elapse;
        this.remoteAddr = remoteAddr;
        this.isIllegalAccess = isIllegalAccess;
    }



    public String getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    public Map getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map parameterMap) {
        this.parameterMap = parameterMap;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getElapse() {
        return elapse;
    }

    public void setElapse(long elapse) {
        this.elapse = elapse;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getIsIllegalAccess() {
        return isIllegalAccess;
    }

    public void setIsIllegalAccess(String isIllegalAccess) {
        this.isIllegalAccess = isIllegalAccess;
    }

    @Override
    public String toString() {
        return "LogMessage{" +
                "id=" + id +
                ", uid=" + uid +
                ", contextPath='" + contextPath + '\'' +
                ", pathInfo='" + pathInfo + '\'' +
                ", parameterMap=" + parameterMap +
                ", token='" + token + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", elapse=" + elapse +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", isIllegalAccess='" + isIllegalAccess + '\'' +
                '}';
    }
}
