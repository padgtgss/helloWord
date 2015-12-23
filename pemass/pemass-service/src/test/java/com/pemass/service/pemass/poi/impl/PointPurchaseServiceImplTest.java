package com.pemass.service.pemass.poi.impl;

import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.service.pemass.poi.PointPurchaseService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Date;


@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class PointPurchaseServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private PointPurchaseService pointPurchaseService;

    @Test
    public void testInsert() {


        PointPurchase pointPurchase = new PointPurchase();
        pointPurchase.setOrganizationId(1L);
        pointPurchase.setPointType(PointTypeEnum.P);
        pointPurchase.setAmount(100);
        pointPurchase.setIsAutomatic(1);
        pointPurchase.setPurchaseTime(new Date());

//        pointPurchase.setPointPoolId(1L);
//        pointPurchase.setOrganizationId(6L);
//        pointPurchase.setPointType(PointTypeEnum.E);
//        pointPurchase.setAmount(100);
//        pointPurchase.setArea("00:00:00:00:00");
//        pointPurchase.setIsAutomatic(0);
//        pointPurchase.setRate("3%");
//        Float cha = Float.valueOf((3 / 100) * 100);
//        pointPurchase.setCharge(cha);
//        pointPurchase.setAuditStatus(AuditStatusEnum.NONE_AUDIT);
//        pointPurchase.setPurchaseTime(new Date());
//        //默认过期时间
//        Calendar c = Calendar.getInstance();
//        c.clear();
//        c.set(2215, 8, 1);
//        pointPurchase.setExpiryTime(c.getTime());
//        pointPurchase.setIsClear(0);
        pointPurchase = pointPurchaseService.insert(pointPurchase);
        System.out.println(pointPurchase.getId());

    }

    @Test
    public void testGetPurchaseAmountOfDayByType() {
        Long amount = pointPurchaseService.getPurchaseAmountOfDayByType(PointTypeEnum.P);
        System.out.println("=========当日积分发放数E积分:" + amount);
        amount = pointPurchaseService.getPurchaseAmountOfDayByType(PointTypeEnum.E);
        System.out.println("=========当日积分发放数E通币:" + amount);
    }

    public static void main(String[] args) {

    }

}