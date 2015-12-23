/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.enumeration.PointChannelEnum;

import java.util.List;

/**
 * @Description: OnePoinOrdertDao
 * @Author: pokl.huang
 * @CreateTime: 2015-06-16 10:15
 */
public interface OnePoinOrdertDao extends BaseDao {

    /**
     * 获取用户 一元购积分
     * @param uid
     * @return
     */
    Long getPointO(Long uid, List<PointChannelEnum> pointChannelEnums);
}
