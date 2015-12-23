/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * @Description: OrderItemDaoTest
 * @Author: estn.zuo
 * @CreateTime: 2014-12-22 11:32
 */
@ContextConfiguration(locations = {"classpath*:applicationContext-persist.xml"})
public class OrderItemDaoTest extends AbstractTestNGSpringContextTests {


    @Resource
    private OrderItemDao orderItemDao;
    @Test
    public void testSelectOrderItemWithFieldsByOrderId() throws Exception {

    }

    @Test
    public void testSelectUnusedOrderItemByOrderId() throws Exception {
//        List<OrderItem> orderItems = orderItemDao.selectOrderItemByOrderId(9L,null);
//        System.out.println(orderItems.size());
    }

    @Test
    public void testSelectWillOverdueTicket() {
//        orderItemDao.selectWillOverdueTicket(1L);
    }
}
