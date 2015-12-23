/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.poi;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.poi.OnePointDetail;
import com.pemass.pojo.poi.OnePointDetailPojo;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.ec.OrderItemService;
import com.pemass.service.pemass.poi.OnePointDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 一圆购积分明细
 * @Author: lin.shi
 * @CreateTime: 2015-06-11 21:14
 */
@Controller
@RequestMapping("/onePointDetail")
public class OnePointDetailController {
    @Resource
    private OnePointDetailService onePointDetailService;

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private ProductService productService;


    /**
     * 查询购买一元购商品列表
     *
     * @param uid
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/search/{uid}", method = RequestMethod.GET,params = {"pageSize","pageIndex"})
    @ResponseBody
    public Object pointDetail(@PathVariable("uid") Long uid, Long pageSize, Long pageIndex) {
        DomainPage domainPage = onePointDetailService.getPointDetail(uid, pageIndex, pageSize);
        List<OnePointDetail> list = domainPage.getDomains();
        List<OnePointDetailPojo> detailPojos = new ArrayList<OnePointDetailPojo>();
        for (OnePointDetail detail : list) {
            List<OrderItem> orderItemList = orderItemService.selectByOrderId(detail.getOrderId());
            Product product=productService.getProductInfo(orderItemList.get(0).getProductSnapshotId());
            OnePointDetailPojo detailPojo = new OnePointDetailPojo();
            detailPojo.setId(detail.getId());
            detailPojo.setAmount(detail.getAmount());
            detailPojo.setCreateTime(detail.getCreateTime());
            detailPojo.setExpiryTime(detail.getExpiryTime());
            detailPojo.setProductName(product.getProductName());
            detailPojos.add(detailPojo);
        }
        domainPage.setDomains(detailPojos);
        return domainPage;
    }


}