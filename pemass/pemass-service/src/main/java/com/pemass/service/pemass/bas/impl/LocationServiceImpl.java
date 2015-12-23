/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas.impl;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.dao.bas.LocationDao;
import com.pemass.persist.domain.jpa.bas.City;
import com.pemass.persist.domain.jpa.bas.District;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.service.pemass.bas.LocationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: LocationServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-10-27 14:14
 */
@Service
public class LocationServiceImpl implements LocationService {

    @Resource
    private BaseDao jpaBaseDao;


    @Resource
    private LocationDao locationDao;

    @Override
    public List<Province> selectAllProvince() {
        return jpaBaseDao.getAllEntities(Province.class);
    }

    @Override
    public List<City> selectCityByProvince(Long provinceId) {
        return jpaBaseDao.getEntitiesByField(City.class, "province.id", provinceId);
    }

    @Override
    public List<District> selectDistrictByCity(Long cityId) {
        return jpaBaseDao.getEntitiesByField(District.class, "city.id", cityId);
    }


    @Override
    public City selectCityByCityName(String cityName) {
        return locationDao.searchCityByName(cityName);
    }
}
