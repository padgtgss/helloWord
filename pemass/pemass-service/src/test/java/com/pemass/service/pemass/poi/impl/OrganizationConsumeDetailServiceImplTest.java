package com.pemass.service.pemass.poi.impl;

import com.pemass.persist.domain.jpa.poi.OrganizationConsumeDetail;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.service.pemass.poi.OrganizationConsumeDetailService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2015/7/17.
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class OrganizationConsumeDetailServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private OrganizationConsumeDetailService organizationConsumeDetailService;

    @Test
    public void testInsert() throws Exception {
        OrganizationConsumeDetail organizationConsumeDetail = new OrganizationConsumeDetail();
        organizationConsumeDetail.setAmount(50);
        organizationConsumeDetail.setConsumeType(ConsumeTypeEnum.PURCHASE_GOODS_GIFT);
        organizationConsumeDetail.setConsumeTargetId(1L);
        organizationConsumeDetail.setPointPurchaseId(5L);
        organizationConsumeDetail.setPointType(PointTypeEnum.E);
        organizationConsumeDetail = organizationConsumeDetailService.insert(organizationConsumeDetail);
    }

    @Test
    public void testGetUnsuccessfulConsumeAmountByPointPoolId() {
        Integer amount = organizationConsumeDetailService.getUnsuccessfulConsumeAmountByPointPoolId(1L);
        System.out.println();
        System.out.println(amount + "---++++++++++");

    }

    @Test
    public void testGetCountConsumeAmountByPresentPackId() {
        Integer amount = organizationConsumeDetailService.getCountConsumeAmountByPresentPackId(17L, ConsumeTypeEnum.PRESENT);
        System.out.println("------------商户的积分消耗条数"+amount);
    }
}