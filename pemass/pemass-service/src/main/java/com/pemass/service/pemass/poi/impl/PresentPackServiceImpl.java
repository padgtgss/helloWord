/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.poi.*;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.vo.PresentPackVO;
import com.pemass.persist.domain.vo.PresentPointVO;
import com.pemass.persist.enumeration.*;
import com.pemass.service.jms.Producer;
import com.pemass.service.pemass.poi.OrganizationConsumeDetailService;
import com.pemass.service.pemass.poi.PointPurchaseService;
import com.pemass.service.pemass.poi.PresentPackService;
import com.pemass.service.pemass.poi.PresentService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @Description: PresentPackServiceImpl
 * @Author: he jun cheng
 * @CreateTime: 2014-10-13 10:35
 */
@Service
public class PresentPackServiceImpl implements PresentPackService {

    @Resource
    private PresentPackDao presentPackDao;

    @Resource
    private PresentService presentService;

    @Resource
    private PointPurchaseService pointPurchaseService;

    @Resource
    private Producer issuePackProducer;

    @Resource
    private OrganizationConsumeDetailService organizationConsumeDetailService;

    @Override
    public void savePresentPack(PresentPack presentPack) {
        Preconditions.checkNotNull(presentPack);
        presentPackDao.persist(presentPack);
    }

    @Override
    public PresentPack updatePresentPack(PresentPack source) {
        Preconditions.checkNotNull(source);
        PresentPack targetPresentPack = selectPresentPackById(source.getId());
        MergeUtil.merge(source, targetPresentPack);
        return presentPackDao.merge(targetPresentPack);
    }

    @Override
    public PresentPack selectPresentPackById(Long presentPackId) {
        Preconditions.checkNotNull(presentPackId);
        return presentPackDao.getEntityById(PresentPack.class, presentPackId);
    }

    @Override
    public PresentPack deletePresentPackById(Long presentPackId) {
        Preconditions.checkNotNull(presentPackId);
        PresentPack targetPresentPack = selectPresentPackById(presentPackId);
        targetPresentPack.setAvailable(AvailableEnum.UNAVAILABLE);
        targetPresentPack.setUpdateTime(new Date());

        return presentPackDao.merge(targetPresentPack);
    }

    @Override
    public void addPresentPack(PresentPackVO presentPackVO) {
        checkNotNull(presentPackVO);
        /** 发红包前扣除相应总积分 **/
        deductPoint(presentPackVO);
        /** 利用JMS发行红包 **/
        issuePackProducer.send(presentPackVO);
    }

    /**
     * 扣除发行红包需要消耗的积分
     *
     * @param presentPackVO 发行红包的数据
     */
    @SuppressWarnings("all")
    private void deductPoint(PresentPackVO presentPackVO) {
        // 红包类型
        PresentPackTypeEnum presentPackType = presentPackVO.getPresentPackType();
        // 红包数量
        int packTotalAmount = presentPackVO.getPackTotalAmount();
        List<PresentPointVO> pointVOs = presentPackVO.getPresentPoints();

        for (PresentPointVO pointVO : pointVOs) {
            // 积分批次ID
            Long pointPurchaseId = pointVO.getPointPurchaseId();
            if (pointPurchaseId == null) continue; // 由于前台页面的传值问题,故作此特殊处理

            // 该批次红包消耗该批次积分总数
            int pointTotalAmount = 0;
            if (PresentPackTypeEnum.GENERAL.equals(presentPackType)) {
                int amount = pointVO.getAmount();
                pointTotalAmount = packTotalAmount * amount;
            } else if (PresentPackTypeEnum.LUCK.equals(presentPackType)) {
                int totalAmount = pointVO.getTotalAmount();
                pointTotalAmount = totalAmount;
            }
            // 更新该批次积分的信息
            updatePointPurchase(pointPurchaseId, pointTotalAmount);
        }
    }

    /**
     * 更新商户的积分认购记录
     *
     * @param pointPurchaseId 认购记录
     * @param amount          减少数量
     */
    private void updatePointPurchase(Long pointPurchaseId, Integer amount) {
        Preconditions.checkNotNull(pointPurchaseId);
        Preconditions.checkNotNull(amount);

        OrganizationPointDetail organizationPointDetail = presentPackDao.getEntityByField(OrganizationPointDetail.class, "pointPurchaseId", pointPurchaseId);
        organizationPointDetail.setUseableAmount(organizationPointDetail.getUseableAmount() - amount);
        organizationPointDetail.setUpdateTime(DateTime.now().toDate());
        presentPackDao.merge(organizationPointDetail);
    }

