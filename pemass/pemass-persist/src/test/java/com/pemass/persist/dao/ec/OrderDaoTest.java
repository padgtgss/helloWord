/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec;

import com.pemass.persist.enumeration.OrderItemStatusEnum;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: OrderDaoTest
 * @Author: estn.zuo
 * @CreateTime: 2014-12-22 11:08
 */
@ContextConfiguration(locations = {"classpath*:applicationContext-persist.xml"})
public class OrderDaoTest extends AbstractTestNGSpringContextTests {

    @Resource
    private OrderDao orderDao;

    @Test
    public void testGetMaxid() throws Exception {

    }

    @Test
    public void testUpdateProductSaleNumber() throws Exception {

    }

    @Test
    public void testGetpointPurchaseList() throws Exception {

    }

    @Test
    public void testGetVersion() throws Exception {

    }

    @Test
    public void testUpdateProductSaleNumberReturn() throws Exception {

    }

    @Test
    public void testGetOrderList() throws Exception {

    }

    @Test
    public void testGetAllOrdersByPage() throws Exception {

    }

    @Test
    public void testGetUsersBuyProductByOrganizationId() throws Exception {

    }

    @Test
    public void testSelectUnusedOrder() throws Exception {
        //List<OrderItemStatusEnum> statusEnums = new ArrayList<OrderItemStatusEnum>();
        //statusEnums.add(OrderItemStatusEnum.UNUSED);
        //
        //orderDao.selectOrder(1L, statusEnums, 1L, 3l);
    }
}
