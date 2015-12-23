/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.vo.SettlementStatisticsVO;

import java.util.Map;

/**
 * @Description: SupplierGatherService
 * @Author: vahoa.ma
 * @CreateTime: 2015-09-14 14:01
 */
public interface ClearingGatherService {

    /**
     * 分页查询清分信息
     *
     * @param fieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<SettlementStatisticsVO> statistics(Map<String, Object> fieldNameValueMap, long pageIndex, long pageSize);

}