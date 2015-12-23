package com.pemass.persist.dao.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.ec.Invoice;

import java.util.Map;

/**
 * Created by Administrator on 2014/12/9.
 */
public interface InvoiceDao extends BaseDao {
    DomainPage<Invoice> getInvoiceList(Class clazz,Long organizationId,Map<String,Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);
}
