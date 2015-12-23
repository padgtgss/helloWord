package com.pemass.persist.dao.sys.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.sys.UserDao;
import com.pemass.persist.domain.jpa.sys.Account;
import com.pemass.persist.domain.jpa.sys.User;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description: 账户DAO
 * Author: estn.zuo
 * CreateTime: 2014-09-18 18:17
 */
@Repository
public class UserDaoImpl extends JPABaseDaoImpl implements UserDao {


    @Override
    public Account getAccountByUsername(String username) {
        return null;
    }

    @Override
    public Account getEntityAccount(Account account, String accountname, String fieldValue) {

        StringBuffer sb = new StringBuffer();
        sb.append("from Account a ");
        sb.append(" left join fetch a.province p ");
        sb.append(" left join fetch a.city c ");
        sb.append(" left join fetch a.district d ");
        sb.append(" where  a.").append(accountname).append("=?1 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, fieldValue);
        List<Account> ret = query.getResultList();
        if (ret == null || ret.size() < 1) {
            return null;
        }
        //TODO how to deal with multiple record.
        return ret.get(0);
    }

    @Override
    public List<User> getUserByRange(String coordinate, Integer distance, Map<String, Object> conditions) {
        /**
         * 拼条件
         */
        String[] coordinates = coordinate.split(",");
        String longitude = coordinates[0];
        String latitude = coordinates[1];

        StringBuffer sql = new StringBuffer("select u.* from sys_user AS u where u.available = 1");
        if (conditions != null) {
            Set<String> fileNames = conditions.keySet();
            Iterator<String> fileNameIterator = fileNames.iterator();
            for (int i = 1; i <= fileNames.size(); i++) {
                String fileName = fileNameIterator.next();
                sql.append(" and u." + fileName + " = " + conditions.get(fileName));
            }
        }
        sql.append(" having 12756274 * asin(");
        sql.append(" Sqrt(power(sin((" + latitude + " - u.latitude) * 0.008726646),2) +");
        sql.append(" Cos(" + latitude + " * 0.0174533) * Cos(u.latitude * 0.0174533) *");
        sql.append(" power(sin((" + longitude + " - u.longitude) * 0.008726646),2))");
        sql.append(" ) <= " + distance + "");

        Query query = em.createNativeQuery(sql.toString(), User.class);
        List list = query.getResultList();

        return list;
    }

    @Override
    public Long getRegisterUserAmount() {
        DateTime maxDate = DateTime.now().minusDays(1).millisOfDay().withMaximumValue();

        StringBuffer sql = new StringBuffer(" SELECT COUNT(u.id) FROM sys_user u ")
                .append(" where u.available =1 ")
                .append(" AND u.register_time <= ?1 ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1,maxDate.toString("yyyy-MM-dd HH:mm:ss"));
        List result = query.getResultList();

        Long totalCount = 0L;
        if (result.get(0) != null) {
            totalCount = Long.parseLong(result.get(0).toString());
        }
        return totalCount;
    }

    @Override
    public Long getAddUsersAmountByDay() {
        DateTime minDate = DateTime.now().minusDays(1).millisOfDay().withMinimumValue();
        DateTime maxDate = DateTime.now().minusDays(1).millisOfDay().withMaximumValue();

        StringBuffer sql = new StringBuffer(" SELECT COUNT(u.id) FROM sys_user u ")
                .append(" where u.available =1 ")
                .append(" AND u.register_time >= ?1 and u.register_time <= ?2 ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1,minDate.toString("yyyy-MM-dd HH:mm:ss"));
        query.setParameter(2,maxDate.toString("yyyy-MM-dd HH:mm:ss"));
        List result = query.getResultList();

        Long totalCount = 0L;
        if (result.get(0) != null) {
            totalCount = Long.parseLong(result.get(0).toString());
        }
        return totalCount;
    }

    @Override
    public User getUserByUsername(String username) {
        StringBuffer sql  = new StringBuffer();
        sql.append("select * from sys_user t ")
                .append("where t.username  = '"+ username +"' ");
        Query query = em.createNativeQuery(sql.toString(), User.class);
        if (query.getResultList().size()>0)
        {
            return (User) query.getResultList().get(0);
        }
        return null;
    }

}

