package com.pemass.service.pemass.sys.impl;

import com.google.common.collect.ImmutableList;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.sys.ServiceControllDao;
import com.pemass.persist.domain.jpa.sys.ServiceControll;
import com.pemass.persist.enumeration.AppTypeEnum;
import com.pemass.service.pemass.sys.ServiceControllService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ServiceControllServiceImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-08-26 20:17
 */
@Service
public class ServiceControllServiceImpl implements ServiceControllService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private ServiceControllDao serviceControllDao;

    @Override
    public ServiceControll insert(ServiceControll serviceControll) {
        jpaBaseDao.persist(serviceControll);
        return serviceControll;
    }

    @Override
    public ServiceControll update(ServiceControll serviceControll) {
        ServiceControll temp = jpaBaseDao.getEntityById(ServiceControll.class,serviceControll.getId());

        return jpaBaseDao.merge((ServiceControll)MergeUtil.merge(serviceControll,temp));
    }

    @Override
    public ServiceControll delete(ServiceControll serviceControll) {
        serviceControll.setAvailable(AvailableEnum.UNAVAILABLE);
        return jpaBaseDao.merge(serviceControll);
    }

    @Override
    public DomainPage selectAll(Long pageIndex, Long pageSize) {
        return jpaBaseDao.getAllEntitiesByPage(ServiceControll.class,pageIndex,pageSize);
    }

    @Override
    public List<ServiceControll> selectAll() {
        return jpaBaseDao.getAllEntities(ServiceControll.class);
    }

    @Override
    public ServiceControll getById(Long id) {

        return jpaBaseDao.getEntityById(ServiceControll.class,id);
    }

    @Override
    public Boolean grant(String appid,  List<String> urls) {
        List<String> list = new ArrayList<String>();
        for(String url : urls){
            list.add(url);
        }
        return serviceControllDao.grant(appid, list);
    }

    @Override
    public Boolean hasAuth(String appid, String url) {
        return serviceControllDao.hasAuth(appid, url);
    }

    @Override
    public DomainPage getByServiceName(String servicename, long pageIndex, long pageSize) {
        Expression expression = new Expression("servicename", Operation.AllLike,servicename);
        return jpaBaseDao.getEntitiesPagesByExpressionList(ServiceControll.class,ImmutableList.of(expression),pageIndex,pageSize);
    }

    @Override
    public DomainPage getByAppId(String appId, Long pageIndex, Long pageSize) {
        /*DomainPage domainPage = new DomainPage();
        domainPage.setPageSize(pageSize);
        domainPage.setPageIndex(pageIndex);
        domainPage.setPageSize(1L);
        domainPage.setTotalCount(1L);
        domainPage.setPageCount(1L);
        if(this.hasAuth(appId,"B*//**") && this.hasAuth(appId,"C*//**")){
            domainPage.setDomains(ImmutableList.of(serviceControllDao.getByUrl("B*//**"),serviceControllDao.getByUrl("C*//**")));
            domainPage.setTotalCount(2L);
            return domainPage;
        }else if (this.hasAuth(appId,"B*//**")) {
            domainPage.setDomains(ImmutableList.of(serviceControllDao.getByUrl("B*//**")));
            return domainPage;
        }else if(this.hasAuth(appId,"C*//**")){
            domainPage.setDomains(ImmutableList.of(serviceControllDao.getByUrl("C*//**")));
            return domainPage;
        }else {*/

            DomainPage _domainPage = serviceControllDao.getByAppId(appId, pageIndex, pageSize);
            List<ServiceControll> serviceControlls = new ArrayList<ServiceControll>();
            for (Object url : _domainPage.getDomains()) {
                ServiceControll serviceControll = serviceControllDao.getByUrl(url.toString());
                if (serviceControll != null)
                    serviceControlls.add(serviceControll);
            }
            _domainPage.setDomains(serviceControlls);
            return _domainPage;

    }

    @Override
    public List<ServiceControll> selectServiceControlls(String servicename, AppTypeEnum appType) {
        List expressions = new ArrayList();
        if(servicename != null && !"".equals(servicename)) {
            Expression servicenameEx = new Expression("servicename", Operation.Equal, servicename);
            expressions.add(servicenameEx);
        }
        if(appType !=null) {
            Expression appTypeEx = new Expression("appType", Operation.Equal, appType);
            expressions.add(appTypeEx);
        }
        return jpaBaseDao.getEntitiesByExpressionList(ServiceControll.class, expressions);
    }

    @Override
    public DomainPage<ServiceControll> selectServiceControlls(AppTypeEnum appType, long pageIndex, long pageSize) {
        Expression appTypeEx = new Expression("appType", Operation.Equal, appType);
        return jpaBaseDao.getEntitiesPagesByExpression(ServiceControll.class, appTypeEx, pageIndex, pageSize);
    }

    @Override
    public void unGrant(String appid, String url) {
        serviceControllDao.unGrant(appid,url);
    }
}

