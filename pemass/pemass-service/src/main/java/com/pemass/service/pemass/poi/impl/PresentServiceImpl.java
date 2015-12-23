/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.poi.PresentDao;
import com.pemass.persist.dao.sys.UserDao;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.domain.jpa.poi.OrganizationConsumeDetail;
import com.pemass.persist.domain.jpa.poi.PointPool;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.domain.jpa.poi.Present;
import com.pemass.persist.domain.jpa.poi.PresentItem;
import com.pemass.persist.domain.jpa.poi.PresentRecord;
import com.pemass.persist.domain.jpa.poi.UserConsumeDetail;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.vo.PushMessageVO;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointChannelEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.persist.enumeration.PresentItemTypeEnum;
import com.pemass.persist.enumeration.PresentPackTypeEnum;
import com.pemass.persist.enumeration.PresentSourceTypeEnum;
import com.pemass.persist.enumeration.PresentStatusEnum;
import com.pemass.persist.enumeration.PushMessageTypeEnum;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.exception.PoiError;
import com.pemass.service.jms.Producer;
import com.pemass.service.pemass.bas.SmsMessageService;
import com.pemass.service.pemass.ec.OrderTicketService;
import com.pemass.service.pemass.poi.OrganizationConsumeDetailService;
import com.pemass.service.pemass.poi.PointPoolService;
import com.pemass.service.pemass.poi.PointPurchaseService;
import com.pemass.service.pemass.poi.PresentService;
import com.pemass.service.pemass.poi.UserConsumeDetailService;
import com.pemass.service.pemass.poi.UserPointDetailService;
import com.pemass.service.pemass.sys.AuthService;
import com.pemass.service.pemass.sys.UserService;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @Description: PresentServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 17:21
 */
