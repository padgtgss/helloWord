package com.pemass.service.pemass.ec.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.dao.ec.OrderDao;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.service.pemass.ec.OrderItemService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContextConfiguration(locations = {"classpath*:applicationContext.xml","classpath*:applicationContext-persist.xml","classpath*:applicationContext-jms.xml"})
public class OrderItemServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private OrderDao orderDao;

    @Test
    public void testGetAllOrder() throws Exception {
        /*DomainPage domainPage = new DomainPage(10,1,0);
        domainPage = orderItemService.getAllOrder(domainPage.getPageIndex(),domainPage.getPageSize());
        System.out.println(domainPage.getDomains().size()+"--------------");*/
    }
    @Test
    public void testSelectThisMonthSalesVolume() throws Exception {
//        Long organizationId = 4l;
//        List result = orderItemService.selectThisMonthSalesVolume(organizationId);
//        System.out.println(result.size()+"-------长度");
        Map<String,Object> map = new HashMap<String, Object>();
        Map<String,Object> map2 = new HashMap<String, Object>();
        Map<String,Object> map3 = new HashMap<String, Object>();
        orderDao.scenicSpotOrders(OrderItem.class,2L,map,map2,map3,1L,10L);
    }

}