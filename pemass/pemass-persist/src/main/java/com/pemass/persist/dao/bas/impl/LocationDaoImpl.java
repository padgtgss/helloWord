/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.bas.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.bas.LocationDao;
import com.pemass.persist.domain.jpa.bas.City;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * @Description: LocationDaoImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-07-10 17:32
 */
@Repository
public class LocationDaoImpl extends JPABaseDaoImpl implements LocationDao {
    @Override
    public City searchCityByName(String cityName) {
        String sql = "select  * from bas_city c where c.available = 1 and c.city_name like '%"+cityName+"%'";
        Query query = em.createNativeQuery(sql,City.class);
        List result = query.getResultList();

        return (City)result.get(0);
    }
}

