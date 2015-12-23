/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.poi.IssueStrategyDao;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.domain.jpa.poi.*;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.vo.PushMessageVO;
import com.pemass.persist.enumeration.*;
import com.pemass.service.exception.PoiError;
import com.pemass.service.jms.Producer;
import com.pemass.service.pemass.poi.IssueStrategyService;
import com.pemass.service.pemass.poi.OrganizationConsumeDetailService;
import com.pemass.service.pemass.poi.PointPoolService;
import com.pemass.service.pemass.poi.PointPurchaseService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @Description: 积分红包营销策略Service 实现
 * @Author: he jun cheng
 * @CreateTime: 2014-12-03 14:52
 */
@Service
public class IssueStrategyServiceImpl implements IssueStrategyService {

    @Resource
    private IssueStrategyDao issueStrategyDao;

    @Resource
    private PresentPackDao presentPackDao;

    @Resource
    private Producer pushProducer;

    @Resource
    private PointPoolService pointPoolService;

    @Resource
    private PointPurchaseService pointPurchaseService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private OrganizationConsumeDetailService organizationConsumeDetailService;


    @Override
    public DomainPage getIssueStrategyByCondition(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage) {
        DomainPage returnDomainPage = presentPackDao.getEntitiesPagesByFieldList(IssueStrategy.class, conditions, fuzzyConditions, domainPage.getPageIndex(), domainPage.getPageSize());
        getBigIssueStrategies(returnDomainPage);
        return returnDomainPage;
    }

    /**
     * 获取策略的附加信息
     *
     * @param returnDomainPage
     */
    private void getBigIssueStrategies(DomainPage returnDomainPage) {
        @SuppressWarnings("unchecked") List<IssueStrategy> issueStrategies = returnDomainPage.getDomains();
        List<Map<String, Object>> newIssueStrategies = Lists.newArrayList();
        for (IssueStrategy issueStrategy : issueStrategies) {
            Map<String, Object> newIssueStrategy = Maps.newConcurrentMap();
            Long organizationId = issueStrategy.getOrganizationId();
            Organization organization = presentPackDao.getEntityById(Organization.class, organizationId);

            newIssueStrategy.put("issueStrategy", issueStrategy);
            newIssueStrategy.put("organization", organization);
            newIssueStrategies.add(newIssueStrategy);
        }
        returnDomainPage.setDomains(newIssueStrategies);
    }

    @Override
    public void insert(IssueStrategy issueStrategy, List<StrategyContent> strategyContents) {
        // 持久化issueStrategy
        issueStrategy.setStrategyStatus(StrategyStatus.EXECUTED);
        presentPackDao.persist(issueStrategy);

        // 之后的相关操作
        strategyHandle(issueStrategy, strategyContents);
    }

    @Override
    public List selectAllIssueStrategy() {
        return issueStrategyDao.selectIssueStrategyByField();
    }

    @Override
    public void deleteIssueStrategy(Long issueStrategyId) {
        IssueStrategy issueStrategy = presentPackDao.getEntityById(IssueStrategy.class, issueStrategyId);
        issueStrategy.setAvailable(AvailableEnum.UNAVAILABLE);
        presentPackDao.merge(issueStrategy);
    }

    @Transactional
    @Override
    public void repealPresentPackPresent(Long issueStrategyId) {
        IssueStrategy issueStrategy = presentPackDao.getEntityById(IssueStrategy.class, issueStrategyId);
        StrategyTypeEnum strategyType = issueStrategy.getStrategyType();
        StrategyStatus strategyStatus = issueStrategy.getStrategyStatus();
        if ((StrategyTypeEnum.SEND_PRESENT_SQUARE.equals(strategyType)
                || StrategyTypeEnum.CONTENT_PRESENT_NOSHOW.equals(strategyType)
                || StrategyTypeEnum.UAV.equals(strategyType)) &&
                !StrategyStatus.REPEAL.equals(strategyStatus)) {

            /** 获得未领取红包的集合 **/
            Map<String, Object> conditions = Maps.newHashMap();
            conditions.put("issueStrategyId", issueStrategy.getId());  //策略ID
            conditions.put("isClaim", 0);   //  未领取
            List<PresentSquare> pss = presentPackDao.getEntitiesByFieldList(PresentSquare.class, conditions);

            /** 将满足条件的红包从红包广场移除 **/
            Date now = new Date();
            for (PresentSquare ps : pss) {
                Present present = presentPackDao.getEntityById(Present.class, ps.getPresentId());
                present.setPresentStatus(PresentStatusEnum.NONE_ISSUE);
                present.setUpdateTime(now);
                presentPackDao.merge(present);
            }

            /** 设置状态为已撤销 **/
            issueStrategy.setStrategyStatus(StrategyStatus.REPEAL);
            issueStrategy.setUpdateTime(now);
            presentPackDao.merge(issueStrategy);
        }
    }

