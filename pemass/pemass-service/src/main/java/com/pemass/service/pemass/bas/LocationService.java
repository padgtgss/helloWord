/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas;

import com.pemass.persist.domain.jpa.bas.City;
import com.pemass.persist.domain.jpa.bas.District;
import com.pemass.persist.domain.jpa.bas.Province;

import java.util.List;

/**
 * @Description: LocationService
 * @Author: estn.zuo
 * @CreateTime: 2014-10-27 14:12
 */
public interface LocationService {

    /**
     * 返回全部省份
     *
     * @return
     */
    List<Province> selectAllProvince();

    /**
     * 根据省份ID获取市
     *
     * @param provinceId
     * @return
     */
    List<City> selectCityByProvince(Long provinceId);

    /**
     * 根据市ID获取区
     *
     * @param cityId
     * @return
     */
    List<District> selectDistrictByCity(Long cityId);



    /**
     * 根据城市名 获取 城市
     *
     * @param cityName
     * @return
     */
    City selectCityByCityName(String cityName);
}
