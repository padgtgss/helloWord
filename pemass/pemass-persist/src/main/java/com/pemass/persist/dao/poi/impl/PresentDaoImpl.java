/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.PresentDao;
import com.pemass.persist.domain.jpa.poi.Present;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PresentItemTypeEnum;
import com.pemass.persist.enumeration.PresentStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @Description: PresentDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 17:23
 */
@Repository(value = "presentDao")
public class PresentDaoImpl extends JPABaseDaoImpl implements PresentDao {

    /**
     * 统计用户未拆分的红包数量
     *
     * @param uid
     * @return
     */
    @Override
    public Long selectUserPackCount(Long uid) {
        StringBuffer sb = new StringBuffer();
        Date NowDate = new Date();
        sb.append("select count(p) from Present p ");
        sb.append(" where p.available = 1 ");
        sb.append(" and p.userId=?1 ");
        sb.append(" and p.auditStatus=?2 ");
        sb.append(" and p.expiryTime>?3 ");
        sb.append(" and p.presentStatus=?4");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, AuditStatusEnum.HAS_AUDIT);
        query.setParameter(3, NowDate);
        query.setParameter(4, PresentStatusEnum.HAS_ISSUE);

        return (Long) query.getResultList().get(0);
    }

    /**
     * 根据商户所属机构的id统计未发放的红包数量
     *
     * @param organizationId 商户所属机构的id
     * @return
     */
    @Override
    public Long getPresentCount(Long organizationId) {
        StringBuffer sb = new StringBuffer();
        Date NowDate = new Date();
        sb.append("select count(p) from Present p,PresentPack c ");
        sb.append(" where p.presentPackId = c.id ");
        sb.append(" and p.available = 1 ");
        sb.append(" and c.available = 1 ");
        sb.append(" and c.organizationId =?1 ");
        sb.append(" and p.auditStatus =?2 ");
        sb.append(" and p.presentStatus =?3");
        sb.append(" and p.expiryTime >?4 ");

        Query query = em.createQuery(sb.toString());
        query.setParameter(1, organizationId);
        query.setParameter(2, AuditStatusEnum.HAS_AUDIT);
        query.setParameter(3, PresentStatusEnum.NONE_ISSUE);
        query.setParameter(4, NowDate);

        return (Long) query.getResultList().get(0);
    }

    /**
     * 根据用户id,查询所用可使用红包
     *
     * @param uid
     * @return
     */
    @Override
    public DomainPage selectPack(Long uid, PresentStatusEnum presentStatus, long pageSize, long pageIndex) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from Present p ");
        sb.append(" where p.available = 1 ");
        sb.append(" and p.userId=?1 ");
        sb.append(" and p.auditStatus=?2 ");
        sb.append(" and p.presentStatus=?3");
        sb.append(" and p.expiryTime>NOW() ");

