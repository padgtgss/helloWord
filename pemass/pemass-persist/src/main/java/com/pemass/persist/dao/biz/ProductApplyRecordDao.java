package com.pemass.persist.dao.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;

import java.util.Map;

/**
 * @Description: ProductApplyRecordDao
 * @Author: luoc
 * @CreateTime: 2015-06-01 15:37
 */
public interface ProductApplyRecordDao extends BaseDao {

    /**
     * 获取某商户的申请调价纪录
     *
     * @param conditions
     * @param domainPage
     * @return
     */
    DomainPage selectApplyRecordByConditions(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 获取某商户的申请调价纪录
     *
     * @param organizationId
     * @return
     */
    DomainPage selectApplyRecordByConditions(Long organizationId,String productName,long pageIndex,long pageSize);

}
