/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.bas;

import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.bas.City;
import com.pemass.persist.domain.jpa.bas.District;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.pojo.bas.CityPojo;
import com.pemass.pojo.bas.DistrictPojo;
import com.pemass.pojo.bas.ProvincePojo;
import com.pemass.service.pemass.bas.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: DemoController
 * @Author: estn.zuo
 * @CreateTime: 2015-04-09 15:32
 */

@Controller
@RequestMapping("/location")
public class LocationController {

    @Resource
    private LocationService locationService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object getList(String cityName) {

        City city = locationService.selectCityByCityName(cityName);
        List<District> districtList = locationService.selectDistrictByCity(city.getId());
        List<DistrictPojo> districtPojoList = new ArrayList<DistrictPojo>();
        CityPojo cityPojo = new CityPojo();
        MergeUtil.merge(city, cityPojo);
        for (District district : districtList) {
            DistrictPojo districtPojo = new DistrictPojo();
            MergeUtil.merge(district, districtPojo);

            districtPojoList.add(districtPojo);
        }
        cityPojo.setDistrictPojoList(districtPojoList);
        return cityPojo;
    }


    /**
     * 获取全部省份
     *
     * @return
     */
    @RequestMapping(value = "/province", method = RequestMethod.GET)
    @ResponseBody
    public Object province() {

        List<Province> provinceList = locationService.selectAllProvince();
        List<ProvincePojo> provincePojoList = new ArrayList<ProvincePojo>();
        for (Province province : provinceList) {
            ProvincePojo provincePojo = new ProvincePojo();
            MergeUtil.merge(province,provincePojo);

            provincePojoList.add(provincePojo);
        }
        return provincePojoList;
    }

    /**
     * 根据省份ID获取城市
     *
     * @param provinceId
     * @return
     */
    @RequestMapping(value = "/city", method = RequestMethod.GET)
    @ResponseBody
    public Object city(Long provinceId) {

        List<City> cityList = locationService.selectCityByProvince(provinceId);
        List<CityPojo> cityPojoList = new ArrayList<CityPojo>();
        for (City city : cityList) {
            CityPojo cityPojo = new CityPojo();
            MergeUtil.merge(city, cityPojo);

            cityPojoList.add(cityPojo);
        }
        return cityPojoList;
    }


    @RequestMapping(value = "/district", method = RequestMethod.GET)
    @ResponseBody
    public Object district(Long cityId) {

        List<District> districtList = locationService.selectDistrictByCity(cityId);
        List<DistrictPojo> districtPojoList = new ArrayList<DistrictPojo>();
        for (District district : districtList) {
            DistrictPojo districtPojo = new DistrictPojo();
            MergeUtil.merge(district, districtPojo);

            districtPojoList.add(districtPojo);
        }
        return districtPojoList;
    }

}
