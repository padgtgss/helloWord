package com.pemass.service.pemass.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.sys.CityServiceProvider;

import java.util.List;
import java.util.Map;

/**
 * @Description: CityServiceProviderService
 * @Author: xy
 * @CreateTime: 2015-08-06 11:36
 */
public interface CityServiceProviderService {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CityServiceProvider getById(Long id);

    /**
     * 查询城市服务商列表
     * @return
     */
    List<CityServiceProvider> selectCityServiceProviderList(Map<String,Object> map);

    /**
     * 分页获取城市服务商
     * @param map 分页条件
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getCityServiceProvider(Map<String, Object> map, long pageIndex, long pageSize);

    /**
     * 新增城市服务商
     * @param cityServiceProvider
     */
    CityServiceProvider insert(CityServiceProvider cityServiceProvider);

    /**
     * 更新城市服务商
     * @param cityServiceProvider
     * @return
     */
    CityServiceProvider update(CityServiceProvider cityServiceProvider);
}
