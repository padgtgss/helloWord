/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.sys.Company;

/**
 * 公司信息
 */
public interface CompanyDao extends BaseDao{
    /**
     * 生成代付手续费获取银联信息
     * @return
     */
    Company getChinaPayCompany();


}