//        if (presentStatus != null && presentStatus.equals(PresentStatusEnum.HAS_ISSUE)){
//            sb.append(" and p.expiryTime>NOW() ");
//        }

        sb.append(" order by p.expiryTime asc");


        Query query = em.createQuery(sb.toString(), Present.class);

        query.setParameter(1, uid);
        query.setParameter(2, AuditStatusEnum.HAS_AUDIT);
        if (presentStatus != null)
            query.setParameter(3, presentStatus);
        else
            query.setParameter(3, PresentStatusEnum.HAS_ISSUE);


        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Present> list = query.getResultList();
        Long totalCount = selectCount(uid, presentStatus);
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(list);

        return domainPage;
    }


    private long selectCount(Long uid, PresentStatusEnum presentStatus) {
        StringBuffer sb = new StringBuffer();
        sb.append("select count(p) from Present p ");
        sb.append(" where p.available = 1 ");
        sb.append(" and p.userId=?1 ");
        sb.append(" and p.auditStatus=?2 ");
        sb.append(" and p.presentStatus=?3");
        sb.append(" and p.expiryTime>NOW() ");
//        if (presentStatus != null && presentStatus.equals(PresentStatusEnum.HAS_ISSUE)) {
//            sb.append(" and p.expiryTime>NOW() ");
//        }
        Query query = em.createQuery(sb.toString());

        query.setParameter(1, uid);
        query.setParameter(2, AuditStatusEnum.HAS_AUDIT);
        if (presentStatus != null)
            query.setParameter(3, presentStatus);
        else
            query.setParameter(3, PresentStatusEnum.HAS_ISSUE);


        return (Long) query.getResultList().get(0);
    }

    /**
     * 查询出用户某一个红包详情
     *
     * @param uid       用户id
     * @param presentId 红包id
     * @return
     */
    @Override
    public Present selectPackDetail(Long uid, Long presentId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from Present p ");
        sb.append(" where p.available = 1 ");
        sb.append(" and p.id=?1");
        sb.append(" and p.userId=?2 ");
        sb.append(" and p.auditStatus=?3 ");
        sb.append(" and p.expiryTime>?4 ");
        sb.append(" and p.presentStatus=?5");


        Query query = em.createQuery(sb.toString());

        query.setParameter(1, presentId);
        query.setParameter(2, uid);
        query.setParameter(3, AuditStatusEnum.HAS_AUDIT);
        query.setParameter(4, new Date());
        query.setParameter(5, PresentStatusEnum.HAS_ISSUE);
        List<Present> list = query.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询总条数
     *
     * @param uid
     * @return
     */
    @Override
    public long selectCount(Long uid) {
        StringBuffer sb = new StringBuffer();
        Date NowDate = new Date();
        sb.append("select count(p) from Present p ");
        sb.append(" where p.available = 1 ");
        sb.append(" and p.userId=?1 ");
        sb.append(" and p.auditStatus=?2 ");
        sb.append(" and p.expiryTime>?3 ");
        sb.append(" and p.presentStatus=?4");

        Query query = em.createQuery(sb.toString());

        query.setParameter(1, uid);
        query.setParameter(2, AuditStatusEnum.HAS_AUDIT);
        query.setParameter(3, NowDate);
        query.setParameter(4, PresentStatusEnum.HAS_ISSUE);

        return (Long) query.getResultList().get(0);
    }

    @Override
    public DomainPage selectReceivePresentById(Long organizationId, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        StringBuffer sb = new StringBuffer();
        sb.append(" select p from Present p,PresentPack pp,User u ");
        sb.append(" where p.presentPackId = pp.id and p.userId = u.id ");
        sb.append(" and p.available = 1 and pp.available = 1 and u.available = 1 ");
        sb.append(" and p.auditStatus = 2 and p.presentStatus in (2,3) and pp.organizationId = ?1 ");
        sb.append(" group by p.userId order by u.registerTime desc");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, organizationId);
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();
        sb.delete(0, sb.length()); //清空sb
        //获取总记录数
        sb.append(" select COUNT(DISTINCT p.userId) from Present p,PresentPack pp,User u ");
        sb.append(" where p.presentPackId = pp.id and p.userId = u.id ");
        sb.append(" and p.available = 1 and pp.available = 1 and u.available = 1 ");
        sb.append(" and p.auditStatus = 2 and p.presentStatus in (2,3) and pp.organizationId = ?1 ");
        sb.append(" group by p.userId order by u.registerTime desc");
        query = em.createQuery(sb.toString());
        query.setParameter(1, organizationId);
        Long totalCount = 0L;
        if (query.getResultList().size() > 0 && query.getResultList() != null) {
            totalCount = (Long) query.getResultList().get(0);
        }
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);

        return domainPage;
    }

    @Override
    public boolean updateAuditStatus(AuditStatusEnum auditStatus, Long presentPackId) {
        String updateSql = "UPDATE Present t SET t.auditStatus = ?1 " +
                " WHERE t.available = 1 AND t.presentPackId = ?2 ";
        Query query = em.createQuery(updateSql);
        query.setParameter(1, auditStatus);
        query.setParameter(2, presentPackId);
        return query.executeUpdate() != 0;
    }

    @Override
    public long getTotalPointByPresentPackID(Long presnetPackID, PresentItemTypeEnum presentItemType) {
        StringBuilder sql = new StringBuilder("SELECT SUM(pi.amount) FROM poi_present p,poi_present_item pi");
        sql.append(" WHERE p.id = pi.present_id");
        if (presnetPackID != null) sql.append(" AND p.present_pack_id = " + presnetPackID);
        if (presentItemType != null) sql.append(" AND pi.present_item_type = '" + presentItemType.toString() + "'");

        Query query = em.createNativeQuery(sql.toString());

        List<Object> result = query.getResultList();
        long pointTotalAmount = 0l;
        if (result.size() == 1)
            pointTotalAmount = result.get(0) != null ? Long.parseLong(result.get(0).toString()) : 0;

        return pointTotalAmount;
    }
}