package com.pemass.persist.dao.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.vo.OrderVO;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.OrderTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by POKL on 2014/11/18.
 */
public interface OrderDao extends BaseDao {

    <T extends BaseDomain> void updateProductSaleNumber(Class<T> clazz, List<String> params, Integer amount);

    <T extends BaseDomain> void updateProductSaleNumberReturn(Class<T> clazz, List<String> params, Integer amount);

    /**
     * 获取订单集合
     *
     * @param clazz
     * @param flag                      标识查询当天或者3个月以内的数据【0--3个月以内，其他数字为当天】
     * @param cashierIds                所有收银员id的集合
     * @param fieldNameValueMap         查询的条件
     * @param fuzzyFieldNameValueMap    模糊查询的条件
     * @param compoundFieldNameValueMap 精确查询的条件
     * @param pageIndex                 当前页
     * @param pageSize                  页大小
     * @return
     */
    DomainPage getOrderList(Class clazz, int flag, List<Long> cashierIds, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,
                            Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 景区查询订单
     *
     * @param clazz
     * @param orgainzationId            景区id
     * @param fieldNameValueMap         精确匹配条件
     * @param fuzzyFieldNameValueMap    模糊匹配条件
     * @param compoundFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage scenicSpotOrders(Class clazz, Long orgainzationId, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,
                                Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 分页查询所有订单
     *
     * @param conditions
     * @param domainPage
     * @return
     */
    DomainPage getAllOrdersByPage(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 获取购买过某个商户的商品的用户
     *
     * @param orgainzationId
     * @return
     */
    DomainPage getUsersBuyProductByOrganizationId(Long orgainzationId, long pageIndex, long pageSize);

    /**
     * 查询某个用户订单
     *
     * @param uid        用户ID
     * @param itemStatus 订单项状态集合
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<Order> selectOrder(Long uid, List<OrderItemStatusEnum> itemStatus, long pageIndex, long pageSize);


    /**
     * 获取已支付的订单统计
     *
     * @param terminalUserId
     * @return
     */
    Order sumOrder(Long terminalUserId, String dateType);

    /**
     * 多条件查询
     *
     * @param clazz
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param compoundFieldNameValueMap
     * @param <T>
     * @return
     */
    <T extends BaseDomain> List<T> getOrderByCompoundConditions(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,
                                                                Map<String, Object> compoundFieldNameValueMap);

    /**
     * 根据用户名和收银员ID 获取订单列表
     *
     * @param userList
     * @param terminalUserId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getOrderByUsername(List<User> userList, Long terminalUserId, Long pageIndex, Long pageSize);

    List<Order> getOrderByPeriodOfTime(Class clazz, Map<String, Object> fieldNameValueMap);

    /**
     * 根据订单id查询订单所属商户
     * @param orderId
     * @return
     */
    Organization getOrganizationByOrderId(Long orderId,OrderTypeEnum orderType);

    /**
     * 获取满足条件的自定义订单
     *
     * @param conditions 自定义订单查询条件
     * @param domainPage 分页条件
     * @return 返回的订单结果
     */
    DomainPage getCustomizationOrdersByConditions(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 获取满足条件的自定义订单
     *
     * @param conditions 自定义订单查询条件
     * @return 返回的订单结果
     */
    List<OrderVO> getCustomizationOrdersByConditions(Map<String, Object> conditions);

    /**
     * 查询已确认未支付的自定义订单
     * @return
     */
    List<Order> selectCustomOrderList();

    /**
     * 清分修改订单清分状态
     * @param orderId
     */
    void updateOrderClearingStatus(Long orderId);

    /**
     * 统计当日已支付订单数量
     * @return
     */
    Long getHasPayOrderAmountOfDay();

    /**
     * 查询当日已支付订单交易额
     * @return
     */
    Float getHasPayOrderPricesOfDay();

    /**
     * 根据条件查询当日已支付订单的数据
     * @param field 需要统计的字段名
     * @return
     */
    Long getHasPayOrderOfDayByField(String field);


    /**
     * 查询商户订单明细
     * @param map
     * @param organizationId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectOrganizationOrder(Map<String,Object> map,long organizationId,long pageIndex,long pageSize);
}
