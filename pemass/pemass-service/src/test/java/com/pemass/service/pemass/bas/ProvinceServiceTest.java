package com.pemass.service.pemass.bas;
/*
* Copyright  2014  Pemass
* All Right Reserved.
*/

import common.AbstractPemassServiceTest;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * @Description: ProvinceServiceTest
 * @Author: estn.zuo
 * @CreateTime: 2014-10-15 18:00
 */
public class ProvinceServiceTest extends AbstractPemassServiceTest {

    @Resource
    private ProvinceService provinceService;

    @Test
    public void testGetProvinceByName() throws Exception {
        System.out.println();
    }

    @Test
    public void testGetProvinceList() throws Exception {
        provinceService.getProvinceList();
    }

    @Test
    public void testGetCityListByProcince() throws Exception {

    }

    @Test
    public void testGetCityListByDistrict() throws Exception {

    }

    public static void main(String[] args) {
        //System.out.println(MD5Util.encrypt("123456", "12345678"));
    }
}
