/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi.impl;

import com.pemass.persist.enumeration.PointChannelEnum;
import com.pemass.persist.dao.poi.OnePointDetailDao;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.domain.jpa.poi.OnePointDetail;
import org.springframework.stereotype.Repository;
import com.pemass.common.core.pojo.DomainPage;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;


/**
 * @Description: OnePointDetailDaoImpl
 * @Author: lin.shi
 * @CreateTime: 2015-06-11 21:39
 */
@Repository(value = "OnePointDetailDao")
public class OnePointDetailDaoImpl extends JPABaseDaoImpl implements OnePointDetailDao {

    @Override
    public Long selectPointGiveAmount(long userId, boolean isGive) {
        StringBuffer sb = new StringBuffer();
        sb.append("select COALESCE(sum(t.useableAmount),0) from OnePointDetail t where t.pointChannelEnum =?1 and t.expiryTime>=?2 and t.available = 1  ");
        if (isGive) {   //积分来源为 朋友
            sb.append(" and t.userId = ?3 and t.belongUserId <>?4");
        } else {      //积分来源为自己
            sb.append(" and t.userId <> ?3 and t.belongUserId =?4");
        }

        Query query = em.createQuery(sb.toString());
        query.setParameter(1, PointChannelEnum.ONE_POINT_GIVE);
        query.setParameter(2, new Date());
        query.setParameter(3, userId);
        query.setParameter(4, userId);
        List<Object> list = query.getResultList();
        return (list == null || list.get(0) == null) ? 0L : (Long) list.get(0);
    }