    @Override
    public DomainPage getPackRecord(Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        return presentPackDao.getPresentPackByPages(fuzzyFieldNameValueMap, pageIndex, pageSize);
    }

    @Override
    public DomainPage getPackIssueRecord(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, DomainPage domainPage) {
        return presentPackDao.getEntitiesPagesByFieldList(PresentPack.class, fieldNameValueMap, fuzzyFieldNameValueMap, domainPage.getPageIndex(), domainPage.getPageSize());
    }

    @Override
    public DomainPage getPackRecordAndAmount(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage) {
        DomainPage searchDomainPage = presentPackDao.getValidPack(domainPage, conditions);

        @SuppressWarnings("unchecked") List<PresentPack> presentPacks = searchDomainPage.getDomains();
        List<Map<String, Object>> newPresentPacks = Lists.newArrayList();
        for (PresentPack presentPack : presentPacks) {
            Map<String, Object> newPresentPack = Maps.newHashMap();
            newPresentPack.put("presentPack", presentPack);
            // 未发放红包数(可用)
            newPresentPack.put("noneIssuePresent", getPresentPackAmount(presentPack.getId(), PresentStatusEnum.NONE_ISSUE));
            // 红包广场红包数
            newPresentPack.put("presentSquarePresent", getPresentPackAmount(presentPack.getId(), PresentStatusEnum.PRESENT_SQUARE));
            // 已发放红包数
            newPresentPack.put("hasIssuePresent", getPresentPackAmount(presentPack.getId(), PresentStatusEnum.HAS_ISSUE));
            // 已领取红包数
            newPresentPack.put("hasReceivePresent", getPresentPackAmount(presentPack.getId(), PresentStatusEnum.HAS_RECEIVE));
            // 红包内容
            newPresentPack.put("presentContent", getPresentContent(presentPack));
            newPresentPacks.add(newPresentPack);
        }

        //替换返回结果并返回
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(searchDomainPage.getPageSize(), searchDomainPage.getPageIndex(), searchDomainPage.getTotalCount());
        returnDomainPage.setDomains(newPresentPacks);
        return returnDomainPage;
    }

    @Override
    public DomainPage getPointPurchases(Map<String, Object> conditions, DomainPage domainPage) {
        return pointPurchaseService.getValidPointPurchase(conditions, domainPage);
    }

    @Override
    public List getPointPurchases(Map<String, Object> conditions) {
        return pointPurchaseService.getValidPointPurchaseList(conditions);
    }

    @Override
    public PresentPack getPackDetailById(Long pid) {
        return presentPackDao.getEntityById(PresentPack.class, pid);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DomainPage getPresentRecord(Long presentPackId, DomainPage domainPage) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("presentPackId", presentPackId);
        domainPage = presentPackDao.getEntitiesPagesByFieldList(Present.class, param, "userId", BaseDao.OrderBy.DESC, domainPage.getPageIndex(), domainPage.getPageSize());
        List<Map<String, Object>> newPresents = getNewPresentList(domainPage);
        domainPage.setDomains(newPresents);
        return domainPage;
    }

    /**
     * 完善红包详情信息
     *
     * @param domainPage 分业信息
     * @return 返回结果
     */
    private List<Map<String, Object>> getNewPresentList(DomainPage domainPage) {
        @SuppressWarnings("unchecked") List<Present> presents = domainPage.getDomains();
        List<Map<String, Object>> newPresents = Lists.newArrayList();
        for (Present present : presents) {
            Map<String, Object> newPresent = Maps.newHashMap();
            newPresent.put("present", present);

            List<PresentItem> presentItems = presentPackDao.getEntitiesByField(PresentItem.class, "presentId", present.getId());
            List<Map<String, Object>> newPresentItems = Lists.newArrayList();
            for (PresentItem presentItem : presentItems) {
                Map<String, Object> newPresentItem = Maps.newHashMap();
                newPresentItem.put("presentItem", presentItem);
                newPresentItems.add(newPresentItem);
            }
            newPresent.put("presentItems", newPresentItems);

            if (present.getUserId() != null) {
                newPresent.put("user", presentPackDao.getEntityById(User.class, present.getUserId()));
            }
            newPresents.add(newPresent);
        }
        return newPresents;
    }

