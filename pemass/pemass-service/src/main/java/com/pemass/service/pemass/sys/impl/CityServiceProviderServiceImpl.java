package com.pemass.service.pemass.sys.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.sys.CityServiceProvider;
import com.pemass.service.pemass.sys.CityServiceProviderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @Description: CityServiceProviderServiceImpl
 * @Author: xy
 * @CreateTime: 2015-08-06 11:37
 */

@Service
public class CityServiceProviderServiceImpl implements CityServiceProviderService
{

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public CityServiceProvider getById(Long id) {
        return jpaBaseDao.getEntityById(CityServiceProvider.class,id);
    }

    @Override
    public List<CityServiceProvider> selectCityServiceProviderList(Map<String,Object> map) {
        return jpaBaseDao.getEntitiesByFieldList(CityServiceProvider.class, map);
    }

    @Override
    public DomainPage getCityServiceProvider(Map<String, Object> map, long pageIndex, long pageSize) {
        DomainPage domainPage = jpaBaseDao.getEntitiesPagesByFieldList(CityServiceProvider.class,map,pageIndex,pageSize);
        if (domainPage.getDomains().size() > 0 && domainPage != null) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = new Object[2];
                objects[0] = domainPage.getDomains().get(i);
                BigInteger bigInteger = new BigInteger(((CityServiceProvider) objects[0]).getProvinceId().toString());
                long provinceId = bigInteger.longValue();
                Province province = jpaBaseDao.getEntityById(Province.class, provinceId);
                objects[1] = province;
                domainPage.getDomains().set(i, objects);
            }
        }
        return domainPage;
    }

    @Override
    public CityServiceProvider insert(CityServiceProvider cityServiceProvider) {
        jpaBaseDao.persist(cityServiceProvider);
        return cityServiceProvider;
    }

    @Override
    public CityServiceProvider update(CityServiceProvider cityServiceProvider) {
        CityServiceProvider cityServiceProviders = jpaBaseDao.getEntityById(CityServiceProvider.class, cityServiceProvider.getId());
        cityServiceProviders = (CityServiceProvider) MergeUtil.merge(cityServiceProvider, cityServiceProviders);
        return jpaBaseDao.merge(cityServiceProviders);
    }
}