    @Override
    public List<StrategyContent> getStrategyContent(IssueStrategy issueStrategy) {
        return presentPackDao.getEntitiesByField(StrategyContent.class, "issueStrategy.id", issueStrategy.getId());
    }

    @Override
    public DomainPage findById(long pageIndex, long pageSize) {

        return issueStrategyDao.findById(pageIndex, pageSize);
    }

    @Override
    public IssueStrategy selectIssueStrategyById(Long issueStrategyId) {
        return issueStrategyDao.getEntityById(IssueStrategy.class, issueStrategyId);
    }

    //======================================= 私有方法 PRIVATE ===================================\\

    /**
     * 处理策略操作 > 给用户发积分、或者发红包、或者将红包方法红包广场、或者无人机拼手气红包
     *
     * @param issueStrategy    策略
     * @param strategyContents 策略内容
     */
    private void strategyHandle(IssueStrategy issueStrategy, List<StrategyContent> strategyContents) {
        List<String> userIdList = Lists.newArrayList();
        if (issueStrategy.getUsers() != null) {
            String userIdStr = issueStrategy.getUsers();
            userIdList = Splitter.on(SystemConst.SEPARATOR_SYMBOL)
                    .trimResults()
                    .omitEmptyStrings()
                    .splitToList(userIdStr);
        }

        for (StrategyContent strategyContent : strategyContents) {
            StrategyContentTypeEnum strategyContentType = strategyContent.getStrategyContentType();
            // 积分
            if (StrategyContentTypeEnum.POINT.equals(strategyContentType)) {
                // 持久化策略项
                saveStrategyContent(issueStrategy, strategyContent);
                // 发积分
                sendPoint(issueStrategy, userIdList, strategyContent);
            }
            // 红包
            else if (StrategyContentTypeEnum.PRESENT.equals(strategyContentType)) {
                StrategyTypeEnum strategyType = issueStrategy.getStrategyType();
                if (StrategyTypeEnum.SEND_PRESENT.equals(strategyType)) {
                    // 持久化策略项
                    saveStrategyContent(issueStrategy, strategyContent);
                    // 发红包
                    sendPresent(issueStrategy, userIdList, strategyContent);
                }
                // 放到红包广场展示 放到红包广场不展示 无人机红包
                else {
                    // 持久化策略项
                    saveStrategyContent(issueStrategy, strategyContent);
                    // 方红包到红包广场
                    sendPresentSquare(issueStrategy, strategyContent);
                }
            }
        }
    }

    private void saveStrategyContent(IssueStrategy issueStrategy, StrategyContent strategyContent) {
        strategyContent.setIssueStrategyId(issueStrategy.getId());
        if (StrategyContentTypeEnum.POINT.equals(strategyContent.getStrategyContentType())) {
            PointPurchase purchase = presentPackDao.getEntityById(PointPurchase.class, strategyContent.getPointPurchaseId());
            strategyContent.setPointPurchaseId(purchase.getId());
        } else if (StrategyContentTypeEnum.PRESENT.equals(strategyContent.getStrategyContentType())) {
            PresentPack pack = presentPackDao.getEntityById(PresentPack.class, strategyContent.getPresentPackId());
            strategyContent.setPresentPackId(pack.getId());
        }
        presentPackDao.persist(strategyContent);
    }

    /**
     * 给用户发某一个批次的积分
     *
     * @param issueStrategy
     * @param userIds
     * @param strategyContent
     */
    private void sendPoint(IssueStrategy issueStrategy, List<String> userIds, StrategyContent strategyContent) {
        PointPurchase purchase = pointPurchaseService.getById(strategyContent.getPointPurchaseId());
        OrganizationPointDetail organizationPointDetail = issueStrategyDao.getEntityByField(OrganizationPointDetail.class, "pointPurchaseId", purchase.getId());
        // 商户积分余额校验
        Integer totalPointNumber = strategyContent.getAmount() * userIds.size();
        if (organizationPointDetail.getUseableAmount() < totalPointNumber) {
            PoiError ise = PoiError.ISSUE_TO_USER_POINT_INSUFFICIENT;
            throw new BaseBusinessException(ise.getErrorMessage(), ise);
        }
        PointTypeEnum pointType = purchase.getPointType();

        /*-- 记录商户消费明细 --*/
        this.recordConsumeDetail(issueStrategy, purchase, pointType, totalPointNumber);

        /*-- 从商户手中扣积分 --*/
        organizationPointDetail.setUseableAmount(organizationPointDetail.getUseableAmount() - totalPointNumber);
        organizationPointDetail.setUpdateTime(new Date());
        presentPackDao.merge(organizationPointDetail);

        /*-- 给用户发积分 --*/
        for (String userId : userIds)
            sendPointToUser(issueStrategy, strategyContent, purchase, pointType, userId);
    }

