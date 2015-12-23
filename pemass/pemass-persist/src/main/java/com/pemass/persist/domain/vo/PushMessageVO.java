/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.vo;

import com.pemass.persist.enumeration.PushMessageTypeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 消息推送VO
 * @Author: estn.zuo
 * @CreateTime: 2015-08-19 16:51
 */
public class PushMessageVO implements Serializable {

    private String audience;    //听众

    private PushMessageTypeEnum pushMessageType;    //消息推送类型

    private List<Object> param;  //模板参数

    public PushMessageVO() {
    }

    public PushMessageVO(String audience, PushMessageTypeEnum pushMessageType, List<Object> param) {
        this.audience = audience;
        this.pushMessageType = pushMessageType;
        this.param = param;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public PushMessageTypeEnum getPushMessageType() {
        return pushMessageType;
    }

    public void setPushMessageType(PushMessageTypeEnum pushMessageType) {
        this.pushMessageType = pushMessageType;
    }

    public List<Object> getParam() {
        return param;
    }

    public void setParam(List<Object> param) {
        this.param = param;
    }




}
