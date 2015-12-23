package com.pemass.service.pemass.sys.impl;

import com.pemass.persist.domain.jpa.sys.BankCard;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.service.pemass.sys.BankCardService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.apache.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;


/**
 * Created by vahoa on 15/9/23.
 */
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class BankCardServiceImplTest extends AbstractTestNGSpringContextTests {
    private static final Logger logger = Logger.getLogger(BankCardServiceImplTest.class.getName());
    @Resource
    private BankCardService bankCardService;

    @Resource
    private OrganizationService organizationService;


    @Test
    public void testSaveBankCard() throws Exception {
        BankCard bankCard = new BankCard();
        Organization organization = organizationService.getOrganizationById(2L);
        bankCard.setCardholderName("test_bank_card");
        bankCard.setCertType("1");
        bankCard.setCertNo("51168668888X");
        bankCard.setIsPublicAccount(0);
        bankCard.setCardNo("5116866818181818");
        bankCard.setProvinceId(1L);
        bankCard.setCityId(1L);
        bankCard.setTargetUUID(organization.getUuid());
        bankCard.setCpAuditStatus(AuditStatusEnum.ING_AUDIT);
        bankCardService.saveBankCard("邮储银行,0100",bankCard);

        bankCardService.getBankCardTestInfo(bankCard,organization);
    }
}