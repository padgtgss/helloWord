package com.pemass.service.pemass.bas;

import com.pemass.persist.domain.jpa.bas.City;
import com.pemass.persist.domain.jpa.bas.District;
import com.pemass.persist.domain.jpa.bas.Province;

import java.util.List;

/**
 * Created by estn.zuo on 14-10-10.
 */
public interface ProvinceService {

    Province getProvinceByName(String provinceName);

    List<Province> getProvinceList();

    List<City> getCityListByProcince(Long provinceId);

    List<District> getDistrictListByCity(Long cityId);

    List<District> getDistrictList();

    Province getProvinceByID(Long id);

    City getCityById(Long id);

    District getDistrictById(Long id);

    List<District> selectDistrictByIds(Long[] districtId);
}
