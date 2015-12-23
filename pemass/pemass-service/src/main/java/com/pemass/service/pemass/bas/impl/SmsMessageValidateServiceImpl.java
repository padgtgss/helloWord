package com.pemass.service.pemass.bas.impl;

import com.pemass.persist.dao.bas.SmsMessageValidateDao;
import com.pemass.service.pemass.bas.SmsMessageValidateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: SmsMessageValidateServiceImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-08-27 16:29
 */
@Service
public class SmsMessageValidateServiceImpl implements SmsMessageValidateService {

    @Resource
    private SmsMessageValidateDao smsMessageValidateDao;

    @Override
    public boolean validateSms(String telephone) {
        return smsMessageValidateDao.validateSms(telephone);
    }
}

