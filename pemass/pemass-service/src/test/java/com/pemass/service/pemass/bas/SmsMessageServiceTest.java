package com.pemass.service.pemass.bas;

import com.pemass.persist.enumeration.SmsTypeEnum;
import junit.framework.TestCase;

import javax.annotation.Resource;

public class SmsMessageServiceTest extends TestCase {


    @Resource
    private SmsMessageService smsMessageService;

    public void testInsert() throws Exception {

    }

    public void testSmsSend() throws Exception {

    }

    public void testAppend() throws Exception {
        smsMessageService.append("18008068200", SmsTypeEnum.ORDER_SUCCESS_AF, null);
    }

    public void testValidateCode() throws Exception {

    }

    public void testValidateCode1() throws Exception {

    }

    public void testValidateCodes() throws Exception {

    }

    public void testUpdate() throws Exception {

    }
}