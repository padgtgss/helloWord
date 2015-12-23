package com.pemass.persist.domain.jpa.bas;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Description: Province
 * Author: estn.zuo
 * CreateTime: 2014-10-10 17:27
 */
@Entity
@Table(name = "bas_province")
public class Province extends BaseDomain {

    @Column(name = "province_name", length = 50,nullable = false)
    private String provinceName;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
