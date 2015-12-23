package com.pemass.service.pemass.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.ProductApplyRecord;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description: ProductApplyRecordService
 * @Author: luoc
 * @CreateTime: 2015-06-01 15:39
 */
public interface ProductApplyRecordService {
    /**
     * 分页查询所有自定义订单列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectApplyRecordList(Long organizationId,String productName,long pageIndex,long pageSize);

    /**
     * 分销商申请调价
     *
     * @param record
     * @return
     */
    ProductApplyRecord saveRecord(ProductApplyRecord record);

    /**
     * 获取当前商户所属商品的申请条件纪录
     *
     * @param conditions
     * @param domainPage
     * @return
     */
    DomainPage selectApplyRecordListByConditions(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 处理商品申请调价
     *
     * @param productApply
     * @return
     */
    @Transactional
    void auditProductApply(ProductApplyRecord productApply);

    /**
     * 查询已有申请记录
     * @param fieldMap
     * @return
     */
    List<ProductApplyRecord> getProductApplyRecordList(Map<String, Object> fieldMap);
}
