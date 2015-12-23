/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.ec.impl;

import com.google.common.collect.Lists;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.dao.ec.InvoiceDao;
import com.pemass.persist.domain.jpa.ec.Invoice;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.enumeration.InvoiceStatusEnum;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.ec.InvoiceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @Description: InvoiceServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-12-10 15:05
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Resource
    private InvoiceDao invoiceDao;

    @Resource
    private ProductService productService;

//    @Resource
//    private OrderService orderService;

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public Invoice selectInvoiceByOrderId(Long orderId) {
        return jpaBaseDao.getEntityByField(Invoice.class, "orderId", orderId);
    }

    /**
     * 查询发票列表
     * @param fieldNameValueMap 精确查询条件
     * @param fuzzyFieldNameValueMap 模糊匹配条件
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public DomainPage<Invoice> getInvoiceList(Long organizationId,Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        DomainPage domainPage =  invoiceDao.getInvoiceList(Invoice.class, organizationId,fieldNameValueMap, fuzzyFieldNameValueMap, pageIndex, pageSize);
        List<Object []> list = Lists.newArrayList();
        if(domainPage.getDomains() != null && domainPage.getDomains().size() > 0){
            for(int i = 0;i < domainPage.getDomains().size();i++){
                Object [] objects = (Object[]) domainPage.getDomains().get(i);
                long invoiceId = new BigInteger(objects[0].toString()).longValue();
                long produtId = new BigInteger(objects[1].toString()).longValue();
                long orderId = new BigInteger(objects[2].toString()).longValue();
                Object [] newObjects = new Object[3];
                newObjects[0] = getInvoiceInfo(invoiceId);
                newObjects[1] = productService.getProductInfo(produtId);
//                newObjects[2] = orderService.getById(orderId);
                newObjects[2] =jpaBaseDao.getEntityById(Order.class,orderId);
                list.add(newObjects);
            }
        }
        domainPage.setDomains(list);
        return domainPage;
    }

    /**
     * 修改发票状态
     * @param billId 订单id
     * @return
     */
    @Override
    public Invoice updateInvoiceStatus(Long billId) {
        Invoice invoice = invoiceDao.getEntityById(Invoice.class,billId);
        if(invoice.getInvoiceStatus() == InvoiceStatusEnum.HAS_SEND){
            invoice.setInvoiceStatus(InvoiceStatusEnum.NONE_SEND);
        }else{
            invoice.setInvoiceStatus(InvoiceStatusEnum.HAS_SEND);
        }
        jpaBaseDao.merge(invoice);
        return invoice;
    }

    /**
     * 发票详情
     * @param invoiceId 发票id
     * @return
     */
    @Override
    public Invoice getInvoiceInfo(Long invoiceId) {
        return invoiceDao.getEntityById(Invoice.class, invoiceId);
    }


    @Override
    public Invoice addInvoice(Invoice invoice) {
        jpaBaseDao.persist(invoice);
        return jpaBaseDao.getEntityByField(Invoice.class, "id", invoice.getId());
    }
}
