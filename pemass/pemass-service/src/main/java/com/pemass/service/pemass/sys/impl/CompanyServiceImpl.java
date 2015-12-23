/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.dao.sys.CompanyDao;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.sys.BankCard;
import com.pemass.persist.domain.jpa.sys.Company;
import com.pemass.persist.enumeration.CompanyTypeEnum;
import com.pemass.service.pemass.bas.ProvinceService;
import com.pemass.service.pemass.sys.BankCardService;
import com.pemass.service.pemass.sys.CompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: CompanyServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-10-15 21:06
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private CompanyDao companyDao;

    @Resource
    private PresentPackDao presentPackDao;

    @Resource
    private ProvinceService provinceService;

    @Resource
    private BankCardService bankCardService;

    @Override
    public Company insert(Company company) {
        jpaBaseDao.persist(company);
        return company;
    }

    @Override
    public Company update(Company company) {
        Company originCompany = jpaBaseDao.getEntityById(Company.class, company.getId());
        originCompany = (Company) MergeUtil.merge(company, originCompany);
        jpaBaseDao.merge(originCompany);
        return company;
    }

    @Override
    public DomainPage selectByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage page) {
        DomainPage domainPage = presentPackDao.getEntitiesPagesByFieldList(Company.class, conditions, fuzzyConditions, page.getPageIndex(), page.getPageSize());
        @SuppressWarnings("unchecked") List<Company> oldCompanys = domainPage.getDomains();
        List<Map<String, Object>> newCompanys = Lists.newArrayList();
        for (Company company : oldCompanys) {
            Map<String, Object> newCompany = Maps.newHashMap();
            BankCard bankCard = bankCardService.getBankCardByTargetUUID(company.getUuid());
            newCompany.put("company", company);
            newCompany.put("bankCard", bankCard);
            newCompanys.add(newCompany);
        }
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(page.getPageSize(), page.getPageIndex(), domainPage.getTotalCount());
        returnDomainPage.setDomains(newCompanys);
        return returnDomainPage;

    }

    @Override
    public Company selectById(Long id) {
        return jpaBaseDao.getEntityById(Company.class, id);
    }

    @Override
    public List<Company> getCompanyList() {
        return jpaBaseDao.getAllEntities(Company.class);
    }

    @Override
    public Company selectBranchCompany(String useArea) {
        Province province = provinceService.getProvinceByName(useArea);
        List<Company> companyList = getCompanyList();
        Company  branchCompany  = null;
        for(Company company : companyList){
            String [] idList = company.getArea().split(",");
            if(idList.length > 0 && idList != null){
                for(int i = 0;i < idList.length;i++){
                    if(company.getCompanyType().equals(CompanyTypeEnum.BRANCH_COMPANY) && province.getId() == Long.valueOf(idList[i])){
                        branchCompany = company;
                        break;
                    }
                }
            }
        }
       return branchCompany;
    }

    @Override
    public Company getGroupCompanyInfo() {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("companyType",CompanyTypeEnum.GROUP);
        List<Company> list=jpaBaseDao.getEntitiesByFieldList(Company.class,map);
        if(list!=null){
            return list.get(0);
        }else {
            return null;
        }
    }
    public Company getChinaPayCompany(){
        return  companyDao.getChinaPayCompany();
    }
}