    @Override
    public Integer getPresentAmount(Long presentPackId, PresentStatusEnum presentStatusEnum) {
        Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("presentPackId", presentPackId);
        conditions.put("presentStatus", presentStatusEnum);

        Long amount = presentPackDao.getPresentPackByConditions(conditions);
        return Integer.parseInt(amount.toString());
    }

    @Override
    public boolean checkPoint(Long pointPurchaseId, Integer needPoint) {
        Preconditions.checkNotNull(pointPurchaseId);
        Preconditions.checkNotNull(needPoint);

        boolean checkStatus = true;
        OrganizationPointDetail opd = presentPackDao.getEntityByField(OrganizationPointDetail.class, "pointPurchaseId", pointPurchaseId);
        if (opd != null && opd.getUseableAmount() < needPoint) checkStatus = false;

        return checkStatus;
    }

    @Override
    public Map<String, Object> checkIssuePack(PresentPackVO presentPackVO) {
        checkNotNull(presentPackVO);

        Map<String, Object> checkMap = Maps.newHashMap();
        boolean checkStatus = true;
        String checkMessage = "校验通过！";

        PresentPackTypeEnum packType = presentPackVO.getPresentPackType();
        List<PresentPointVO> pointVOs = presentPackVO.getPresentPoints();
        int packTotalAmount = presentPackVO.getPackTotalAmount();

        // 校验每次批次信息
        for (PresentPointVO pointVO : pointVOs) {
            Long pointId = pointVO.getPointPurchaseId();
            if (pointId == null) continue;

            if (PresentPackTypeEnum.LUCK.equals(packType)) {
                int pointTotalAmount = pointVO.getTotalAmount();
                int minAmount = pointVO.getMinAmount();
                int maxAmount = pointVO.getMaxAmount();
                // 校验最大积分和最小积分实现的可能性
                if ((pointTotalAmount - minAmount * packTotalAmount) < (maxAmount - minAmount)) {
                    checkStatus = false;
                    checkMessage = "你所选择的最大积分数过大！";
                    break;
                }

                // 校验拼手气红包的可行性
                boolean bonusResult = checkRandBonus(pointTotalAmount, packTotalAmount, minAmount, maxAmount);
                if (!bonusResult) {
                    checkStatus = false;
                    checkMessage = "你的拼手气红包积分或某次积分信息存在问题，不能完成操作！";
                    break;
                }
                // 校验积分余额是否足够
                boolean pointResult = checkPoint(pointId, pointTotalAmount);
                if (!pointResult) {
                    checkStatus = false;
                    checkMessage = "你所选择积分或某批次积分余额已不足，不能完成操作！";
                    break;
                }

            } else {
                int amount = pointVO.getAmount();
                int needAmount = amount * packTotalAmount;
                // 校验积分余额
                boolean pointResult = checkPoint(pointId, needAmount);
                if (!pointResult) {
                    checkStatus = false;
                    checkMessage = "你所选择积分或某批次积分余额已不足，不能完成操作！";
                    break;
                }
            }
        }

        // 封装校验结果并返回
        checkMap.put("checkStatus", checkStatus);
        checkMap.put("checkMessage", checkMessage);
        return checkMap;
    }

    /**
     * 校验是否能购生成拼手气红包
     *
     * @param bonusTotal 积分总数
     * @param bonusCount 红包总数
     * @param minAmount  最小积分数
     * @param maxAmount  最大积分数
     * @return 校验结果
     */
    private boolean checkRandBonus(int bonusTotal, int bonusCount, int minAmount, int maxAmount) {
        boolean bonusStatus = true;
        if ((minAmount * bonusCount) > bonusTotal) bonusStatus = false;
        if ((maxAmount * bonusCount) < bonusTotal) bonusStatus = false;
        return bonusStatus;
    }

    @Override
    public boolean checkPointPurchase(Long purchaseId, Integer needPointAmount) {
        Preconditions.checkNotNull(purchaseId);
        Preconditions.checkNotNull(needPointAmount);
        boolean checkResult = false;
        OrganizationPointDetail opd = presentPackDao.getEntityByField(OrganizationPointDetail.class, "pointPurchaseId", purchaseId);
        if (opd.getUseableAmount() >= needPointAmount) checkResult = true;
        return checkResult;
    }

