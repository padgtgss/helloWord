/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.sys;

import com.pemass.persist.domain.jpa.bas.City;
import com.pemass.persist.domain.jpa.bas.District;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.pojo.sys.OrganizationPojo;
import com.pemass.service.pemass.bas.ProvinceService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: OrganizationController
 * @Author: zhou hang
 * @CreateTime: 2015-04-20 15:18
 */
@Controller
@RequestMapping("/organization")
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;

    @Resource
    private ProvinceService provinceService;

    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/search", method = RequestMethod.GET,params = {"terminalUserId","provinceId"})
    @ResponseBody
    public Object lists(Long terminalUserId,Long provinceId) {
        List<Organization> list = organizationService.getOrganizationByNoIds(terminalUserId,provinceId);
        List<OrganizationPojo> organizationPojos = new ArrayList<OrganizationPojo>();
        for (Organization organization : list) {
            OrganizationPojo organizationPojo = new OrganizationPojo();
            organizationPojo.setId(organization.getId());
            organizationPojo.setOrganizationName(organization.getOrganizationName());
            Province province = provinceService.getProvinceByID(organization.getProvinceId());
            City city = provinceService.getCityById(organization.getCityId());
            District district = provinceService.getDistrictById(organization.getDistrictId());
            organizationPojo.setLocation ( province.getProvinceName() + city.getCityName() + district.getDistrictName() + organization.getLocation());
            organizationPojos.add(organizationPojo);
        }
        return organizationPojos;
    }

}