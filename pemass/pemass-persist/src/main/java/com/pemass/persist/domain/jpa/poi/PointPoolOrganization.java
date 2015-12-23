/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: 积分池和组织机构关系表
 * @Author: estn.zuo
 * @CreateTime: 2015-07-13 14:16
 */
@Entity
@Table(name = "poi_point_pool_organization")
public class PointPoolOrganization extends BaseDomain {

    @Column(name = "point_pool_id")
    private Long pointPoolId;   //积分池ID

    @Column(name = "organization_id")
    private Long organizationId;    //组织机构ID

    // ============= getter and setter ============= \\

    public Long getPointPoolId() {
        return pointPoolId;
    }

    public void setPointPoolId(Long pointPoolId) {
        this.pointPoolId = pointPoolId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