@Service
public class PresentServiceImpl implements PresentService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private PresentDao presentDao;

    @Resource
    private UserService userService;

    @Resource
    private AuthService authService;

    @Resource
    private SmsMessageService smsMessageService;

    @Resource
    private OrganizationConsumeDetailService organizationConsumeDetailService;

    @Resource
    private PointPurchaseService pointPurchaseService;

    @Resource
    private PointPoolService pointPoolService;

    @Resource
    private UserConsumeDetailService userConsumeDetailService;

    @Resource
    private UserPointDetailService userPointDetailService;

    @Resource
    private OrderTicketService orderTicketService;

    @Resource
    private Producer pushProducer;

    @Resource
    private UserDao userDao;


    // =====================================
    @Override
    public void savePresent(Present present) {
        Preconditions.checkNotNull(present);
        presentDao.persist(present);
    }

    @Override
    public Present getById(Long presentId) {
        Preconditions.checkNotNull(presentId);
        return jpaBaseDao.getEntityById(Present.class, presentId);
    }

    @Override
    public Long getNoneIssuePresentCountByOrganizationId(Long organizationId) {
        return presentDao.getPresentCount(organizationId);
    }

    @Override
    public List<Present> selectPresentByPackId(Long presentPackId) {
        return presentDao.getEntitiesByField(Present.class, "presentPackId", presentPackId);
    }

    /**
     * 根据用户id,查询所用可使用红包
     *
     * @param uid
     * @return
     */
    @Override
    public DomainPage selectPack(Long uid,PresentStatusEnum presentStatus, long pageSize, long pageIndex) {
        return presentDao.selectPack(uid, presentStatus, pageSize, pageIndex);
    }


    /**
     * 用户赠送红包
     *
     * @param username  用户名（赠送手机号）
     * @param presentId 红包id
     * @return
     */
    @Transactional
    @Override
    public Boolean grant(Long presentId, String username) {
        Preconditions.checkNotNull(presentId);
        Preconditions.checkNotNull(username);
        Present present = jpaBaseDao.getEntityById(Present.class, presentId);
        if (present == null) {
            throw new BaseBusinessException(PoiError.PRESENT_NOT_EXIST);
        }
        if (PresentStatusEnum.HAS_RECEIVE.equals(present.getPresentStatus())) {
            throw new BaseBusinessException(PoiError.PRESENT_SHOULD_NOT_GRANT);
        }

        /**1,判断是否添加C端用户*/
//        User toUser = jpaBaseDao.getEntityByField(User.class, "username", username);
        User toUser = userDao.getUserByUsername(username);
        User fromUser = jpaBaseDao.getEntityById(User.class, present.getUserId());
        if (toUser == null) {
            toUser = new User();
            toUser.setUsername(username);
            toUser.setPassword(username);
            authService.register(toUser);
            //冻结用户
            toUser.setAvailable(AvailableEnum.FROZEN);
            jpaBaseDao.merge(toUser);
            smsMessageService.append(toUser.getUsername(), SmsTypeEnum.PRESENT_NOREG, new Object[]{fromUser.getUsername(), "您", PemassConst.CLOUDMONEY_LATEST_URL});
        } else {
            smsMessageService.append(toUser.getUsername(), SmsTypeEnum.PRESENT_REGED, new Object[]{fromUser.getUsername()});
        }
        /**2,添加赠送红包记录*/
        this.doSavePresentRecord(fromUser, toUser, present);
        /**3,赠送好友*/
        present.setUserId(toUser.getId());
        jpaBaseDao.merge(present);


        /**推送消息**/
        PushMessageVO pushMessageVO = new PushMessageVO();
        pushMessageVO.setAudience(toUser.getId().toString());
        pushMessageVO.setPushMessageType(PushMessageTypeEnum.GRANT_PRESENT);
        List<Object> param = new ArrayList<Object>();
        param.add(fromUser.getUsername());
        pushMessageVO.setParam(param);
        pushProducer.send(pushMessageVO);

        return true;
    }

    @Override
    public DomainPage selectHasReceivePresentByOrganizationId(Long organizationId, long pageIndex, long pageSize) {
        DomainPage domainPage = presentDao.selectReceivePresentById(organizationId, pageIndex, pageSize);
        DomainPage newDomainPage = this.PosttingDomainPage(domainPage);

        return newDomainPage;
    }

    /**
     * 重新封装DomainPage
     *
     * @param domainPage
     * @return
     */
    private DomainPage PosttingDomainPage(DomainPage domainPage) {
        List result = new ArrayList();
        String area;
        String urlStr = "";
        for (Object object : domainPage.getDomains()) {
            Object[] obj = new Object[2];
            Present present = (Present) object;
            User presentUser = jpaBaseDao.getEntityById(User.class, present.getUserId());
            obj[0] = presentUser;
            urlStr = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=P3T7zML1eu48NaVhb6ZweZlG&location="
                    + presentUser.getLatitude() + "," + presentUser.getLongitude();
            area = getUserArea(urlStr);
            obj[1] = area;
            result.add(obj);
        }
        DomainPage newDomainPage = domainPage;
        newDomainPage.getDomains().clear();
        newDomainPage.getDomains().addAll(result);
        return newDomainPage;
    }

    /**
     * 根据url地址获取区域
     *
     * @param urlStr
     * @return
     */
    private String getUserArea(String urlStr) {
        StringBuffer json = new StringBuffer();
        String location = urlStr.substring(urlStr.lastIndexOf("=") + 1);
        String area = null;
        if ("null,null".equals(location)) {
            area = "无区域";
        } else {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStreamReader input = new InputStreamReader(conn.getInputStream(), "utf-8");
                Scanner inputStream = new Scanner(input);
                while (inputStream.hasNext()) {
                    json.append(inputStream.nextLine());
                }
                Map<String, Object> results;
                Map<String, Object> maps;
                maps = new ObjectMapper().readValue(json.toString(), Map.class);
                Integer status = (Integer) maps.get("status");
                if (status == 0) {
                    results = (Map<String, Object>) maps.get("result");
                    area = (String) results.get("formatted_address");
                } else {
                    area = "无区域";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return area;
    }


    @Transactional
    @Override
    public Boolean packAndGrant(Long uid, String presentName, PointTypeEnum pointType, Integer amount, Integer isGeneral, Long[] orderTicketIds,
                                String username, String instruction) {
        /** 1.包红包 */
        Present present = this.pack(uid, presentName, pointType, amount, isGeneral, orderTicketIds, instruction);
        /** 2.赠送红包 */
        this.grant(present.getId(), username);
        /** 3.通知 */
        return true;
    }

    @Transactional
    @Override
    public Present pack(Long uid, String presentName, PointTypeEnum pointType, Integer amount, Integer isGeneral, Long[] orderTicketIds, String instruction) {
        Preconditions.checkNotNull(amount == null || orderTicketIds == null, "积分数量和电子票ID不能同时为空");
        Preconditions.checkNotNull(uid);

        User user = userService.getById(uid);

        /** 1.保存红包 */
        Present present = this.doSavePresent(user, presentName, instruction);

        /** 2.处理积分 */
        this.doHandlePoint(uid, pointType, amount, isGeneral, present);

        /** 3.处理电子票 */
        this.doHandleTicket(orderTicketIds, present);

        return present;
    }

    /**
     * 处理红包中的积分
     *
     * @param uid
     * @param pointType
     * @param amount
     * @param isGeneral
     * @param present
     */
    private void doHandlePoint(Long uid, PointTypeEnum pointType, Integer amount, Integer isGeneral, Present present) {
        Integer _amount = amount;
        if (amount <= 0) {
            return;
        }

        DomainPage<UserPointDetail> userPointDetailDomainPage = userPointDetailService.selectByPointTypeAndIsGeneral(uid, pointType, isGeneral, "expiryTime", BaseDao.OrderBy.DESC, 0, Integer.MAX_VALUE);
        List<UserPointDetail> userPointDetails = userPointDetailDomainPage.getDomains();
        Date expiryTime = userPointDetails.get(0).getExpiryTime();
        for (UserPointDetail userPointDetail : userPointDetails) {
            UserConsumeDetail userConsumeDetail = new UserConsumeDetail();
            userConsumeDetail.setUserPointDetailId(userPointDetail.getId());
            userConsumeDetail.setConsumeType(ConsumeTypeEnum.PRESENT);
            userConsumeDetail.setConsumeTargetId(present.getId());
            userConsumeDetail.setPointType(pointType);

            userConsumeDetail.setUserPointDetailId(userPointDetail.getId());

            int useableAmount = userPointDetail.getUseableAmount();
            if (userPointDetail.getUseableAmount() < amount) {
//                userPointDetail.setUseableAmount(0);
//                jpaBaseDao.merge(userPointDetail);
                userPointDetailService.updateUseableAmountById(userPointDetail.getId(),userPointDetail.getUseableAmount());

                userConsumeDetail.setPayableAmount(useableAmount);
                userConsumeDetail.setAmount(useableAmount);
                jpaBaseDao.persist(userConsumeDetail);
            } else {
//                userPointDetail.setUseableAmount(useableAmount - amount);
//                jpaBaseDao.merge(userPointDetail);
                userPointDetailService.updateUseableAmountById(userPointDetail.getId(), amount);

                userConsumeDetail.setPayableAmount(amount);
                userConsumeDetail.setAmount(amount);
                jpaBaseDao.persist(userConsumeDetail);
                amount = 0;
                break;  //当进入else表示循环结束
            }
            amount = amount - useableAmount;
        } //end of for

        //当积分不足时，抵扣失败
        if (amount > 0) {
            if (pointType == PointTypeEnum.E) {
                throw new BaseBusinessException(PoiError.POINT_E_NOT_ENOUGH);
            } else {
                throw new BaseBusinessException(PoiError.POINT_P_NOT_ENOUGH);
            }
        }

//        pushMessageService.notifyPointChange(uid, pointType, _amount, 0);

        //2.红包项
        PresentItem item = new PresentItem();
        item.setPresentId(present.getId());
        item.setAmount(_amount);
        item.setPresentItemType(PresentItemTypeEnum.P);
        item.setIsGeneral(isGeneral);
        item.setExpiryTime(expiryTime);
        jpaBaseDao.persist(item);
    }

    /**
     * 处理赠送电子票
     *
     * @param orderTicketIds
     * @param present
     */
    private void doHandleTicket(Long[] orderTicketIds, Present present) {
        if (orderTicketIds == null || orderTicketIds.length == 0) {
            return;
        }

        for (Long ticketId : orderTicketIds) {
            PresentItem item = new PresentItem();
            item.setPresentId(present.getId());
            item.setAmount(1);
            item.setPresentItemType(PresentItemTypeEnum.ORDER_TICKET);
            OrderTicket ticket = orderTicketService.getOrderTicketById(ticketId);
            item.setExpiryTime(ticket.getExpiryTime());
            item.setOrderTicketId(ticketId);
            jpaBaseDao.persist(item);
            //冻结电子票
            ticket.setUserId(null);
            jpaBaseDao.merge(ticket);
        }
    }

    /**
     * 保存红包赠送记录
     *
     * @param fromUser
     * @param toUser
     * @param present
     */
    private void doSavePresentRecord(User fromUser, User toUser, Present present) {
        PresentRecord presentRecord = new PresentRecord();
        presentRecord.setPresentId(present.getId());
        presentRecord.setToUserId(toUser.getId());
        presentRecord.setFromUserId(fromUser.getId());
        presentRecord.setGivingTime(new Date());
        jpaBaseDao.persist(presentRecord);
    }

    @Override
    public List<PresentItem> getPresentItemsByPresentId(Long presentId) {
        return jpaBaseDao.getEntitiesByField(PresentItem.class, "presentId", presentId);
    }

    @Transactional
    @Override
    public Present unpack(Long presentId) {

        Present present = jpaBaseDao.getEntityById(Present.class, presentId);
        if (present == null) {
            throw new BaseBusinessException(PoiError.PRESENT_NOT_EXIST);
        }
        if (PresentStatusEnum.HAS_RECEIVE.equals(present.getPresentStatus())) {
            throw new BaseBusinessException(PoiError.PRESENT_SHOULD_NOT_PACK);
        }

        List<PresentItem> presentItems = jpaBaseDao.getEntitiesByField(PresentItem.class, "presentId", presentId);

        /**-- 1.拆积分 --*/
        this.unpackPoint(present, presentItems);

        /**-- 2.拆电子票 --*/
        this.unpackTicket(present, presentItems);

        /**-- 3.修改红包状态 --*/
        present.setPresentStatus(PresentStatusEnum.HAS_RECEIVE);
        jpaBaseDao.merge(present);
        return present;
    }

    @Override
    public Present updatePresent(Present present) {
        return jpaBaseDao.merge(present);
    }

    @Override
    public boolean updatePresentAuditStatus(AuditStatusEnum auditStatus, Long presentPackId) {
        Preconditions.checkNotNull(auditStatus);
        Preconditions.checkNotNull(presentPackId);
        return presentDao.updateAuditStatus(auditStatus, presentPackId);
    }

    @Override
    public Integer selectPack(Long uid) {
        Long total = presentDao.selectCount(uid);
        return total.intValue();
    }

    @Override
    public long getTotalPointByPresentPackID(Long presentPackID, PresentItemTypeEnum presentItemType) {
        Preconditions.checkNotNull(presentPackID);
        Preconditions.checkNotNull(presentItemType);
        return presentDao.getTotalPointByPresentPackID(presentPackID, presentItemType);
    }

    /**
     * 从红包中拆出电子票
     *
     * @param present
     * @param presentItems
     */
    private void unpackTicket(Present present, List<PresentItem> presentItems) {
        for (PresentItem presentItem : presentItems) {
            if (PresentItemTypeEnum.ORDER_TICKET.equals(presentItem.getPresentItemType())) {
                OrderTicket orderTicket = orderTicketService.getOrderTicketById(presentItem.getOrderTicketId());
                orderTicket.setUserId(present.getUserId());
                jpaBaseDao.merge(orderTicket);
            }
        }
    }

    /**
     * 根据红包拆积分
     *
     * @param present
     * @param presentItems
     */
    private void unpackPoint(Present present, List<PresentItem> presentItems) {
        //拆商家赠送红包
        if (PresentSourceTypeEnum.ORGANIZATION.equals(present.getPresentSourceType())) {
            List<OrganizationConsumeDetail> organizationConsumeDetails = organizationConsumeDetailService.selectByConsumeTypeAndTargetId(ConsumeTypeEnum.PRESENT, present.getId());
            for (OrganizationConsumeDetail detail : organizationConsumeDetails) {
                UserPointDetail userPointDetail = new UserPointDetail();
                userPointDetail.setUserId(present.getUserId());
                userPointDetail.setPointPurchaseId(detail.getPointPurchaseId());
                PointPurchase pointPurchase = pointPurchaseService.getById(detail.getPointPurchaseId());
                userPointDetail.setOrganizationId(pointPurchase.getOrganizationId());
                userPointDetail.setPointType(detail.getPointType());
                userPointDetail.setArea(pointPurchase.getArea());
                userPointDetail.setAmount(detail.getAmount());
                userPointDetail.setUseableAmount(detail.getAmount());
                userPointDetail.setPointChannel(PointChannelEnum.PRESENT);
                userPointDetail.setPointChannelTargetId(present.getId());
                PointPool pointPool = pointPoolService.getById(pointPurchase.getPointPoolId());
                DateTime dateTime = DateTime.now().plusMonths(pointPool.getExpiryPeriod()).millisOfDay().withMaximumValue();
                userPointDetail.setExpiryTime(dateTime.toDate());
                jpaBaseDao.persist(userPointDetail);
            } //end  of for

            //更新实付积分数
            organizationConsumeDetailService.updateAmount(ConsumeTypeEnum.PRESENT, present.getId());
        }

        //拆用户赠送红包
        if (PresentSourceTypeEnum.USER.equals(present.getPresentSourceType())) {
            List<UserConsumeDetail> userConsumeDetails = userConsumeDetailService.selectByConsumeTypeAndTargetId(ConsumeTypeEnum.PRESENT, present.getId());
            for (UserConsumeDetail detail : userConsumeDetails) {
                UserPointDetail userPointDetail = new UserPointDetail();
                userPointDetail.setUserId(present.getUserId());
                UserPointDetail fromUserPointDetail = userPointDetailService.getById(detail.getUserPointDetailId());
                userPointDetail.setPointPurchaseId(fromUserPointDetail.getPointPurchaseId());
                userPointDetail.setOrganizationId(fromUserPointDetail.getOrganizationId());
                userPointDetail.setPointType(fromUserPointDetail.getPointType());
                userPointDetail.setArea(fromUserPointDetail.getArea());
                userPointDetail.setAmount(detail.getPayableAmount());
                userPointDetail.setUseableAmount(detail.getPayableAmount());
                userPointDetail.setPointChannel(PointChannelEnum.PRESENT);
                Date expiryTime = null;
                for (PresentItem presentItem : presentItems) {
                    if (presentItem.getPresentItemType().toString().equals(detail.getPointType().toString())) {
                        expiryTime = presentItem.getExpiryTime();
                        break;
                    }
                }
                userPointDetail.setExpiryTime(expiryTime);
                jpaBaseDao.persist(userPointDetail);
            } // end of for

            //     userConsumeDetailService.updateAmount(ConsumeTypeEnum.PRESENT, present.getId());
        }
    }


    /**
     * 清算积分操作
     *
     * @param list      所有认购积分批次
     * @param pointType 类型
     * @param amount    赠送的积分数量
     * @return
     */
    public Boolean comparison(List<UserPointDetail> list, PointTypeEnum pointType, Integer amount, Present present) {
        Integer newAmount = amount;
        // int i = 1;
        if (list != null && list.size() > 0) {
            for (UserPointDetail pointDetail : list) {
                updatePresentExpiryTime(present, pointDetail.getExpiryTime());
                amount = pointDetail.getUseableAmount() - newAmount;//如果结果为正数、第一批积分就够用，则反之
                if (amount < 0) {
                    //     ++i;
                    Integer deduction = newAmount - pointDetail.getUseableAmount();
                    newAmount = deduction;
                    insertPresentItem(present, pointType, pointDetail.getUseableAmount(), pointDetail); //添加包红包明细
                    recordOperation(pointDetail, pointType, pointDetail.getUseableAmount(), present.getId());//清分操作
                } else {
                    insertPresentItem(present, pointType, newAmount, pointDetail); //添加包红包明细
                    recordOperation(pointDetail, pointType, newAmount, present.getId());//清分操作
                    return true;
                }
            }
        }
        /**1,如何积分不够赠送，则抛出异常*/
        if (newAmount > 0) {
            throw new BaseBusinessException(PoiError.INTEGRAL_NOT_USE);
        }
        return false;
    }


    /**
     * 添加用户消耗积分记录
     *
     * @param userPointDetail
     * @param pointType
     * @param newAmount
     */
    private void recordOperation(UserPointDetail userPointDetail, PointTypeEnum pointType, Integer newAmount, Long consumeTargetId) {
        /**1,更新扣除相关积分*/
        userPointDetail.setUseableAmount((int) userPointDetail.getUseableAmount() - newAmount);
        jpaBaseDao.merge(userPointDetail);
        /**2,添加消费记录*/
        UserConsumeDetail userConsumeDetail = new UserConsumeDetail();
        userConsumeDetail.setAmount(newAmount);
        userConsumeDetail.setPayableAmount(newAmount);
        userConsumeDetail.setPointType(pointType);
        userConsumeDetail.setUserPointDetailId(userPointDetail.getId());
        userConsumeDetail.setConsumeType(ConsumeTypeEnum.PRESENT);
        userConsumeDetail.setConsumeTargetId(consumeTargetId);

        jpaBaseDao.persist(userConsumeDetail);
    }

    /**
     * 添加一个红包
     *
     * @param user
     * @param presentName
     * @param instruction
     * @return
     */
    private Present doSavePresent(User user, String presentName, String instruction) {
        Present present = new Present();
        present.setUserId(user.getId());
        present.setArea("00:00:00:00:00");
        present.setAuditStatus(AuditStatusEnum.HAS_AUDIT);
        present.setExpiryTime(SystemConst.FUTURE_TIME);
        present.setPresentStatus(PresentStatusEnum.HAS_ISSUE);
        present.setPresentName(presentName);
        present.setInstruction(instruction);
        present.setSourceName(user.getUsername());
        present.setIssueTime(new Date());
        present.setPresentPackType(PresentPackTypeEnum.GENERAL);
        present.setPresentSourceType(PresentSourceTypeEnum.USER);
        jpaBaseDao.persist(present);
        return present;
    }

    /**
     * 添加‘电子票’红包项
     *
     * @param tickets
     * @param present
     */
    private void insertPresentItemByTicket(List<OrderTicket> tickets, Present present) {
        for (OrderTicket ticket : tickets) {
            PresentItem presentItem = new PresentItem();
            presentItem.setPresentItemType(PresentItemTypeEnum.ORDER_TICKET);
            presentItem.setPresentId(present.getId());
            presentItem.setAmount(1);
            presentItem.setOrderTicketId(ticket.getId());
            jpaBaseDao.persist(presentItem);
            //*修改红包的时间，此时间 为红包项的最长的时间
            updatePresentExpiryTime(present, SystemConst.FUTURE_TIME);
        }
    }

    /**
     * 添加红包‘积分’项明细
     *
     * @param present
     * @param pointType
     * @param amount
     * @param pointDetail
     */
    private void insertPresentItem(Present present, PointTypeEnum pointType, Integer amount, UserPointDetail pointDetail) {
        PresentItem presentItem = new PresentItem();
        presentItem.setPresentId(present.getId());
        presentItem.setAmount(amount);
        if (PointTypeEnum.E.equals(pointType)) {
            presentItem.setPresentItemType(PresentItemTypeEnum.E);
        }
        if (PointTypeEnum.P.equals(pointType)) {
            presentItem.setPresentItemType(PresentItemTypeEnum.P);
        }
        jpaBaseDao.persist(presentItem);
    }

    /**
     * 修改时间  取最长的时间
     *
     * @param present
     * @param date
     */
    public void updatePresentExpiryTime(Present present, Date date) {
        if (present.getExpiryTime().getTime() < date.getTime()) {
            present.setExpiryTime(date);
            jpaBaseDao.merge(present);
        }
    }

}