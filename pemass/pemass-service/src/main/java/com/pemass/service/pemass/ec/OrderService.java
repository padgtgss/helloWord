package com.pemass.service.pemass.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.Payment;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.OrderStatusEnum;
import com.pemass.persist.enumeration.OrderTypeEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import com.pemass.persist.enumeration.PaymentTypeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;

import java.util.List;
import java.util.Map;

/**
 * Created by POKL on 2014/11/18.
 */
public interface OrderService {

    /**
     * 保存订单
     *
     * @param order
     */
    void saveOrder(Order order);

    /**
     * 生成商品订单
     *
     * @param username
     * @param terminalUserId
     *@param productId
     * @param amount
     * @param totalPrice
     * @param usePointE
     * @param usePointP      @return
     */
    Order insertProductOrder(String username, Long terminalUserId, Long siteId , Long productId, Integer amount, Double totalPrice, Integer usePointE, Integer usePointP, Integer usePointO, List<OrderPointPayDetailPojo> orderPointPayDetailPojoList);

    /**
     * 生成自定义订单
     * <p/>
     * 收银员ID和机构ID不能同时为空
     *
     * @param username                用户名
     * @param originalPrice           消费金额
     * @param organizationId          机构ID
     * @param terminalUserId          收银员ID
     * @param externalOrderIdentifier 外部订单号
     * @param remark                  备注
     * @return
     */
    Order insertCustomizationOrder(String username, Double originalPrice, Long organizationId, Long terminalUserId, String externalOrderIdentifier, String remark);


    /**
     * 支付自定义订单
     *
     * @param orderId                   订单ID
     * @param paymentType               支付类型
     * @param terminalUserId            检票员ID
     * @param externalPaymentIdentifier 外部支付流水号
     * @param payId
     * @param posSerial                 机具号
     * @param deviceId                  设备ID
     * @param isSucceed
     * @return
     */
    Payment payCustomizationOrder(Long orderId, PaymentTypeEnum paymentType,
                                  Long terminalUserId, String externalPaymentIdentifier, String payId, String posSerial, Integer deviceId, Integer isSucceed);

    Order getById(Long orderId);

    DomainPage getUsersBuyProductByOrganizationId(Long organizationId, long pageIndex, long pageSize);

    /**
     * 支付商品订单
     *  @param orderId        订单ID
     * @param paymentType    支付类型
     * @param terminalUserId 收银员ID
     * @param externalPaymentIdentifier
     * @param payId         支付帐号
     * @param deviceId  @return
     * @param isSucceed
     */
    Payment payProductOrder(Long orderId, PaymentTypeEnum paymentType, Long terminalUserId, String externalPaymentIdentifier, String payId, String posSerial, Integer deviceId, Integer isSucceed);



    DomainPage getOrderList(String username, Long terminalUserId, Long pageIndex, Long pageSize);

    Long updateReturn(Long orderId);

    Order sumOrder(Long terminalUserId, String year);

    DomainPage<Order> selectOrderByUid(Long uid, Long siteId, OrderStatusEnum orderStatusEnum, OrderTypeEnum purchaseWay, PayStatusEnum payStatusEnum, Long pageIndex, Long pageSize);

    Order getByOrderIdentifier(String orderIdentifier);

    List<Order> getOrderByPeriodOfTime(Map<String, Object> fieldNameValueMap);

    Boolean updateOrderStatus(Long orderId);

    /**
     * 获取有消耗壹购积分的订单
     *
     * @param uid 订单所属用户
     * @return
     */
    DomainPage<Order> selectOrderByOnePoint(Long uid, long pageIndex, long pageSize);

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    Order cancel(Long orderId);


    /**
     * 一元购下单
     *
     * @param username
     * @param terminalUserId
     *@param siteId
     * @param productId
     * @param amount
     * @param totalPrice
     * @param usePointE
     * @param usePointP
     * @param usePointO
     * @param orderPointPayDetails         @return
     */
    Order insertOnePointOrder(String username, Long terminalUserId, Long siteId, Long productId, Integer amount, Double totalPrice, Integer usePointE, Integer usePointP, Integer usePointO, List<OrderPointPayDetailPojo> orderPointPayDetails);


    /**
     * 按条件查询订单
     *
     * @return
     */
    List<Order> selectOrderByFieldList(Map<String, Object> map);

    /**
     * 获取待付款的订单数
     *
     * @param uid
     * @return
     */
    Integer countObligations(Long uid);

    /**
     * 根据订单编号查询订单
     *
     * @param orderIdentifier 订单编号
     * @return
     */
    Order selectByOrderIdentifier(String orderIdentifier);

    /**
     * 根据订单号查询订单所属商户
     * @param orderId
     * @return
     */
    Organization getOrganizationByOrderId(Long orderId,OrderTypeEnum orderType);

    /**
     * 定时任务获取已确认、支付成功、未清分的订单
     * @return
     */
    List<Order> getIsClearingOrderList();

    /**
     * 修改订单信息
     * @param order
     * @return
     */
    Order updateOrder(Order order);

    void  updateOrderClearingStatus(Long orderId);

    /**
     * 根据条件分页获取订单
     * @param fieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<Order> getOrder(Map<String, Object> fieldNameValueMap,long pageIndex, long pageSize);

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
     * @param flag 1:交易额  2:消耗的E积分  3:消耗的E通币
     * @return
     */
    Long getHasPayOrderOfDayByField(Integer flag);

    /**
     * 查询商户订单明细
     * @param map
     * @param organizationId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Map<String,Object> selectOrganizationOrder(Map<String,Object> map,long organizationId,long pageIndex,long pageSize);

    /**
     * 订单明细
     * @param orderId
     * @return
     */
    Map<String,Object> getOrderDetail(long orderId,long organizationId);

    void givingPoint(Organization organization,Long userId,PointTypeEnum pointType,Integer pointNum);
}
