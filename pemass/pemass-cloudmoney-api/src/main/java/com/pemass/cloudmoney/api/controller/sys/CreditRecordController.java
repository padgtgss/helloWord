/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.sys;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.util.FileUtil;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.sys.CreditRecord;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.pojo.sys.CreditRecordPojo;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.sys.CreditRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description: 授信
 * @Author: lin.shi
 * @CreateTime: 2015-08-05 19:45
 */
@Controller
@RequestMapping("creditRecord")
public class CreditRecordController {

    @Resource
    private CreditRecordService creditRecordService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping( method = RequestMethod.POST)
    @ResponseBody
    public Object applyCredit(CreditRecord creditRecord,@RequestParam(value = "legalIdcardFile", required = false) MultipartFile legalIdcardFile,
                            @RequestParam(value = "legalIdcardBackFile", required = false) MultipartFile legalIdcardBackFile,
                            @RequestParam(value = "faceFile", required = false) MultipartFile faceFile) throws Exception{

        if (legalIdcardFile != null && !legalIdcardFile.isEmpty() && legalIdcardFile.getSize() != 0) {
            String path = FileUtil.saveFile(legalIdcardFile.getInputStream(), FileUtil.fetchExtension(legalIdcardFile.getOriginalFilename()));
            creditRecord.setLegalIdcardUrl(path);
        }
        if (legalIdcardBackFile != null && !legalIdcardBackFile.isEmpty() && legalIdcardBackFile.getSize() != 0) {
            String path = FileUtil.saveFile(legalIdcardBackFile.getInputStream(), FileUtil.fetchExtension(legalIdcardBackFile.getOriginalFilename()));
            creditRecord.setLegalIdcardBackUrl(path);
        }
        if (faceFile != null && !faceFile.isEmpty() && faceFile.getSize() != 0) {
            String path = FileUtil.saveFile(faceFile.getInputStream(), FileUtil.fetchExtension(faceFile.getOriginalFilename()));
            creditRecord.setFaceUrl(path);
        }
        creditRecordService.applyCredit(creditRecord);
        return ImmutableMap.of("result",true);
    }

    /**
     *用户授信详情
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/user/{uid}",method = RequestMethod.GET)
    @ResponseBody
    public Object getCreditRecord(@PathVariable("uid")Long uid){
        CreditRecord creditRecord = creditRecordService.getByUserId(uid);
        if(creditRecord == null){
            creditRecord = new CreditRecord();
            creditRecord.setAuditStatus(AuditStatusEnum.NONE_AUDIT);
        }
        return MergeUtil.merge(creditRecord,new CreditRecordPojo());
    }

}