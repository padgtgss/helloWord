package com.pemass.persist.dao.ec.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.ec.ClearingDao;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.ec.Clearing;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: ClearingDaoImpl
 * @Author: luoc
 * @CreateTime: 2015-08-18 10:37
 */
@Repository(value = "clearingDao")
public class ClearingDaoImpl extends JPABaseDaoImpl implements ClearingDao {

    @Override
    public List<Object[]> getGroupClearing() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date());
        String sql = "SELECT e.income_bank_card_id,e.outgo_bank_card_id,e.settlement_role_target_id,e.settlement_role, SUM(e.amount) as amount, e.province_id,e.city_id,e.district_id,e.target_bank_card_id, SUM(e.target_amount) as target_amount,e.transaction_account_type  ";
        sql += " FROM ec_clearing e where e.is_settle=0 and e.target_bank_card_id <> 0 and date_format(e.settlement_date,'%y-%m-%d') <= date_format('"+ date +"','%y-%m-%d')  GROUP BY  e.income_bank_card_id , e.outgo_bank_card_id,e.settlement_role";

        Query query = em.createNativeQuery(sql);
        List<Object[]> result = query.getResultList();
        return result;
    }

    @Override
    public DomainPage getOrganizationClearList(Map<String, Object> map, Long organizationId, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ec_clearing where");
        if(organizationId == null){
            sb.append(" available = 1");
        }else{
            sb.append(" settlement_role_target_id = " + organizationId + " and available = 1");
        }
        if (map.size() != 0) {
            if (map.get("startTime") != null) {
                sb.append(" and date_format(create_time,'%y-%m-%d') >= date_format(?1,'%y-%m-%d')");
            }
            if (map.get("endTime") != null) {
                sb.append(" and date_format(create_time,'%y-%m-%d') <= date_format(?2,'%y-%m-%d')");
            }
        }
        String manageStatus = map.get("manageStatus").toString();
        int paymentStatus = Integer.valueOf(map.get("paymentStatus").toString());
        if(!manageStatus.equals("-1")){
            sb.append(" and is_settle = " + Integer.valueOf(manageStatus));
        }
        if(paymentStatus != 0){
            if(paymentStatus == 1){
                sb.append(" and target_amount > 0");
            }else{
                sb.append(" and target_amount < 0");
            }

        }
        sb.append(" and transaction_account_type = 0 order by create_time desc");
        Query query = em.createNativeQuery(sb.toString(), Clearing.class);
        if (map.size() != 0) {
            if (map.get("startTime") != null) {
                query.setParameter(1, dd.format((Date) map.get("startTime")));
            }
            if (map.get("endTime") != null) {
                query.setParameter(2, dd.format((Date) map.get("endTime")));
            }
        }
        Long totalCount = (long)query.getResultList().size();
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Product> resultList = query.getResultList();

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public DomainPage getBusinessProfitsList(Map<String, Object> map, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ec_clearing where (settlement_role = 'POINT_PROFIT_FOR_ISSUE' or settlement_role = 'POINT_PROFIT_FOR_ACCEPT'");
        sb.append(" or settlement_role = 'AGENT_PROFIT_FOR_ISSUE' OR settlement_role = 'AGENT_PROFIT_FOR_ACCEPT' OR settlement_role = 'POINT_FOR_ISSUE')");
        if (map.size() != 0) {
            if (map.get("startTime") != null) {
                sb.append(" and date_format(create_time,'%y-%m-%d') >= date_format(?1,'%y-%m-%d')");
            }
            if (map.get("endTime") != null) {
                sb.append(" and date_format(create_time,'%y-%m-%d') <= date_format(?2,'%y-%m-%d')");
            }
        }
        long settlementRoleId = Long.valueOf(map.get("settlementRoleId").toString());
        int status = Integer.valueOf(map.get("status").toString());
        sb.append(" and settlement_role_target_id ="+settlementRoleId);
        sb.append(" and transaction_account_type ="+status);
        sb.append(" and is_settle = 2");
        sb.append(" and available = 1  ORDER BY create_time DESC");
        Query query = em.createNativeQuery(sb.toString(),Clearing.class);
        if (map.size() != 0) {
            if (map.get("startTime") != null) {
                query.setParameter(1, dd.format((Date) map.get("startTime")));
            }
            if (map.get("endTime") != null) {
                query.setParameter(2, dd.format((Date) map.get("endTime")));
            }
        }
        Long totalCount = (long)query.getResultList().size();
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Product> resultList = query.getResultList();

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public List getBusinessProfitsOrganizationList(int status) {
        StringBuffer sb = new StringBuffer();
        sb.append("select settlement_role_target_id,transaction_account_type,settlement_role from ec_clearing where" +
                "(settlement_role = 'POINT_PROFIT_FOR_ISSUE' OR settlement_role = 'POINT_PROFIT_FOR_ACCEPT' OR settlement_role = 'AGENT_PROFIT_FOR_ISSUE'" +
                 "OR settlement_role = 'AGENT_PROFIT_FOR_ACCEPT'OR settlement_role = 'POINT_FOR_ISSUE')"+
                " and available = 1 and transaction_account_type = "+ status +" GROUP BY settlement_role_target_id ORDER BY create_time DESC");
        Query query = em.createNativeQuery(sb.toString());
        List list = query.getResultList();
        return list;
    }
}
