package com.pemass.service.pemass.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.domain.Expression;
import com.pemass.persist.domain.jpa.biz.AuditRecord;
import com.pemass.persist.domain.jpa.biz.Product;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/14.
 */
public interface AuditRecordService {
    /**
     * 新增一条一元购商品申请记录
     * @param product
     */
    void addOneProductAuditRecord(Product product);

    /**
     * 根据商品id查询该商品申请一元购记录
     * @param productId
     * @return
     */
    AuditRecord selectAuditRecordByProductId(Long productId);

    /**
     * 判断是否申请过一元购商品
     * @param productId
     * @return 0、没有 1、有
     */
    Integer isThereAuditRecord(Long productId);

    /**
     * 新增一条申请一元购支付记录
     * @param oId 机构id
     */
    void addOnePayAuditRecord(Long oId);

    /**
     * 分页查询申请列表
     * @param list
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getAuditRecordByPage(List<Expression> list,long pageIndex,long pageSize);

    /**
     * 根据id记录
     * @param auditRecordId
     * @return
     */
    AuditRecord getAuditRecordById(Long auditRecordId);

    /**
     * 更新记录
     * @param auditRecord
     */
    void updateAuditRecordInfo(AuditRecord auditRecord);
}
