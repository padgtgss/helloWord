package com.pemass.persist.dao.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.BaseDomain;

import java.util.List;
import java.util.Map;

/**
 * Created by hejch on 2014/10/22.
 */
public interface PresentPackDao extends BaseDao {
    /**
     * 根据对象的属性集合查询对象 ,fieldNameValueMap每个属性值采用精确匹配 ,fuzzyFieldNameValueMap模糊匹配（没有某个查询条件给 null 就是）
     *
     * @param clazz
     * @param fieldNameValueMap      精确查询匹配参数
     * @param fuzzyFieldNameValueMap 模糊查询匹配参数
     * @param <T>
     * @return
     */
    <T extends BaseDomain> DomainPage getEntitiesPagesByFieldList(Class<T> clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 超级查询方法（没有某个查询条件给 null 就是）
     *
     * @param clazz
     * @param conditions           精确查询匹配参数
     * @param fuzzyConditions      模糊查询匹配参数
     * @param collectionConditions IN查询匹配参数（List<Object>: in匹配的集合,匹配值如为字符串请记得在匹配值上加上 '' ）
     * @param intervalConditions   Between And查询匹配参数（Object[]: object[0] 开始值,object[1] 结束值）
     * @param orderByConditions    排序匹配参数（支持单排序和混合排序）
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends BaseDomain> DomainPage getEntitiesPagesByFieldList(Class<T> clazz, Map<String, Object> conditions, Map<String, Object> fuzzyConditions,
                                                                  Map<String, List<Object>> collectionConditions, Map<String, Object[]> intervalConditions,
                                                                  Map<String, OrderBy> orderByConditions, long pageIndex, long pageSize);


    /**
     * 根据对象的属性集合查询对象的总记录
     *
     * @param clazz
     * @param conditions
     * @param <T>
     * @return
     */
    <T extends BaseDomain> Long getEntityTotalCount(Class<T> clazz, Map<String, Object> conditions);

    /**
     * 查询审核记录
     *
     * @param fuzzyMap  模糊查询匹配参数
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    DomainPage getAuditRecordByPages(Map<String, Object> fuzzyMap, long pageIndex, long pageSize);

    /**
     * 分页查询所有未审核的红包记录
     *
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getPresentPackByPages(Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 获取当天最大的红包批次编号
     *
     * @param dataStr
     * @return
     */
    String getMaxPackIdentifier(String dataStr);

    /**
     * 获取未过期的红包记录
     *
     * @param domainPage
     * @param conditions
     * @return
     */
    DomainPage getPackRecord(DomainPage domainPage, Map<String, Object> conditions);

    /**
     * 获取某个商户下的有效红包 > 未过期且未发放红包数大于0
     *
     * @param domainPage
     * @return
     */

    DomainPage getValidPack(DomainPage domainPage, Map<String, Object> conditions);

    /**
     * 获取渠道商
     *
     * @param i
     * @param distributionCode
     * @return
     */
    String getEntityByDistributors(int i, String distributionCode);


    DomainPage getEntitiesPagesByList(Map<String, Object> fuzzyConditions, DomainPage domainPage);

    /**
     * 提及满足条件的红包数量
     *
     * @param conditions 条件
     * @return 返回结果
     */
    long getPresentPackByConditions(Map<String, Object> conditions);

    DomainPage getTheBusinessPremises(Map<String, Object> conditions, Map<String, Object> fuzzyConditions,
                                      Map<String, List<Object>> collectionConditions, long pageIndex, long pageSize);
}
