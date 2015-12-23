/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.ec.Invoice;

import java.util.Map;

/**
 * @Description: InvoiceService
 * @Author: estn.zuo
 * @CreateTime: 2014-12-10 15:04
 */
public interface InvoiceService {

    /**
     * 根据订单ID查询发票信息
     *
     * @param orderId 订单ID
     * @return
     */
    Invoice selectInvoiceByOrderId(Long orderId);


    /**
     * 查询发票列表
     * @param fieldNameValueMap 精确查询条件
     * @param fuzzyFieldNameValueMap 模糊匹配条件
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<Invoice> getInvoiceList(Long organizationId,Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 修改发票状态
     * @param billId 订单id
     * @return
     */
    Invoice updateInvoiceStatus(Long billId);

    /**
     * 发票详情
     * @param invoiceId 发票id
     * @return
     */
    Invoice getInvoiceInfo(Long invoiceId);

    /**
     * 开发票
     *
     * @param invoice
     * @return
     */
    Invoice addInvoice(Invoice invoice);
}
