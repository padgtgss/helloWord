/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.sys.CompanyDao;
import com.pemass.persist.domain.jpa.sys.Company;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * 公司信息
 */
@Repository
public class CompanyDaoImpl extends JPABaseDaoImpl implements CompanyDao {


    @Override
    public Company getChinaPayCompany() {
        String sql = "select * from sys_company t where company_name like '%银联%' " ;
        Query query = em.createNativeQuery(sql, Company.class);
        List<Company> list= (List<Company>) query.getResultList();
        if(list!=null){
           return  list.get(0);
        }else {
            return null;
        }
    }
}