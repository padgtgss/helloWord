package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.common.server.enumeration.GenderEnum;
import com.pemass.persist.enumeration.AccountStatusEnum;
import com.pemass.persist.enumeration.AppTypeEnum;
import com.pemass.persist.enumeration.FeedbackTypeEnum;
import com.pemass.persist.enumeration.PositionRoleEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Description: 接口管理
 * Author: z
 * CreateTime: 2014-10-11 10:03
 */
@Entity
@Table(name = "sys_service_controll")
public class ServiceControll extends BaseDomain {

    @Column(name = "servicename", length = 50, nullable = false)
    private String servicename;   //接口名称

    @Column(name = "url", length = 100, nullable = false)
    private String url;     //接口URL     Eg:GET/ticket/search

    @Column(name = "app_type")
    @Enumerated(EnumType.STRING)
    private AppTypeEnum appType;   //客户端类型



    //=================== getter and setter =========


    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AppTypeEnum getAppType() {
        return appType;
    }

    public void setAppType(AppTypeEnum appType) {
        this.appType = appType;
    }


    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.getServicename()+"|")
                .append(this.getUrl()+"|")
                .append(this.getAppType().toString());
        return sb.toString();
    }
}
