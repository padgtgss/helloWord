/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.biz;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.util.FileUtil;
import com.pemass.persist.domain.jpa.biz.ExpresspayCardRecord;
import com.pemass.persist.domain.jpa.ec.Invoice;
import com.pemass.service.pemass.biz.ExpresspayCardRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description: ExpresspayCardRecordControoler
 * @Author: zhou hang
 * @CreateTime: 2015-04-17 11:36
 */
@Controller
@RequestMapping("expresspayCard/record")
public class ExpresspayCardRecordController {

    @Resource
    private ExpresspayCardRecordService expresspayCardRecordService;

    /**
     * 销售卡
     *
     * @param cardNumber             卡号
     * @param terminalUserId         收银员id
     * @param expresspayCardRecord   记录卡对象
     * @param invoice                发票对象
     * @param legalIdcardUrlFile     法人身份证正面URL
     * @param legalIdcardUrlBackFile 法人身份证背面URL
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object add(String cardNumber, Long terminalUserId, ExpresspayCardRecord expresspayCardRecord, Invoice invoice,
                      @RequestParam(value = "legalIdcardUrlFile", required = false) MultipartFile legalIdcardUrlFile,
                      @RequestParam(value = "legalIdcardUrlBackFile", required = false) MultipartFile legalIdcardUrlBackFile) throws Exception {

        if (legalIdcardUrlFile != null) {
            String legalIdcardUrl = FileUtil.saveFile(legalIdcardUrlFile.getInputStream(), FileUtil.fetchExtension(legalIdcardUrlFile.getOriginalFilename()));
            expresspayCardRecord.setLegalIdcardUrl(legalIdcardUrl);
        }
        if (legalIdcardUrlBackFile != null) {
            String legalIdcardUrlBack = FileUtil.saveFile(legalIdcardUrlBackFile.getInputStream(), FileUtil.fetchExtension(legalIdcardUrlBackFile.getOriginalFilename()));
            expresspayCardRecord.setLegalIdcardUrlBack(legalIdcardUrlBack);
        }
        return ImmutableMap.of("result", expresspayCardRecordService.add(expresspayCardRecord, invoice, cardNumber, terminalUserId));
    }


}