    @Override
    public PresentPack getEntityById(Long id) {
        return presentPackDao.getEntityById(PresentPack.class, id);
    }

    @Override
    public void updateEntity(PresentPack presentPack) {
        Integer auditStatus = 1;
        /*1.批量修改红包的审核状态*/
        presentService.updatePresentAuditStatus(presentPack.getAuditStatus(), presentPack.getId());
        /*2.审核失败，对红包的操作*/
        if (AuditStatusEnum.FAIL_AUDIT == presentPack.getAuditStatus()) {
            auditStatus = 0;
            List<Present> presents = presentPackDao.getEntitiesByField(Present.class, "presentPackId", presentPack.getId());
            for (Present present : presents) {
                this.handlePresent(present);
            }
        }
        /*3.对商户积分消耗明细的操作*/
        organizationConsumeDetailService.updateByPresentPackId(auditStatus, ConsumeTypeEnum.PRESENT, presentPack.getId());

        presentPackDao.merge(presentPack);  //修改红包批次(PresentPack)
    }

    private Object[] getPresentContent(PresentPack pack) {
        Object[] presentContent = new Object[3];

        // 根据PresentPack获取Present
        Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("presentPackId", pack.getId());
        @SuppressWarnings("unchecked") List<Present> presents = presentPackDao.getEntitiesPagesByFieldList(Present.class, conditions, null, 0, 1).getDomains();

        Object contentPointP = 0;
        Object contentPointE = 0;
        Object contentOrder = "无";

        if (presents.size() == 1) {
            Present present = presents.get(0);
            // 根据present获取PresentItem
            List<PresentItem> presentItems = presentPackDao.getEntitiesByField(PresentItem.class, "presentId", present.getId());

            for (PresentItem presentItem : presentItems) {
                if (presentItem.getPresentItemType() == PresentItemTypeEnum.P) {
                    contentPointP = presentItem.getAmount();
                } else if (presentItem.getPresentItemType() == PresentItemTypeEnum.E) {
                    contentPointE = presentItem.getAmount();
                } else if (presentItem.getPresentItemType() == PresentItemTypeEnum.ORDER_TICKET) {
                    contentOrder = presentPackDao.getEntityById(Order.class, presentItem.getOrderTicketId()).getOrderIdentifier();
                }
            }
        }

        presentContent[0] = contentPointP;
        presentContent[1] = contentPointE;
        presentContent[2] = contentOrder;
        return presentContent;
    }

    /**
     * 当审核失败后，处理红包内容
     *
     * @param present 红包
     */
    private void handlePresent(Present present) {
        /*1.根据红包的id查询出商户的消耗明细*/
        List<OrganizationConsumeDetail> organizationConsumeDetailList =
                organizationConsumeDetailService.selectByConsumeTypeAndTargetId(ConsumeTypeEnum.PRESENT, present.getId());
        /*2.循环商户的消耗明细，并修改商户的积分明细*/
        for (OrganizationConsumeDetail consumeDetail : organizationConsumeDetailList) {
            OrganizationPointDetail pointDetail =
                    presentPackDao.getEntityByField(OrganizationPointDetail.class, "pointPurchaseId", consumeDetail.getPointPurchaseId());
            Integer amount = pointDetail.getUseableAmount() + consumeDetail.getPayableAmount();
            pointDetail.setUseableAmount(amount);
            presentPackDao.merge(pointDetail);
        }// end of for
    }

    @Override
    public DomainPage getAuditRecords(Map<String, Object> fuzzyMap, long pageIndex, long pageSize) {
        return presentPackDao.getAuditRecordByPages(fuzzyMap, pageIndex, pageSize);
    }

    @Override
    public void deletePresentPack(Long presentPackId) {
        PresentPack presentPack = presentPackDao.getEntityById(PresentPack.class, presentPackId);
        presentPack.setAvailable(AvailableEnum.UNAVAILABLE);
        presentPackDao.merge(presentPack);
    }

    /**
     * 对某个批次红包某个使用状态的红包进行统计
     *
     * @param presentPackId
     * @param presentStatus
     */
    private Long getPresentPackAmount(Long presentPackId, PresentStatusEnum presentStatus) {
        Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("presentPackId", presentPackId);
        conditions.put("presentStatus", presentStatus);

        return presentPackDao.getEntityTotalCount(Present.class, conditions);
    }

}