    /**
     * 给用户发放积分
     *
     * @param issueStrategy
     * @param strategyContent
     * @param purchase
     * @param pointType
     * @param userId
     */
    private void sendPointToUser(IssueStrategy issueStrategy, StrategyContent strategyContent, PointPurchase purchase, PointTypeEnum pointType, String userId) {
        Organization organization = organizationService.getOrganizationById(issueStrategy.getOrganizationId());

        UserPointDetail pointDetail = new UserPointDetail();
        pointDetail.setOrganizationId(issueStrategy.getOrganizationId());
        pointDetail.setPointPurchaseId(purchase.getId());
        pointDetail.setAmount(strategyContent.getAmount());
        pointDetail.setArea(purchase.getArea());
        // 积分有效期
        PointPool pointPool = pointPoolService.getById(purchase.getPointPoolId());
        DateTime dateTime = DateTime.now().plusMonths(pointPool.getExpiryPeriod()).millisOfDay().withMaximumValue();
        pointDetail.setExpiryTime(dateTime.toDate());
        pointDetail.setPointType(pointType);
        pointDetail.setUseableAmount(strategyContent.getAmount());
        pointDetail.setPointChannel(PointChannelEnum.STRATEGY_LARGESS);

        User user = presentPackDao.getEntityById(User.class, Long.parseLong(userId));
        pointDetail.setUserId(user.getId());

        presentPackDao.persist(pointDetail);

        //=============== 积分发放后给该用户推送一条信息 ==============\\
        this.sendPointPushMessage(user, organization.getOrganizationName(), purchase.getPointType(), strategyContent.getAmount());
    }

    /**
     * 记录商户积分消耗明细
     *
     * @param issueStrategy 策略（targetId）
     * @param pointPurchase 认购记录
     * @param pointType     积分类型
     * @param amount        数量
     */
    private void recordConsumeDetail(IssueStrategy issueStrategy, PointPurchase pointPurchase, PointTypeEnum pointType, Integer amount) {
        OrganizationConsumeDetail organizationConsumeDetail = new OrganizationConsumeDetail();
        organizationConsumeDetail.setPointType(pointType);
        organizationConsumeDetail.setPointPurchaseId(pointPurchase.getId());
        organizationConsumeDetail.setConsumeType(ConsumeTypeEnum.STRATEGY);
        organizationConsumeDetail.setConsumeTargetId(issueStrategy.getId());
        organizationConsumeDetail.setAmount(amount);
        organizationConsumeDetail.setPayableAmount(0);
        organizationConsumeDetailService.insert(organizationConsumeDetail);
    }

    /**
     * 给用户发某一个批次的红包
     *
     * @param issueStrategy
     * @param userIds
     * @param strategyContent
     */
    private void sendPresent(IssueStrategy issueStrategy, List<String> userIds, StrategyContent strategyContent) {
        PresentPack pack = presentPackDao.getEntityById(PresentPack.class, strategyContent.getPresentPackId());

        /*-- 红包校验 --*/
        Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("presentStatus", PresentStatusEnum.NONE_ISSUE);
        conditions.put("presentPackId", pack.getId());

        Integer needPresent = userIds.size() * strategyContent.getAmount();
        DomainPage domainPage = presentPackDao.getEntitiesPagesByFieldList(Present.class, conditions, 1, needPresent);
        @SuppressWarnings("unchecked") List<Present> searchPresents = (List<Present>) domainPage.getDomains();
        Queue<Present> presents = Lists.newLinkedList(searchPresents);
        if (presents.size() != needPresent) {
            PoiError ise = PoiError.ISSUE_TO_USER_PRESENT_INSUFFICIENT;
            throw new BaseBusinessException(ise.getErrorMessage(), ise);
        }

        /*-- 给用户发红包 --*/
        seedPresentToUser(issueStrategy, userIds, strategyContent, presents);
    }

