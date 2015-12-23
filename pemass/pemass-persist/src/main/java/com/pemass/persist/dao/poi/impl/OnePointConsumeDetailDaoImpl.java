/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.OnePointConsumeDetailDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

/**
 * @Description: OnePointConsumeDetailDaoImpl
 * @Author: lin.shi
 * @CreateTime: 2015-06-11 21:51
 */
@Repository(value = "OnePointConsumeDetailDao")
public class OnePointConsumeDetailDaoImpl extends JPABaseDaoImpl implements OnePointConsumeDetailDao {

    @Override
    public List<Object[]> consumeDetailCountByOrder(Long uid, Long orderId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select (")
                //消耗自己的积分
                .append("SELECT COALESCE(sum(t.amount), 0) FROM  poi_one_point_consume_detail t WHERE  t.user_id = ?1  AND t.belong_user_id = ?2 AND t.order_id = ?3 ),")
                        //消耗朋友的积分
                .append("(SELECT COALESCE(sum(t.amount), 0) FROM  poi_one_point_consume_detail t WHERE  t.user_id = ?4  AND t.belong_user_id <> ?5 AND t.order_id = ?6 )");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, uid);
        query.setParameter(3, orderId);
        query.setParameter(4, uid);
        query.setParameter(5, uid);
        query.setParameter(6, orderId);
        return query.getResultList();
    }


    @Override
    public Double getConsumeDetailCout(Long uid) {
        String sql = "select COALESCE(sum(t.profit),0) from OnePointConsumeDetail t where t.available =1  and t.belongUserId =?1";
        Query query = em.createQuery(sql);
        query.setParameter(1, uid);
        return Double.valueOf(query.getResultList().get(0).toString());
    }

    @Override
    public Object[] getProfitCount(Long uid) {
        StringBuffer sb = new StringBuffer("select ");
        sb.append("(SELECT COALESCE(SUM(t.profit),0) FROM poi_one_point_consume_detail t WHERE t.user_id = ?1 AND t.belong_user_id = ?2 AND t.available = 1),  ")
                .append("(SELECT COALESCE(SUM(t.profit),0) FROM poi_one_point_consume_detail t WHERE t.user_id <> ?3 AND t.belong_user_id = ?4 AND t.available = 1)");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, uid);
        query.setParameter(3, uid);
        query.setParameter(4, uid);
        return (Object[]) query.getResultList().get(0);
    }

    @Override
    public DomainPage selectProfitDetails(long uid,boolean isYouself, long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer();
        sb.append("select t.order_identifier,COALESCE(sum(t.amount), 0),COALESCE(sum(t.profit), 0),t.create_time,u.username  ")
                .append("  from poi_one_point_consume_detail t ")
                .append(" left join sys_user u on(u.id = t.user_id)")
                .append("  where  t.available =1 and  t.belong_user_id=?1 and t.amount >0 and ");
        if(isYouself){
            sb.append(" t.user_id = ?2 ");
        }else{
            sb.append(" t.user_id <> ?2 ");
        }
        sb.append(" group by t.order_id");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, uid);
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List list = query.getResultList();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, this.getProfitDetailCount(uid,isYouself));
        domainPage.setDomains(list);
        return domainPage;
    }


    private long getProfitDetailCount(long uid,boolean isYouself) {
        StringBuffer sb = new StringBuffer();
        sb.append("select count(*) from (")
                .append("select count(*) from poi_one_point_consume_detail t  ")
                .append(" where t.available = 1 and t.belong_user_id = ?1 and t.amount >0 and ");
        if(isYouself){
            sb.append(" t.user_id = ?2 ");
        }else{
            sb.append(" t.user_id <> ?2 ");
        }
        sb.append("  group by t.order_id )tt");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, uid);
        return ((BigInteger) query.getResultList().get(0)).longValue();
    }


    @Override
    public DomainPage selectForFriendDetail(Long uid, String belongUserName, long pageIndex, long pageSize) {
        StringBuffer sb  = new StringBuffer();
        sb.append("SELECT c.order_identifier, COALESCE(sum(c.amount),0),COALESCE(SUM(c.profit),0) ,c.create_time ")
                .append("  from poi_one_point_consume_detail c  ")
                .append(" LEFT JOIN sys_user u ON (u.id = c.belong_user_id) ")
                .append(" where c.available = 1 and  ")
                .append(" c.user_id = ?1  and u.username = ?2 and c.amount >0 ")
                .append(" GROUP BY c.order_id");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1,uid);
        query.setParameter(2,belongUserName);
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        DomainPage domainPage = new DomainPage(pageSize,pageIndex,this.getForFriendDetailCount(uid,belongUserName));
        domainPage.setDomains(query.getResultList());

        return domainPage;
    }



    private long getForFriendDetailCount(Long uid, String belongUserName){
        StringBuffer sb  = new StringBuffer(" SELECT COUNT(*) from ( ");
        sb.append("SELECT c.order_id")
                .append("  from poi_one_point_consume_detail c  ")
                .append(" LEFT JOIN sys_user u ON (u.id = c.belong_user_id) ")
                .append(" where c.available = 1 and  ")
                .append(" c.user_id = ?1  and u.username = ?2  and c.amount >0")
                .append(" GROUP BY c.order_id   )tt");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1,uid);
        query.setParameter(2,belongUserName);
        return ((BigInteger)query.getResultList().get(0)).longValue();
    }

    @Override
    public Long getFreeGivingPointAmount(Long uid) {
        StringBuffer sb = new StringBuffer();
        sb.append("select COALESCE(sum(t.payableAmount),0) from OnePointConsumeDetail t  ")
                .append("  where  t.userId =?1 and t.belongUserId <> ?2 and t.available =1");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1,uid);
        query.setParameter(2,uid);
        return (Long)query.getResultList().get(0);
    }
}