    @Override
    public List<Object[]> pointCount(Long uid) {
        StringBuffer sb = new StringBuffer();
        sb.append("select COALESCE(sum(t.amount),0),COALESCE(sum(t.useableAmount),0) from OnePointDetail t where t.available = 1 and ");
        sb.append("  t.userId=?1 and t.pointChannelEnum=?2  and t.expiryTime>=?3 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, PointChannelEnum.ONE_POINT_PURCHASE);
        query.setParameter(3, new Date());

        return query.getResultList();
    }

    @Override
    public DomainPage getPointDetail(Long uid, long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer("from OnePointDetail t where t.userId=?1 and t.expiryTime>=?2 and t.pointChannelEnum=?3  and t.available = 1 and t.useableAmount=0 ");
        sb.append(" order by t.expiryTime asc ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, new Date());
        query.setParameter(3, PointChannelEnum.ONE_POINT_PURCHASE);
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<OnePointDetail> list = query.getResultList();
        long totalCount = getEntityTotalCount(uid);
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.setDomains(list);
        return domainPage;
    }

    @Override
    public List<OnePointDetail> selectDetailByUserId(Long uid) {
        StringBuffer sb = new StringBuffer("from OnePointDetail t where t.userId=?1 and t.belongUserId=?2 and t.expiryTime>=?3 and t.available = 1 and t.useableAmount>0  ");
        sb.append(" order by t.expiryTime asc ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, uid);
        query.setParameter(3, new Date());
        return query.getResultList();
    }

    private Long getEntityTotalCount(Long uid) {
        StringBuffer sb = new StringBuffer("select count(t) from OnePointDetail t where t.userId=?1 and t.expiryTime>=?2  and t.pointChannelEnum=?3 and t.available = 1 and t.useableAmount=0 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, new Date());
        query.setParameter(3, PointChannelEnum.ONE_POINT_PURCHASE);
        return (Long) query.getResultList().get(0);
    }


    @Override
    public DomainPage pointGiveSearch(long uid, boolean isGive, long pageIndex, long pageSize) {

        StringBuffer sb = new StringBuffer("select  t.id,t.amount,t.useable_amount,u.username ,COALESCE(SUM(c.profit),0), t.create_time ")
                .append(" from poi_one_point_detail t  ")
                .append(" left join poi_one_point_consume_detail c on (c.one_point_detail_id = t.id) ");
        if (isGive) {  //被赠送的
            sb.append("left join sys_user u on(t.belong_user_id=u.id) where  t.belong_user_id <> ?2 and t.user_id=?1 ");
        } else {  //赠送给朋友的
            sb.append(" left join sys_user u on(t.user_id = u.id) where t.uid <>?1 and t.belong_user_id =?2 ");
        }
        sb.append(" and  t.expiry_time>=?3 and t.point_source=?4 and t.available = 1")
                .append(" group by t.id ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, uid);
        query.setParameter(3, new Date());
        query.setParameter(4, PointChannelEnum.ONE_POINT_GIVE.ordinal());
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Object[]> list = query.getResultList();
        long totalCount = getPointGiveCount(uid, isGive);
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.setDomains(list);
        return domainPage;
    }

    /**
     * 统计赠送或是被赠送的积分总记录数
     *
     * @param uid
     * @param isGive
     * @return
     */
    private long getPointGiveCount(long uid, boolean isGive) {
        StringBuffer sb = new StringBuffer();
        if (isGive) { //被赠送
            sb.append("select count(t) from OnePointDetail t where t.belongUserId <>?1 and t.userId=?2 and  t.expiryTime>=?3 and t.pointChannelEnum=?4  and t.available = 1   ");
        } else {  //赠送给朋友的
            sb.append("select count(t) from OnePointDetail t where t.belongUserId=?1 and t.userId<>?2 and  t.expiryTime>=?3 and t.pointChannelEnum=?4  and t.available = 1   ");
        }
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, uid);
        query.setParameter(3, new Date());
        query.setParameter(4, PointChannelEnum.ONE_POINT_GIVE);
        return (Long) query.getResultList().get(0);
    }


    @Override
    public OnePointDetail selectPointGiveDetail(long id) {
        StringBuffer sb = new StringBuffer("from OnePointDetail t where t.id =?1 and t.expiryTime >=?2")
                .append(" and t.pointChannelEnum = ?3 and t.available=1");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, id);
        query.setParameter(2, new Date());
        query.setParameter(3, PointChannelEnum.ONE_POINT_GIVE);
        List<OnePointDetail> list = query.getResultList();
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public DomainPage selectPointGiveDetailAmount(long uid, long pageSize, long pageIndex) {

        StringBuffer sb = new StringBuffer("select u.id,u.userName,COALESCE(sum(t.useable_amount),0) from poi_one_point_detail t  ");

        sb.append("   left join  sys_user u on(t.belong_user_id = u.id) where t.user_id = ?1 and t.belong_user_id<>?2  ");

        sb.append("and t.point_source =?3 and t.expiry_time >= ?4  and t.available = 1 GROUP BY  t.belong_user_id");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, uid);
        query.setParameter(3, PointChannelEnum.ONE_POINT_GIVE.ordinal());
        query.setParameter(4, new Date());
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List list = query.getResultList();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, this.getPointGiveDetailAmountCount(uid));
        domainPage.setDomains(list);
        return domainPage;
    }

    private long getPointGiveDetailAmountCount(Long uid) {
        StringBuffer sb = new StringBuffer("select count(*) from ( ");
        sb.append(" select t.belong_user_id from poi_one_point_detail t  ");

        sb.append("    where t.user_id = ?1 and t.belong_user_id<>?2  ");

        sb.append("and t.point_source =?3 and t.expiry_time >= ?4  and t.available = 1 GROUP BY  t.belong_user_id) tt");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, uid);
        query.setParameter(3, PointChannelEnum.ONE_POINT_GIVE.ordinal());
        query.setParameter(4, new Date());
        return ((BigInteger) query.getResultList().get(0)).longValue();
    }


    @Override
    public Long getByUseableAmount(Long uid) {
        StringBuffer sb = new StringBuffer(" select COALESCE(sum(t.useableAmount),0) from OnePointDetail t where t.userId=?1 and t.expiryTime>=?2 and t.available = 1   ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, new Date());
        List<Object> list = query.getResultList();
        return (list == null || list.get(0) == null) ? 0L : (Long) list.get(0);
    }

    @Override
    public List<Object[]> getByOnePointSearch(Long uid) {
        StringBuffer sb = new StringBuffer("SELECT COALESCE(sum(t.amount),0),COALESCE(sum(t.useableAmount),0),u.username,t.createTime from poi_one_point_detail t ");
        sb.append("left join sys_user u on(u.id = t.user_id)");
        sb.append(" where (t.user_id=?1 or t.belongUserId=?2)  and t.expiryTime>=?3 and t.useableAmount>0  and t.available = 1 ");
        sb.append(" group by u.id ORDER BY t.expiryTime asc ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, uid);
        query.setParameter(3, new Date());
        return query.getResultList();
    }

    @Override
    public Long getByRemainpointCount(Long uid) {
        StringBuffer sb = new StringBuffer("select COALESCE(sum(t.useableAmount),0) from OnePointDetail t where t.userId = ?1 and t.belongUserId =?2 and t.expiryTime>=?3 and t.available = 1");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, uid);
        query.setParameter(3, new Date());
        List<Object> list = query.getResultList();
        return (list == null || list.get(0) == null) ? null : (Long) list.get(0);
    }

    @Override
    public Long getByGivePointCount(Long uid) {
        StringBuffer sb = new StringBuffer("select COALESCE(sum(t.useableAmount),0) from OnePointDetail t where t.belongUserId = ?1 and t.pointChannelEnum =?2 and t.expiryTime>=?3 and t.available = 1");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, PointChannelEnum.ONE_POINT_GIVE);
        query.setParameter(3, new Date());
        List<Object> list = query.getResultList();
        return (list == null || list.get(0) == null) ? null : (Long) list.get(0);
    }

    @Override
    public List<OnePointDetail> selectDetail(Long uid, Long belongUserId) {
        StringBuffer sb = new StringBuffer("from OnePointDetail t ")
                .append("where t.userId = ?1 and t.belongUserId = ?2 and t.expiryTime >= ?3 ")
                .append("   and t.useableAmount >0 and t.available =1");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, belongUserId);
        query.setParameter(3, new Date());
        return query.getResultList();
    }

    @Override
    public void clearPoint(Long uid) {
        StringBuffer sb = new StringBuffer();
        sb.append("update OnePointConsumeDetail set amount =0,payableAmount =0,profit = 0 ");
        sb.append(" where amount >0 and available=1 and userId = ?1");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1,uid);
        query.executeUpdate();
    }
}