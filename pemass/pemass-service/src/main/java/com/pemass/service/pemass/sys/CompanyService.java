/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.sys.Company;

import java.util.List;
import java.util.Map;

/**
 * @Description: 组织机构
 * @Author: estn.zuo
 * @CreateTime: 2014-10-15 21:05
 */
public interface CompanyService {

    /**
     * 插入一条机构信息
     *
     * @param company
     * @return
     */
    Company insert(Company company);

    /**
     * 更新一条机构信息
     *
     * @param company
     * @return
     */
    Company update(Company company);

    /**
     * 分页获取机构信息
     *
     * @param page
     * @return
     */
    DomainPage selectByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage page);

    /**
     * 根据公司id获取公司信息
     *
     * @param id
     * @return
     */
    Company selectById(Long id);

    /**
     * 获得公司列表
     *
     * @return
     */
    List<Company> getCompanyList();

    /**
     * 根据区域查找分公司
     * @param useArea 公司所属区域
     * @return
     */
    Company selectBranchCompany(String useArea);

    /**
     * 获取积分通总公司信息
     * @return
     */
    Company getGroupCompanyInfo();

    /**
     * 生成代付手续费获取银联信息
     * @return
     */
    Company getChinaPayCompany();
}
