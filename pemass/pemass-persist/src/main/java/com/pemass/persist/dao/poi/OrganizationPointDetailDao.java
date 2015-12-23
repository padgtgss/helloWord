package com.pemass.persist.dao.poi;/**
 * Created by Administrator on 2015/7/21.
 */

import com.pemass.common.server.dao.BaseDao;

import java.util.Map;

/**
 * @Description: OrganizationPointDetailDao
 * @Author: cassiel.liu
 * @CreateTime: 2015-07-21 15:37
 */
public interface OrganizationPointDetailDao extends BaseDao {

    /**
     * 根据条件获取可用积分数量
     *
     * @param fieldNameValueMap
     * @return
     */
    Long getSumUseableAmount(Map<String, Object> fieldNameValueMap);

    Integer getAmountByPointPoolId(Long pointPoolId);


}
