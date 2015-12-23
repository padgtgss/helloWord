package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: OrganizationCategory
 * @Author: xy
 * @CreateTime: 2015-09-15 10:39
 */
@Entity
@Table(name = "sys_organization_category")
public class OrganizationCategory extends BaseDomain {

    @Column(name = "category_name", length = 50)
    private String categoryName;//商户类别名称

    @Column(name = "category_code")
    private String categoryCode;//商户类别编码

    @Column(name = "state")
    private Integer state;//状态

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
