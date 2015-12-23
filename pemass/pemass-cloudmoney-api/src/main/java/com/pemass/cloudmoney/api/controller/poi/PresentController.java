/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.poi;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.annotation.Auth;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.domain.jpa.poi.Present;
import com.pemass.persist.domain.jpa.poi.PresentItem;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.persist.enumeration.PresentStatusEnum;
import com.pemass.pojo.biz.ProductSnapshotPojo;
import com.pemass.pojo.ec.OrderTicketPojo;
import com.pemass.pojo.poi.PresentItemPojo;
import com.pemass.pojo.poi.PresentPojo;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.ec.OrderTicketService;
import com.pemass.service.pemass.poi.PresentItemService;
import com.pemass.service.pemass.poi.PresentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 红包明细
 * @Author: zhou hang
 * @CreateTime: 2014-11-26 19:25
 */
@Controller
@RequestMapping("/present")
public class PresentController {

    @Resource
    private PresentService presentService;

    @Resource
    private PresentItemService presentItemService;

    @Resource
    private OrderTicketService orderTicketService;

    @Resource
    private ProductSnapshotService productSnapshotService;


    /**
     * 根据用户id,查询不同状态的红包
     *
     * @param uid
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(Long uid, PresentStatusEnum presentStatus, Long pageSize, Long pageIndex) {
        DomainPage domainPage = presentService.selectPack(uid, presentStatus, pageSize, pageIndex);
        /**1,封装数据*/
        List<PresentPojo> presentPojoList = new ArrayList<PresentPojo>();
        for (Object obj : domainPage.getDomains()) {
            Present present = (Present) obj;
            PresentPojo presentPojo = new PresentPojo();
            MergeUtil.merge(present, presentPojo);
            presentPojoList.add(presentPojo);
        }
        domainPage.setDomains(presentPojoList);
        return domainPage;
    }


    /**
     * 根据用户id,查询未拆所用可使用红包数
     *
     * @param uid
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/unpack", method = RequestMethod.GET)
    @ResponseBody
    public Object countUnpack(Long uid) {
        Integer total = presentService.selectPack(uid);
        return ImmutableMap.of("result", total);
    }


    /**
     * 根据红包ID 查看详情
     *
     * @param presentId
     * @return
     */
    @Auth(entity = Present.class, parameter = "presentId")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{presentId}", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@PathVariable("presentId") Long presentId) {
        Present present = presentService.getById(presentId);
        return merge(present);
    }


    /**
     * 用户拆红包
     *
     * @param presentId 红包id
     * @return
     */
    @Auth(entity = Present.class, parameter = "presentId")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{presentId}/unpack", method = RequestMethod.POST)
    @ResponseBody
    public Object unpack(@PathVariable("presentId") Long presentId) {
        return ImmutableMap.of("result", presentService.unpack(presentId) != null);
    }

    /**
     * 用户赠送红包
     *
     * @param username  用户名（赠送手机号）
     * @param presentId 红包id
     * @return
     */
    @Auth(entity = Present.class, parameter = "presentId")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{presentId}/grant", method = RequestMethod.POST)
    @ResponseBody
    public Object grant(@PathVariable("presentId") Long presentId, String username) {
        return ImmutableMap.of("result", presentService.grant(presentId, username));
    }


    /**
     * C端用户送红包给伙伴
     *
     * @param uid           赠送者ID
     * @param presentName   红包名称
     * @param amount        积分数量
     * @param isGeneral     积分是否通用
     * @param orderTicketId 电子票Ids
     * @param username      赠送伙伴的手机号
     * @param instruction   红包说明
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/pack/grant", method = RequestMethod.POST)
    @ResponseBody
    public Object packAndGrant(Long uid, String presentName, Integer amount, Integer isGeneral, Long[] orderTicketId,
                               String username, String instruction) {

        return ImmutableMap.of("result", presentService.packAndGrant(uid, presentName, PointTypeEnum.P, amount, isGeneral, orderTicketId, username, instruction));
    }


    /**
     * 封装数据
     *
     * @param present
     * @return
     */
    private PresentPojo merge(Present present) {
        PresentPojo presentPojo = new PresentPojo();
        MergeUtil.merge(present, presentPojo);

        /**封装红包项**/
        Map param = ImmutableMap.of("presentId", present.getId());
        List<PresentItem> presentItemList = presentItemService.selectPresentItemList(param);
        List<PresentItemPojo> presentItemPojoList = new ArrayList<PresentItemPojo>();
        for (PresentItem presentItem : presentItemList) {
            PresentItemPojo presentItemPojo = new PresentItemPojo();
            MergeUtil.merge(presentItem, presentItemPojo);

            /**封装电子票**/
            if (presentItem.getOrderTicketId() != null) {
                OrderTicket orderTicket = orderTicketService.getOrderTicketById(presentItem.getOrderTicketId());
                OrderTicketPojo orderTicketPojo = new OrderTicketPojo();
                MergeUtil.merge(orderTicket, orderTicketPojo);

                ProductSnapshot productSnapshot = productSnapshotService.getSnapshotById(orderTicket.getProductSnapshotId());
                ProductSnapshotPojo productSnapshotPojo = new ProductSnapshotPojo();
                MergeUtil.merge(productSnapshot, productSnapshotPojo);

                orderTicketPojo.setProductSnapshotPojo(productSnapshotPojo);
                presentItemPojo.setOrderTicketPojo(orderTicketPojo);
            }

            presentItemPojoList.add(presentItemPojo);
        } // end of for

        presentPojo.setPresentItemPojoList(presentItemPojoList);

        return presentPojo;
    }

}