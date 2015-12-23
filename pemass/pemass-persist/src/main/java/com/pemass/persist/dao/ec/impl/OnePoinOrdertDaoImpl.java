/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.ec.OnePoinOrdertDao;
import com.pemass.persist.enumeration.PointChannelEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @Description: OnePoinOrdertDaoImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-06-16 10:37
 */
@Repository
public class OnePoinOrdertDaoImpl extends JPABaseDaoImpl implements OnePoinOrdertDao  {

    @Override
    public Long getPointO(Long uid, List<PointChannelEnum> pointChannelEnums) {
        StringBuffer sb = new StringBuffer();
        sb.append("select sum(t.useableAmount)  from OnePointDetail t where t.available = 1 and ");
        sb.append("  t.user.id=?1 and t.expiryTime>=?2 and t.pointSource in (:pointSourceEnums) ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, new Date());
        query.setParameter("pointSourceEnums", pointChannelEnums);

        List<Object> result  = query.getResultList();
        Long sum  = 0L;


        if(result.size()>0){
            for (Object obj : result) {
                if (obj != null)
                sum += Long.valueOf(obj.toString());
            }
        }


        return sum;
    }
}

