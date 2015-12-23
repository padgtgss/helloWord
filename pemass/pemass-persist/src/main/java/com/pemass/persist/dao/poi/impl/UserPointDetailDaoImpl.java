/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.UserPointDetailDao;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.enumeration.PointTypeEnum;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @Description: UserPointDetailDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 14:39
 */
@Repository
public class UserPointDetailDaoImpl extends JPABaseDaoImpl implements UserPointDetailDao {


    /**
     * 根据用户id查询可使用积分
     *
     * @param uid 用户id
     * @return
     */
    @Override
    public List<UserPointDetail> selectPointCount(Long uid) {
        StringBuffer sb = new StringBuffer();
        sb.append("from UserPointDetail t ")
                .append("where t.userId = ?1 and  t.available=1 and t.expiryTime >= ?2 and t.useableAmount>0");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, new Date());

        return query.getResultList();
    }

    @Override
    public DomainPage selectUsersByPointPurchaseId(List<Long> pointPurchaseIds, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        StringBuffer sb = new StringBuffer();
        String sql = "select c from UserPointDetail c,User u,PointPurchase pp " +
                " where c.userId = u.id and c.pointPurchaseId = pp.id " +
                " and c.available = 1 and u.available = 1 and pp.available =1 " +
                " and c.useableAmount <> 0 and pp.id in (:id) " +
                " group by u.id order by u.registerTime desc";
        Query query = em.createQuery(sql);
        query.setParameter("id", pointPurchaseIds);

        Long totalCount = (long) query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List result = query.getResultList();

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(result);
        return domainPage;
    }

    @Override
    public List<UserPointDetail> selectByUserId(Long uid, PointTypeEnum typeEnum) {
        StringBuffer sb = new StringBuffer();
        sb.append("from PointDetail p ");
        sb.append(" where p.available=1 ");
        sb.append(" and p.userId=?1 ");
        sb.append(" and p.pointType=?2 ");
        sb.append(" and p.expiryTime>?3 ");
        sb.append(" and p.useableAmount>0 ");
        sb.append(" order by p.expiryTime asc ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, typeEnum);
        query.setParameter(3, new Date());
        return query.getResultList();
    }

    @Override
    public Long getEntityById(Long uid) {
        StringBuffer sb = new StringBuffer();
        sb.append("select sum(p.useableAmount) from PointDetail p ");
        sb.append(" where p.available=1 ");
        sb.append(" and p.userId=?1 ");
        sb.append(" and p.pointType=?2 ");
        sb.append(" and p.expiryTime>?3 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, PointTypeEnum.P);
        query.setParameter(3, new Date());
        return (Long) query.getResultList().get(0);
    }

    @Transactional
    @Override
    public void usePointReturn(Long id, Integer payableAmount) {
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE poi_user_point_detail p SET p.useable_amount = p.useable_amount + ?1 ")
                .append("WHERE p.id = ?2  and p.available = 1  and p.expiry_time >?3 and p.amount >= p.useable_amount + ?4 ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, payableAmount);
        query.setParameter(2, id);
        query.setParameter(3, new Date());
        query.setParameter(4,payableAmount).executeUpdate();

    }

    @Override
    public List selectExpireUserPointDetail() {
        String sql = "select pupd.id, pupd.useable_amount,ppp.point_pool_id,pupd.point_type from poi_user_point_detail pupd,poi_point_purchase ppp where " +
                "ppp.id = pupd.point_purchase_id and pupd.expiry_time < now() and pupd.available = 1 and ppp.available = 1 and (pupd.point_type = 'P' OR pupd.point_type = 'E')";
        Query query = em.createNativeQuery(sql);
        List list = query.getResultList();
        return list;
    }

    @Override
    public DomainPage selectUserPointDetail(Long uid, PointTypeEnum pointType, long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer("SELECT * from (")
                .append(" SELECT ppd.point_channel,ppd.amount,ppd.create_time as create_time,ppd.expiry_time,ppd.point_channel_target_id  ")
                .append("from poi_user_point_detail ppd  ")
                .append(" where ppd.user_id = ?1 and ppd.available = 1  and ")
                .append(" ppd.point_type = ?3 and ppd.area ='00:00:00:00:00' ")
                .append(" UNION ALL")
                .append(" SELECT pcd.consume_type,-SUM(pcd.amount),pcd.create_time as create_time,CAST('' as datetime),CAST('' as SIGNED) ")
                .append(" from poi_user_consume_detail pcd  ")
                .append(" LEFT JOIN poi_user_point_detail ppd on(ppd.id = pcd.user_point_detail_id)")
                .append(" where  ppd.point_type =?4  and ppd.area ='00:00:00:00:00' and pcd.amount>0 and")
                .append(" pcd.available = 1 and ppd.user_id = ?5 GROUP BY pcd.consume_target_id")
                .append(" ) as TEMP")
                .append(" ORDER BY create_time DESC");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(3, pointType.toString());
        query.setParameter(4, pointType.toString());
        query.setParameter(5, uid);
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, this.userPointDetailCount(uid, pointType));
        domainPage.setDomains(query.getResultList());
        return domainPage;
    }


    private long userPointDetailCount(Long uid, PointTypeEnum pointType) {
        StringBuffer sb = new StringBuffer("SELECT count(*) from (")
                .append(" SELECT ppd.point_channel,ppd.amount,ppd.create_time as create_time,ppd.expiry_time, ppd.point_channel_target_id  ")
                .append("from poi_user_point_detail ppd  ")
                .append(" where ppd.user_id = ?1 and ppd.available = 1  and ")
                .append(" ppd.point_type = ?3 and ppd.area ='00:00:00:00:00' ")
                .append(" UNION ALL")
                .append(" SELECT pcd.consume_type,SUM(pcd.amount),pcd.create_time as create_time,CAST('' as datetime),CAST('' as SIGNED)")
                .append(" from poi_user_consume_detail pcd  ")
                .append(" LEFT JOIN poi_user_point_detail ppd on(ppd.id = pcd.user_point_detail_id)")
                .append(" where  ppd.point_type =?4  and ppd.area ='00:00:00:00:00' and pcd.amount>0 and")
                .append(" pcd.available = 1 and ppd.user_id = ?5 GROUP BY pcd.consume_target_id")
                .append(" ) as temp ")
                .append(" ORDER BY create_time DESC");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(3, pointType.toString());
        query.setParameter(4, pointType.toString());
        query.setParameter(5, uid);
        return ((BigInteger) query.getResultList().get(0)).longValue();
    }


    @Override
    public DomainPage selectDirectionPointDetailByPool(Long uid, long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT t3.id, t3.issue_name,COALESCE(sum(t1.useable_amount),0)  ")
                .append("  from poi_user_point_detail t1  ")
                .append(" LEFT JOIN poi_point_purchase t2 on(t2.id = t1.point_purchase_id) ")
                .append(" LEFT JOIN poi_point_pool t3 on(t3.id = t2.point_pool_id)  ")
                .append(" where t1.area ='00:00:00:00:?'   and t1.available =1 ")
                .append(" and t1.point_type =?1 and t1.user_id = ?2  and t1.expiry_time >= ?3 ")
                .append("  GROUP BY t3.id");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, PointTypeEnum.P.toString());
        query.setParameter(2, uid);
        query.setParameter(3, new Date());
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, this.getDirectionPointDetailByPoolCount(uid));
        domainPage.setDomains(query.getResultList());
        return domainPage;
    }

    private long getDirectionPointDetailByPoolCount(Long uid) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM(");
        sb.append("SELECT t3.id ")
                .append("  from poi_user_point_detail t1  ")
                .append(" LEFT JOIN poi_point_purchase t2 on(t2.id = t1.point_purchase_id) ")
                .append(" LEFT JOIN poi_point_pool t3 on(t3.id = t2.point_pool_id)  ")
                .append(" where t1.area ='00:00:00:00:?'   and t1.available =1 ")
                .append(" and t1.point_type =?1 and t1.user_id = ?2  and t1.expiry_time >= ?3 ")
                .append("  GROUP BY t3.id   ) T ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, PointTypeEnum.P.toString());
        query.setParameter(2, uid);
        query.setParameter(3, new Date());

        return ((BigInteger) query.getResultList().get(0)).longValue();
    }

    @Override
    public DomainPage selectUserDirectionPointDetail(Long uid, Long pointPoolId, long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer("SELECT * from (")
                .append(" SELECT ppd.point_channel,ppd.amount,ppd.create_time as create_time,ppd.expiry_time, ppd.point_channel_target_id ")
                .append("from poi_user_point_detail ppd  ")
                .append(" where  ppd.area <>'00:00:00:00:00' and ppd.available = 1  and ")
                .append(" ppd.point_type = ?1 and ppd.user_id = ?2  ")
                .append(" UNION ALL")
                .append(" SELECT pcd.consume_type,-SUM(pcd.amount),pcd.create_time as create_time,CAST('' as datetime),CAST('' as SIGNED) ")
                .append(" from poi_user_consume_detail pcd  ")
                .append(" LEFT JOIN poi_user_point_detail ppd on(ppd.id = pcd.user_point_detail_id)")
                .append(" where  ppd.point_type =?4  and ppd.user_id = ?5  and")
                .append(" pcd.available = 1 AND  ppd.area <>'00:00:00:00:00' and pcd.amount>0 GROUP BY pcd.consume_target_id")
                .append(" ) as TEMP")
                .append(" ORDER BY create_time DESC");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, PointTypeEnum.P.toString());
        query.setParameter(2, uid);
        query.setParameter(4, PointTypeEnum.P.toString());
        query.setParameter(5, uid);
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, this.getUserDirectionPointDetailCount(uid));
        domainPage.setDomains(query.getResultList());

        return domainPage;
    }


    private long getUserDirectionPointDetailCount(Long uid) {
        StringBuffer sb = new StringBuffer("SELECT count(*) from (")
                .append(" SELECT ppd.point_channel,ppd.amount,ppd.create_time as create_time,ppd.expiry_time, ppd.point_channel_target_id  ")
                .append("from poi_user_point_detail ppd  ")
                .append(" where  ppd.area <>'00:00:00:00:00' and ppd.available = 1  and ")
                .append(" ppd.point_type = ?1 and ppd.user_id = ?2 ")
                .append(" UNION ALL")
                .append(" SELECT pcd.consume_type,-SUM(pcd.amount),pcd.create_time as create_time,CAST('' as datetime),CAST('' as SIGNED) ")
                .append(" from poi_user_consume_detail pcd  ")
                .append(" LEFT JOIN poi_user_point_detail ppd on(ppd.id = pcd.user_point_detail_id)")
                .append(" where  ppd.point_type =?4   and ppd.user_id = ?5  and")
                .append(" pcd.available = 1 AND ppd.area <>'00:00:00:00:00' and pcd.amount>0 GROUP BY pcd.consume_target_id")
                .append(" ) as TEMP");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, PointTypeEnum.P.toString());
        query.setParameter(2, uid);
        query.setParameter(4, PointTypeEnum.P.toString());
        query.setParameter(5, uid);
        return ((BigInteger) query.getResultList().get(0)).longValue();
    }


    @Override
    public DomainPage selectUserPointDetailP(Long uid, boolean isDirection, long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer();
        sb.append("select t.pointPurchaseId,COALESCE(sum(useableAmount),0),t.expiryTime from UserPointDetail t where t.pointType = ?1 and t.expiryTime>=?2 and t.userId =?3    ");
        sb.append(" and t.useableAmount>0 and t.available =1 ");
        if (isDirection)   //定向积分
            sb.append(" and t.area <> '00:00:00:00:00'");
        else    //通用积分
            sb.append(" and t.area = '00:00:00:00:00'");
        sb.append(" group by t.pointPurchaseId");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, PointTypeEnum.P);
        query.setParameter(2, new Date());
        query.setParameter(3, uid);
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        DomainPage<UserPointDetail> domainPage = new DomainPage<UserPointDetail>(pageSize, pageIndex, this.getUserPointDetailPCount(uid, isDirection));
        domainPage.setDomains(query.getResultList());
        return domainPage;
    }


    @Override
    public Integer getUserPointDetailGeneralPCount(Long uid) {
        StringBuffer sb = new StringBuffer();
        sb.append("select COALESCE(sum(t.useableAmount),0) from UserPointDetail t ")
                .append(" where t.available =1 and t.userId =?1 and t.expiryTime >= ?2 and t.pointType =?3 and t.area ='00:00:00:00:00'");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, new Date());
        query.setParameter(3, PointTypeEnum.P);

        return Integer.valueOf(query.getResultList().get(0).toString());
    }

    @Override
    public Integer getUserPointDetailECount(Long uid) {
        StringBuffer sb = new StringBuffer();
        sb.append("select COALESCE(sum(t.useableAmount),0) from UserPointDetail t ")
                .append(" where t.available =1 and t.userId =?1 and t.expiryTime >= ?2 and t.pointType =?3 and t.area ='00:00:00:00:00'");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, new Date());
        query.setParameter(3, PointTypeEnum.E);

        return Integer.valueOf(query.getResultList().get(0).toString());

    }

    @Override
    public Integer getPointAmountByPointPoolId(Long pointPoolId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select sum(c.useable_amount) from poi_user_point_detail c ")
                .append("left join poi_point_purchase p on c.point_purchase_id = p.id ")
                .append("where c.available = 1 and p.available = 1 and p.point_pool_id = ?1 and c.expiry_time >= ?2");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, pointPoolId);
        query.setParameter(2, new Date());
        Integer amount = 0;
        List result = query.getResultList();
        if (result.get(0) != null) {
            BigDecimal bigDecimal = (BigDecimal) result.get(0);
            amount = bigDecimal.intValue();
        }
        return amount;
    }

    @Override
    public Integer getExpiredAmountByPointPoolId(Long pointPoolId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select sum(c.useable_amount) from poi_user_point_detail c ")
                .append("LEFT JOIN poi_point_purchase p ON c.point_purchase_id = p.id ")
                .append("where p.available = 1 and c.available = 0 and p.point_pool_id = ?1 and c.expiry_time < ?2");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, pointPoolId);
        query.setParameter(2, new Date());
        Integer amount = 0;
        List result = query.getResultList();
        if (result.get(0) != null) {
            BigDecimal bigDecimal = (BigDecimal) result.get(0);
            amount = bigDecimal.intValue();
        }
        return amount;
    }

    @Transactional
    @Override
    public void updateUseableAmountById(Long id, Integer useableAmount) {
        StringBuffer sb = new StringBuffer("UPDATE poi_user_point_detail t ");
        sb.append("SET t.useable_amount = t.useable_amount - ?1  ");
        sb.append("WHERE t.id = ?2 ");
        sb.append("AND t.available = 1 ");
        sb.append("and t.useable_amount >= ?3 ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, useableAmount);
        query.setParameter(2, id);
        query.setParameter(3, useableAmount).executeUpdate();
    }

    private long getUserPointDetailPCount(Long uid, boolean isDirection) {
        StringBuffer sb = new StringBuffer("select count(*) from ( ");
        sb.append("select t.point_purchase_id from poi_user_point_detail t where t.point_type = ?1 and t.expiry_time>=?2 and t.user_Id =?3   ");
        sb.append(" and t.useable_amount>0 and t.available =1 ");
        if (isDirection)   //定向积分
            sb.append(" and t.area <> '00:00:00:00:00'  ");
        else    //通用积分
            sb.append(" and t.area = '00:00:00:00:00'  ");
        sb.append(" group by t.point_purchase_id  ) tt");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, PointTypeEnum.P.toString());
        query.setParameter(2, new Date());
        query.setParameter(3, uid);
        return ((BigInteger) query.getResultList().get(0)).longValue();
    }
}