    /**
     * 给用户发红包
     *
     * @param issueStrategy
     * @param userIds
     * @param strategyContent
     * @param presents
     */
    private void seedPresentToUser(IssueStrategy issueStrategy, List<String> userIds, StrategyContent strategyContent, Queue<Present> presents) {
        Organization organization = organizationService.getOrganizationById(issueStrategy.getOrganizationId());
        for (int i = 0; i < userIds.size(); i++) {
            int amount = strategyContent.getAmount(); //每个用户获得红包个数
            Long userId = Long.parseLong(userIds.get(i));
            User user = presentPackDao.getEntityById(User.class, userId);
            for (int j = 0; j < amount; j++) {
                Present present = presents.poll();
                present.setUserId(user.getId());
                present.setPresentStatus(PresentStatusEnum.HAS_ISSUE);
                present.setUpdateTime(DateTime.now().toDate());

                presentPackDao.merge(present);
            }
            //=============== 红包发放后给该用户推送一条信息 ==============\\
            sendPresentPushMessage(user, organization.getOrganizationName(), amount);
        }
    }

    /**
     * 把红包方法红包广场
     *
     * @param issueStrategy
     * @param strategyContent
     */
    private void sendPresentSquare(IssueStrategy issueStrategy, StrategyContent strategyContent) {

        //把红包发到红包广场，取出某数量要发往广场改批次的红包，新增记录到广场表
        PresentPack presentPack = presentPackDao.getEntityById(PresentPack.class, strategyContent.getPresentPackId());
        Map<String, Object> conditions = Maps.newHashMap();
        conditions.put("presentStatus", PresentStatusEnum.NONE_ISSUE);
        conditions.put("presentPackId", presentPack.getId());

        DomainPage domainPage = presentPackDao.getEntitiesPagesByFieldList(Present.class, conditions, null, 1, strategyContent.getAmount());
        @SuppressWarnings("unchecked") List<Present> presents = (List<Present>) domainPage.getDomains();

        // 校验未发放红包是否足够
        if (presents.size() != strategyContent.getAmount()) {
            throw new BaseBusinessException(PoiError.ISSUE_TO_SQUARE_PRESENT_INSUFFICIENT);
        }

        // 将红包放到广场
        savePresentToSquare(issueStrategy, presents);
    }

    /**
     * 将红包放到红包广场
     *
     * @param issueStrategy
     * @param presents
     */
    private void savePresentToSquare(IssueStrategy issueStrategy, List<Present> presents) {
        for (Present present : presents) {
            present.setPresentStatus(PresentStatusEnum.PRESENT_SQUARE);
            present.setUpdateTime(new Date());
            present = presentPackDao.merge(present);

            //新增红包广场记录
            PresentSquare presentSquare = new PresentSquare();
            presentSquare.setIssueStrategyId(issueStrategy.getId());
            presentSquare.setPresentId(present.getId());
            presentSquare.setStartTime(issueStrategy.getStartTime());
            presentSquare.setEndTime(issueStrategy.getEndTime());
            presentSquare.setExecuteStartTime(issueStrategy.getExecuteStartTime());
            presentSquare.setExecuteEndTime(issueStrategy.getExecuteEndTime());
            presentSquare.setIsClaim(0);
            presentPackDao.persist(presentSquare);

            //红包广场的红包放到REDIS中
            issueStrategyDao.bindPresentSquareToIssueStrategy(presentSquare.getId(), issueStrategy.getId(), issueStrategy.getEndTime());
        }
    }


    /**
     * 送红包发生推送消息
     */
    private void sendPresentPushMessage(User user, String organizationName, Integer amount) {
        PushMessageVO pushMessageVO = new PushMessageVO();
        pushMessageVO.setAudience(user.getId().toString());
        pushMessageVO.setPushMessageType(PushMessageTypeEnum.GRANT_PRESENT_FROM_ORGANIZATION);

        List<Object> param = Lists.newArrayList();
        param.add(organizationName);
        param.add(amount);
        pushMessageVO.setParam(param);

        pushProducer.send(pushMessageVO);
    }

    /**
     * 送积分发送推送消息
     */
    private void sendPointPushMessage(User user, String organizationName, PointTypeEnum pointType, Integer amount) {

        PushMessageVO pushMessageVO = new PushMessageVO();
        pushMessageVO.setAudience(user.getId().toString());
        pushMessageVO.setPushMessageType(PushMessageTypeEnum.GRANT_POINT_FROM_ORGANIZATION);

        List<Object> param = Lists.newArrayList();
        param.add(organizationName);
        String pointDescription = amount + "个" + pointType.getDescription();
        param.add(pointDescription);
        pushMessageVO.setParam(param);

        pushProducer.send(pushMessageVO);
    }

}