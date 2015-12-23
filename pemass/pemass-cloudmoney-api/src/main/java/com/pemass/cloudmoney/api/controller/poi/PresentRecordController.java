/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.pojo.poi.PresentRecordPojo;
import com.pemass.service.pemass.poi.PresentRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 赠送红包记录
 * @Author: zhou hang
 * @CreateTime: 2015-01-07 14:29
 */
@Controller
@RequestMapping("/present/record")
public class PresentRecordController {

    @Resource
    private PresentRecordService presentRecordService;

    /**
     * 赠送记录
     *
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(Long uid, long pageIndex, long pageSize) {
        DomainPage domainPage = presentRecordService.selectByFromUserId(uid, pageIndex, pageSize);
        domainPage.setDomains(merge(domainPage.getDomains()));
        return domainPage;
    }


    /**
     * 封装对象
     *
     * @param objects
     * @return
     */
    private List<PresentRecordPojo> merge(List<Object[]> objects) {
        List<PresentRecordPojo> recordPojoList = new ArrayList<PresentRecordPojo>();
        for (Object[] obj : objects) {
            PresentRecordPojo recordPojo = new PresentRecordPojo();
            recordPojo.setPresentId(Long.valueOf(obj[0].toString()));
            recordPojo.setGivingTime((java.util.Date) obj[1]);
            recordPojo.setPresentName(obj[2].toString());
            recordPojo.setToUsername(obj[3].toString());
            recordPojoList.add(recordPojo);
        }
        return recordPojoList;
    }


}