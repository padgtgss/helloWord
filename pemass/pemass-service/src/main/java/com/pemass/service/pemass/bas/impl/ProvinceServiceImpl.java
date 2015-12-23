package com.pemass.service.pemass.bas.impl;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.bas.City;
import com.pemass.persist.domain.jpa.bas.District;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.service.pemass.bas.ProvinceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: ProvinceServiceImpl
 * Author: estn.zuo
 * CreateTime: 2014-10-10 17:57
 */
@Service
public class ProvinceServiceImpl implements ProvinceService {

    @Resource
    private BaseDao jpaBaseDao;


    @Override
    public Province getProvinceByName(String provinceName) {
        return jpaBaseDao.getEntityByField(Province.class, "provinceName", provinceName);
    }

    @Override
    public List<Province> getProvinceList() {
        return jpaBaseDao.getAllEntities(Province.class);
    }


    @Override
    public List<City> getCityListByProcince(Long provinceId) {
        return jpaBaseDao.getEntitiesByField(City.class, "province.id", provinceId);
    }


    @Override
    public List<District> getDistrictListByCity(Long cityId) {
        return jpaBaseDao.getEntitiesByField(District.class, "city.id", cityId);
    }

    @Override
    public List<District> getDistrictList() {
        return jpaBaseDao.getAllEntities(District.class);
    }

    @Override
    public Province getProvinceByID(Long id) {
        return jpaBaseDao.getEntityByField(Province.class, "id", id);
    }

    @Override
    public City getCityById(Long id) {
        return jpaBaseDao.getEntityById(City.class, id);
    }

    @Override
    public District getDistrictById(Long id) {
        return jpaBaseDao.getEntityById(District.class, id);
    }

    @Override
    public List<District> selectDistrictByIds(Long[] districtId) {

        List<District> siteList = new ArrayList<District>();
        for (int i = 0; i < districtId.length; i++) {
            District district = jpaBaseDao.getEntityById(District.class, districtId[i]);
            if (district != null) {
                siteList.add(district);
            }
        }
        return siteList;
    }

}
