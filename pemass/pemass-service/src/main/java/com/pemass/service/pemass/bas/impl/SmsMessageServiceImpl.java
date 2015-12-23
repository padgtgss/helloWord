/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.bas.impl;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.persist.dao.bas.SmsMessageDao;
import com.pemass.persist.domain.jpa.sms.SmsMessage;
import com.pemass.persist.domain.vo.SmsMessageVo;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.exception.SmsError;
import com.pemass.service.exception.SysError;
import com.pemass.service.jms.Producer;
import com.pemass.service.pemass.bas.SmsMessageService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description: SmsMessageServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2015-01-20 13:42
 */
@Service
public class SmsMessageServiceImpl implements SmsMessageService {

    @Resource
    private Producer smsProducer;

    @Resource
    private SmsMessageDao smsMessageDao;


    @Override
    public SmsMessage insert(SmsMessageVo smsMessageVo) {
        SmsMessage smsMessage = smsSendValidateCode(smsMessageVo);

        if (smsMessageVo.getSmsType().toString().contains("MAIL")) {
            /**邮件默认成功**/
            smsMessage.setIsSuccess(1);
            //生成过期时间
            DateTime dateTime = new DateTime();
            smsMessage.setExpiryTime(dateTime.plusMinutes(30).toDate());//邮箱有效期为30分钟
            //保存验证码
            if (null != smsMessageVo.getList()) {
                String validateCode = (String) smsMessageVo.getList().get(0);
                smsMessage.setValidateCode(validateCode);
            }
        } else {
            String template = PemassConst.SMS_PROPERTIES.getProperty(smsMessageVo.getSmsType().toString());//读取短信模板

            String[] array = template.split("=");//拆分
            MessageFormat messageFormat = new MessageFormat(array[0]);//模板未合并内容
            String content;
            if (null == array) {
                content = template;
            }
            if (array.length == 3) {
                smsMessage.setExpiryTime(addDate(Integer.valueOf(array[1])));
                smsMessage.setValidateCode(UUIDUtil.randomNumber(Integer.valueOf(array[2]))); //验证码位数
                content = messageFormat.format(analyze(smsMessageVo.getList(), smsMessage.getValidateCode()));//合并之后 短信内容
            } else {
                if (null != smsMessageVo.getList()) {
                    Object[] arrayList = smsMessageVo.getList().toArray();
                    content = messageFormat.format(arrayList);//合并之后 短信内容
                } else {
                    content = array[0];
                }
            }

//            String result = smsService.sendSms(smsMessage.getReceiveNumber(), content);
            smsMessage.setIsSuccess(0);
//            smsMessage.setOriginalData(result);
            smsMessage.setContent(content);
            /**邮件默认成功 或 短信发送成功**/

//            if (result.contains("<code>2</code>")) {
//                smsMessage.setIsSuccess(1);
//            }
        }
        smsMessage.setIsUsed(0);
        smsMessageDao.insertEntity(smsMessage);

        return smsMessage;
    }


    private SmsMessage smsSendValidateCode(SmsMessageVo smsMessageVo) {

        SmsMessage smsMessage = new SmsMessage();

        smsMessage.setReceiveNumber(smsMessageVo.getReceiveNumber());
        smsMessage.setSmsType(smsMessageVo.getSmsType());
        smsMessage.setUuid(smsMessageVo.getUuid()); //uuid

        return smsMessage;
    }


    @Override
    public String smsSend(SmsMessageVo smsMessageVo) {
        if (smsMessageVo == null) {
            throw new BaseBusinessException(SmsError.SMSMESSAGE_IS_NULL);
        }
        if (null == smsMessageVo.getReceiveNumber() || "".equals(smsMessageVo.getReceiveNumber())) {
            throw new BaseBusinessException(SmsError.SMSMESSAGE_RECEIVENUMBER_IS_NULL);
        }
        if (null == smsMessageVo.getSmsType()) {
            throw new BaseBusinessException(SmsError.SMSMESSAGE_SMSTYPE_IS_NULL);
        }
        smsMessageVo.setUuid(UUIDUtil.randomWithoutBar());
        smsProducer.send(smsMessageVo);
        return smsMessageVo.getUuid();
    }


    /**
     * 追加过期时间
     *
     * @param number 追加的分钟数
     * @return
     */
    private Date addDate(Integer number) {
        if (number == null) {
            throw new BaseBusinessException(SmsError.SMSMESSAGE_EXPIRYTIME_IS_NULL);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, number);
        return calendar.getTime();
    }

    /**
     * 分解参数
     *
     * @param variable
     * @return
     */
    private static Object[] analyze(List<Object> list, String variable) {
        if (null != list && list.size() > 0) {
            list.add(0, variable);
            Object[] array = list.toArray();
            return array;
        }
        return new String[]{variable};
    }


    public void append(String telephone, SmsTypeEnum smsTypeEnum, Object[] objects) {

        SmsMessageVo smsMessageVo = new SmsMessageVo();
        smsMessageVo.setReceiveNumber(telephone);
        smsMessageVo.setSmsType(smsTypeEnum);
        List<Object> list = new ArrayList<Object>();
        if (null != objects) {
            for (int i = 0; i < objects.length; i++) {
                list.add(objects[i]);
            }
            smsMessageVo.setList(list);
        }
        smsSend(smsMessageVo);
    }

    @Transactional
    @Override
    public boolean validateCode(String username, SmsTypeEnum regVal, String validateCode) {
        SmsMessage smsMessage = smsMessageDao.findEntity(username, regVal, validateCode);
        if (null == smsMessage) {
            throw new BaseBusinessException(SysError.VALIDATE_CODE_ERROR);
        }
        smsMessage.setIsUsed(1);
        smsMessageDao.merge(smsMessage);
        return true;
    }

    @Transactional
    @Override
    public boolean validateCode(String username, SmsTypeEnum customOrderNoreg, SmsTypeEnum customOrderReged, String validateCode) {
        SmsMessage smsMessage = smsMessageDao.findEntity(username, customOrderNoreg, customOrderReged, validateCode);
        if (null == smsMessage) {
            throw new BaseBusinessException(SysError.VALIDATE_CODE_ERROR);
        }
        smsMessage.setIsUsed(1);
        smsMessageDao.merge(smsMessage);
        return true;
    }

    @Transactional
    @Override
    public boolean validateCodes(String username, SmsTypeEnum customOrderNoreg, SmsTypeEnum customOrderReged, String validateCode) {
        SmsMessage smsMessage = smsMessageDao.findEntity(username, customOrderNoreg, customOrderReged, validateCode);
        if (null == smsMessage) {
            return false;
        }
        smsMessage.setIsUsed(1);
        smsMessageDao.merge(smsMessage);
        return true;
    }

    @Transactional
    @Override
    public boolean validateCodes(String username, SmsTypeEnum customOrderNoreg, String validateCode) {
        SmsMessage smsMessage = smsMessageDao.findEntity(username, customOrderNoreg, validateCode);
        if (null == smsMessage) {
            return false;
        }
        smsMessage.setIsUsed(1);
        smsMessageDao.merge(smsMessage);
        return true;
    }

    @Transactional
    @Override
    public boolean update(SmsMessage smsMessage) {
        smsMessageDao.merge(smsMessage);
        return true;
    }

}