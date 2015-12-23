/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.bas;

import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.pojo.bas.ProvincePojo;
import com.pemass.service.pemass.bas.ProvinceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ProvinceController
 * @Author: zhou hang
 * @CreateTime: 2015-05-29 10:05
 */
@Controller
@RequestMapping("/province")
public class ProvinceController {

    @Resource
    private ProvinceService provinceService;

    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object lists() {
        List<Province> list = provinceService.getProvinceList();
        List<ProvincePojo> pojos = new ArrayList<ProvincePojo>();
        for (Province province : list) {
            ProvincePojo provincePojo = new ProvincePojo();
            provincePojo.setId(province.getId());
            provincePojo.setProvinceName(province.getProvinceName());
            pojos.add(provincePojo);
        }
        return pojos;
    }

}