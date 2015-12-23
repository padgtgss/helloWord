/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.poi;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.domain.jpa.poi.Present;
import com.pemass.persist.domain.jpa.poi.PresentItem;
import com.pemass.pojo.biz.ProductSnapshotPojo;
import com.pemass.pojo.ec.OrderTicketPojo;
import com.pemass.pojo.poi.PresentItemPojo;
import com.pemass.pojo.poi.PresentPojo;
import com.pemass.pojo.poi.PresentSquarePojo;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.ec.OrderTicketService;
import com.pemass.service.pemass.poi.PresentItemService;
import com.pemass.service.pemass.poi.PresentSquareService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 红包广场
 * @Author: zhou hang
 * @CreateTime: 2014-12-10 10:16
 */
@Controller
@RequestMapping("/presentSquare")
public class PresentSquareController {

    @Resource
    private PresentSquareService presentSquareService;

    @Resource
    private PresentItemService presentItemService;

    @Resource
    private OrderTicketService orderTicketService;

    @Resource
    private ProductSnapshotService productSnapshotService;

    /**
     * 查询商家发放到 红包广场的所有策略类型红包
     *
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(Long uid, long pageSize, long pageIndex) {
        DomainPage domainPage = presentSquareService.selectAllPresentSquare(pageIndex, pageSize);
        List list = new ArrayList();
        if (domainPage.getDomains().size() > 0) {
            for (Object obj : domainPage.getDomains()) {
                Object[] temp = (Object[]) obj;
                PresentSquarePojo pojo = new PresentSquarePojo();
                pojo.setSourceName(temp[0].toString());
                pojo.setStrategyName(temp[1].toString());
                pojo.setExpiryTime((Date) temp[2]);
                pojo.setIssueStrategyId(Long.valueOf(temp[4].toString()));
                pojo.setTotal(presentSquareService.getTotalByIssueStrategyId(pojo.getIssueStrategyId()));
                pojo.setStatus(presentSquareService.hasGrabPresent(uid, pojo.getIssueStrategyId()));
                if (temp[5] != null) {
                    pojo.setLogo(temp[5].toString());
                }
                list.add(pojo);
            }
            domainPage.setDomains(list);
        }

        return domainPage;
    }

    /**
     * 用户抢红包操作
     *
     * @param uid             用户id
     * @param issueStrategyId 策略id
     * @return
     */
    @RequestMapping(value = "/grab", method = RequestMethod.POST)
    @ResponseBody
    public Object grab(Long uid, Long issueStrategyId) {
        Present present = presentSquareService.grabPresent(uid, issueStrategyId);
        return ImmutableMap.of("result", present != null);
    }

    /**
     * 用户抢红包操作
     *
     * @param uid             用户id
     * @param issueStrategyId 策略id
     * @return
     */
    @RequestMapping(value = "/grab/unpack", method = RequestMethod.POST)
    @ResponseBody
    public Object grabAndUnpack(Long uid, Long issueStrategyId) {
        return merge(presentSquareService.grabAndUnpack(uid, issueStrategyId));
    }

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