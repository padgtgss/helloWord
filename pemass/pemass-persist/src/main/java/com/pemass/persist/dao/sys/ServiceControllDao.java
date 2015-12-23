package com.pemass.persist.dao.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.sys.ServiceControll;

import java.util.List;

/**
 * Created by estn.zuo on 14-9-18.
 */
public interface ServiceControllDao extends BaseDao {


    Boolean grant(String appid,  List<String> urls);

    Boolean hasAuth(String appid, String url);


    DomainPage<String> getByAppId(String appId, Long pageIndex, Long pageSize);

    void unGrant(String appid, String url);

    ServiceControll getByUrl(String url);
}
