/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.bas;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.bas.City;

/**
 * @Description: ResourcesDao
 * @Author: estn.zuo
 * @CreateTime: 2014-11-03 16:11
 */
public interface LocationDao extends BaseDao {


    City searchCityByName(String cityName);
}
