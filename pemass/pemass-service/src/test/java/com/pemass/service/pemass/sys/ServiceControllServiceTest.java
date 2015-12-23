package com.pemass.service.pemass.sys;

import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.domain.jpa.sys.ServiceControll;
import com.pemass.persist.enumeration.AppTypeEnum;
import junit.framework.TestCase;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class ServiceControllServiceTest extends TestCase {


    @Resource
    private ServiceControllService serviceControllService;

    public void testInsert() throws Exception {

    }

    public void testUpdate() throws Exception {

    }

    public void testDelete() throws Exception {

    }

    public void testSelectAll() throws Exception {

    }

    @Test
    public void testGrant() throws Exception {
//        String appid, List<ServiceControll > serviceControllList

        String appid = "17a9f6f0a7154a48b2664dbbb7193069";
        List<ServiceControll> serviceControllList = new ArrayList<ServiceControll>();
        ServiceControll serviceControll = new ServiceControll();
        serviceControll.setAppType(AppTypeEnum.C);
        serviceControll.setUrl("GET/ticket/search");
        serviceControll.setServicename("C端电子票列表");
        serviceControll.setAvailable(AvailableEnum.AVAILABLE);
        serviceControll.setId(1L);
        serviceControllList.add(serviceControll);
    //    serviceControllService.grant(appid,serviceControllList);
    }

    public void testHasAuth() throws Exception {

    }
}