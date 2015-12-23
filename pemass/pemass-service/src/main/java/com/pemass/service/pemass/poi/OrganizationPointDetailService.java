/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.poi.OrganizationPointDetail;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.User;

import java.util.Map;

/**
 * @Description: 商户积分明细
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 11:09
 */
public interface OrganizationPointDetailService {

    /**
     * 新增记录
     *
     * @param organizationPointDetail 商户积分明细对象
     * @return
     */
    OrganizationPointDetail insert(OrganizationPointDetail organizationPointDetail);

    /**
     * 新增记录
     *
     * @param pointPurchase 商户积分认购对象
     * @return
     */
    OrganizationPointDetail insertByPurchase(PointPurchase pointPurchase);

    /**
     * 根据ID获取OrganizationPointDetail
     *
     * @param id id
     * @return 返回的值
     */
    OrganizationPointDetail selectById(Long id);

    /**
     * 获取可用的积分
     *
     * @param fieldNameValueMap
     * @return
     */
    Long getUseableAmount(Map<String, Object> fieldNameValueMap);

    /**
     * 根据积分池id获取该批次积分在商户积分明细中的数量
     * @param pointPoolId 积分池id
     * @return
     */
    Integer getPointAmountByPointPoolId(Long pointPoolId);


    void updateOrganizationPointP(Organization organization, OrderItem orderItem, User purchaseUser);


    void updateOrganizationPointE(Organization organization, OrderItem orderItem, User purchaseUser);

    /**
     * 根据积分批次号查询商户积分明细
     * @param pointPurchaseId
     * @return
     */
    OrganizationPointDetail selectByPurchaseId(Long pointPurchaseId);

}
