/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.bas.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.bas.ResourcesDao;
import com.pemass.persist.domain.jpa.bas.Resources;
import com.pemass.persist.enumeration.ResourceType;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description: ResourcesDaoImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-11-03 16:12
 */
@Repository
public class ResourcesDaoImpl extends JPABaseDaoImpl implements ResourcesDao {

    @Override
    public Integer deleteBatchByUUID(String targetUUID) {
        Query deleteBatchByUUIDSql = em.createNativeQuery("delete from bas_resources  where target_uuid = ?1");
        deleteBatchByUUIDSql.setParameter(1, targetUUID);
        return deleteBatchByUUIDSql.executeUpdate();
    }

    @Override
    public List<Resources> selectAllEntities(String targetUUID) {
        String hql=" from Resources  r where r.available=1 and r.targetUUID=?1  order by r.sequence asc ";
        Query query = em.createQuery(hql);
        query.setParameter(1,targetUUID);
        return query.getResultList();
    }

}
