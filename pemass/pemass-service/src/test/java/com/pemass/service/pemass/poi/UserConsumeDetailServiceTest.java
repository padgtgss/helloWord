package com.pemass.service.pemass.poi;

import com.pemass.persist.enumeration.ConsumeTypeEnum;
import common.AbstractPemassServiceTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * Created by estn on 15/7/23.
 */
public class UserConsumeDetailServiceTest extends AbstractPemassServiceTest {

    @Resource
    private UserConsumeDetailService userConsumeDetailService;

    @Test
    public void testSaveByOrder() throws Exception {

    }

    @Test
    public void testUpdateAmount() throws Exception {
        Boolean result = userConsumeDetailService.updateAmount(ConsumeTypeEnum.ORDER, 1L);
        Assert.assertEquals(result, Boolean.TRUE);

    }

    @Test
    public void testGetPointConsumeAmountByPointPoolId() {
        Integer amount = userConsumeDetailService.getPointConsumeAmountByPointPoolId(1L);
        System.out.println();
        System.out.println(amount + "----------");
    }
}