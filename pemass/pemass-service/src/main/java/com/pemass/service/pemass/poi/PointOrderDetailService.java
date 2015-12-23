/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

import com.pemass.persist.domain.jpa.poi.UserConsumeDetail;

import java.util.List;
import java.util.Map;

/**
 * @Description: PointOrderDetailService
 * @Author: zhou hang
 * @CreateTime: 2014-11-27 14:14
 */
public interface PointOrderDetailService {
    /**
     * 查询积分消耗明细
     * @param param
     * @return
     */
  List<UserConsumeDetail> getPointConsumeDetailList(Map<String, Object> param);
}
