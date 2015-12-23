package com.pemass.persist.domain.mongo.log;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: QuartzLog
 * @Author: xy
 * @CreateTime: 2015-08-02 01:16
 */
@Entity("quartz_log")
public class QuartzLogMessage implements Serializable {

    @Id
    private ObjectId id;

    private Boolean isSucceed;  //是否成功

    private Date createTime;//创建时间

    private String exceptionInfo;//异常信息

    private String triggerKey;//触发器名称

    private Date fireTime;//执行时间

    private String jobBean;//执行任务的jobBean

    private long elapse;    //耗时

    public QuartzLogMessage() {
    }

    public QuartzLogMessage(ObjectId id, Boolean isSucceed, Date createTime, String exceptionInfo, String triggerKey, Date fireTime, String jobBean, long elapse) {
        this.id = id;
        this.isSucceed = isSucceed;
        this.createTime = createTime;
        this.exceptionInfo = exceptionInfo;
        this.triggerKey = triggerKey;
        this.fireTime = fireTime;
        this.jobBean = jobBean;
        this.elapse = elapse;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public String getTriggerKey() {
        return triggerKey;
    }

    public void setTriggerKey(String triggerKey) {
        this.triggerKey = triggerKey;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public String getJobBean() {
        return jobBean;
    }

    public void setJobBean(String jobBean) {
        this.jobBean = jobBean;
    }

    public long getElapse() {
        return elapse;
    }

    public void setElapse(long elapse) {
        this.elapse = elapse;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(Boolean isSucceed) {
        this.isSucceed = isSucceed;
    }

    @Override
    public String toString() {
        return "QuartzLogMessage{" +
                "id=" + id +
                ", isSucceed=" + isSucceed +
                ", createTime=" + createTime +
                ", exceptionInfo='" + exceptionInfo + '\'' +
                ", triggerKey='" + triggerKey + '\'' +
                ", fireTime=" + fireTime +
                ", jobBean='" + jobBean + '\'' +
                ", elapse=" + elapse +
                '}';
    }
}
