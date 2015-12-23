/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.persist.dao.poi.LotteryDrawDao;
import com.pemass.persist.dao.poi.UserPointDetailDao;
import com.pemass.persist.domain.jpa.poi.LotteryDraw;
import com.pemass.persist.domain.jpa.poi.OrganizationConsumeDetail;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.domain.jpa.poi.UserConsumeDetail;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.domain.jpa.sys.Account;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.service.exception.PoiError;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.poi.LotteryDrawService;
import com.pemass.service.pemass.sys.ConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description: LotteryDrawServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2015-01-27 11:19
 */
@Service
public class LotteryDrawServiceImpl implements LotteryDrawService {

    @Resource
    private LotteryDrawDao lotteryDrawDao;

    @Resource
    private UserPointDetailDao userPointDetailDao;

    @Resource
    private ConfigService configService;

    @Override
    public void insertExecute(Long uid, Integer prize) {
        User user = lotteryDrawDao.getEntityById(User.class, uid);
        if (user == null) {
            throw new BaseBusinessException(SysError.USER_NOT_EXIST);
        }
        List<LotteryDraw> list = lotteryDrawDao.getEntityById(uid);
        Config config=configService.getConfigByKey(ConfigEnum.JFT_ORGANIZATION_ID);
        Account account = lotteryDrawDao.getEntityByField(Account.class, "organizationId",Long.valueOf(config.getValue()));
        if (account == null) {
            throw new BaseBusinessException(SysError.USERNAME_NOT_EXIST);
        }
        List<PointPurchase> purchaseListP = lotteryDrawDao.getBySystemUser(PointTypeEnum.P, account.getOrganizationId());
        String orderIdentifier = UUIDUtil.randomWithoutBar();
        /**判断是否可以免费抽奖*/
        if (list.size() <= 0) {
            comparison(purchaseListP, prize, user, PointTypeEnum.P, orderIdentifier);
            return;
        }
        List<UserPointDetail> pointDetails = userPointDetailDao.selectByUserId(user.getId(), PointTypeEnum.P);
        comparison(purchaseListP, prize, user, PointTypeEnum.P, orderIdentifier); //扣除相应奖项的积分 且 用户得到相应奖项的积分
        comparison(user, 100, pointDetails, orderIdentifier);//扣除用户抽奖产生的积分
    }


    /**
     * 扣除用户抽奖所消耗积分
     *
     * @param user            用户
     * @param totalPointP     积分
     * @param pointDetails    积分明细集合
     * @param orderIdentifier 编号
     * @return
     */
    private void comparison(User user, Integer totalPointP, List<UserPointDetail> pointDetails, String orderIdentifier) {
        Integer newAmount = totalPointP;
        if (pointDetails.size() > 0 && pointDetails != null) {
            for (UserPointDetail pointDetail : pointDetails) {
                totalPointP = pointDetail.getUseableAmount() - newAmount;//如果结果为正数、第一批积分就够用，则反之
                if (totalPointP < 0) {
                    Integer deduction = newAmount - pointDetail.getUseableAmount();
                    newAmount = deduction;
                    LotteryDraw lotteryDraw = insertLotteryDraw(user, pointDetail, pointDetail.getUseableAmount(), orderIdentifier); //添加抽中奖记录
                    insertConsume(pointDetail, pointDetail.getUseableAmount(), lotteryDraw);//添加用户消耗积分明细
                    updatePointDetail(pointDetail, pointDetail.getUseableAmount());
                } else {
                    LotteryDraw lotteryDraw = insertLotteryDraw(user, pointDetail, newAmount, orderIdentifier); //添加抽中奖记录
                    insertConsume(pointDetail, newAmount, lotteryDraw);//添加用户消耗的积分明细
                    updatePointDetail(pointDetail, newAmount);
                    return;
                }
            }
        }
        /**1,如何积分不够赠送，则抛出异常*/
        if (newAmount > 0) {
            throw new BaseBusinessException(PoiError.POINT_P_NOT_ENOUGH);
        }
    }

    /**
     * 添加用户消耗抽奖记录
     *
     * @param user            用户
     * @param pointDetail     积分明细对象
     * @param useableAmount   消耗积分
     * @param orderIdentifier 编号
     * @return
     */
    private LotteryDraw insertLotteryDraw(User user, UserPointDetail pointDetail, Integer useableAmount, String orderIdentifier) {
        LotteryDraw lotteryDraw = new LotteryDraw();
        lotteryDraw.setUserId(user.getId());
        lotteryDraw.setPointDetailId(pointDetail.getId());
        lotteryDraw.setTotalPoint(useableAmount);
        lotteryDraw.setOrderIdentifier(orderIdentifier);
        lotteryDraw.setPointType(pointDetail.getPointType());
        lotteryDraw.setUseTime(new Date());

        lotteryDrawDao.persist(lotteryDraw);
        return lotteryDraw;
    }

    /**
     * 修改用户的积分
     *
     * @param detail    积分明细对象
     * @param newAmount 消耗积分
     */
    private void updatePointDetail(UserPointDetail detail, Integer newAmount) {
        UserPointDetail pointDetail = lotteryDrawDao.getEntityById(UserPointDetail.class, detail.getId());
        pointDetail.setUseableAmount(pointDetail.getUseableAmount() - newAmount);
        lotteryDrawDao.merge(pointDetail);
    }

    /**
     * 添加用户积分消耗明细
     *
     * @param userPointDetail   积分明细对象
     * @param useableAmount 消耗积分
     * @param lotteryDraw   抽奖记录对象
     */
    private void insertConsume(UserPointDetail userPointDetail, Integer useableAmount, LotteryDraw lotteryDraw) {
        UserConsumeDetail consume = new UserConsumeDetail();
        consume.setConsumeTargetId(lotteryDraw.getId());
        consume.setUserPointDetailId(userPointDetail.getId());
        consume.setPointType(userPointDetail.getPointType());
        consume.setAmount(useableAmount);
        consume.setConsumeType(ConsumeTypeEnum.LUCKY_DRAW);
        lotteryDrawDao.persist(consume);
    }

    /**
     * 添加用户所抽中的奖项，并扣除商家相应积分
     *
     * @param purchaseListP   商家认购积分集合
     * @param prize           奖项
     * @param user            用户
     * @param pointTypeEnum   积分类型
     * @param orderIdentifier 编号
     * @return
     */
    private LotteryDraw comparison(List<PointPurchase> purchaseListP, Integer prize, User user, PointTypeEnum pointTypeEnum, String orderIdentifier) {
        Integer newAmount = prize;
        Long purchaseId = purchaseListP.get(0).getId(); //此处 ：默认账户 必需要有一条认购积分记录
        LotteryDraw lotteryDraw = null;
        if (purchaseListP.size() > 0 && purchaseListP != null) {
            for (PointPurchase purchase : purchaseListP) {
                purchaseId = purchase.getId();
//                prize = purchase.getUseableAmount() - newAmount;//如果结果为正数、第一批积分就够用，则反之
//                if (prize < 0) {
//                    Integer deduction = newAmount - purchase.getUseableAmount();
//                    newAmount = deduction;
//                    insertPointDetail(purchase, user, purchase.getUseableAmount());//添加用户抽奖所得的积分
//                    lotteryDraw = insertLotteryDraw(user, purchase, purchase.getUseableAmount(), orderIdentifier); //添加抽中奖记录
//                    insertOrganizationConsumeDetail(lotteryDraw.getId(), purchase, pointTypeEnum, purchase.getUseableAmount());//添加商家消耗明细
//                    updatePurchase(purchase, purchase.getUseableAmount());
//                } else {
//                    insertPointDetail(purchase, user, newAmount);//添加用户抽奖所得的积分
//                    lotteryDraw = insertLotteryDraw(user, purchase, newAmount, orderIdentifier); //添加抽中奖记录
//                    insertOrganizationConsumeDetail(lotteryDraw.getId(), purchase, pointTypeEnum, newAmount);  //添加商家消耗明细
//                    updatePurchase(purchase, newAmount);
//                    return lotteryDraw;
//                }
            }
        }
        /**如果商家积分不足，则积分余额  为负数*/
        if (newAmount > 0) {
            PointPurchase purchase = lotteryDrawDao.getEntityById(PointPurchase.class, purchaseId);
            insertPointDetail(purchase, user, newAmount);//添加用户抽奖所得的积分
            lotteryDraw = insertLotteryDraw(user, purchase, newAmount, orderIdentifier); //添加抽中奖记录
            insertOrganizationConsumeDetail(lotteryDraw.getId(), purchase, pointTypeEnum, newAmount);  //添加商家消耗明细
            updatePurchase(purchase, newAmount);
        }
        return lotteryDraw;
    }

    /**
     * 添加商家积分消耗明细
     *
     * @param id
     * @param purchase
     * @param pointTypeEnum
     * @param useableAmount
     */
    private void insertOrganizationConsumeDetail(Long id, PointPurchase purchase, PointTypeEnum pointTypeEnum, Integer useableAmount) {
        OrganizationConsumeDetail organizationConsumeDetail = new OrganizationConsumeDetail();
        organizationConsumeDetail.setConsumeType(ConsumeTypeEnum.LUCKY_DRAW);
        organizationConsumeDetail.setPointType(pointTypeEnum);
        organizationConsumeDetail.setConsumeTargetId(id);
        organizationConsumeDetail.setPointPurchaseId(purchase.getId());
        organizationConsumeDetail.setAmount(useableAmount);

        lotteryDrawDao.persist(organizationConsumeDetail);
    }

    /**
     * 更新商家积分
     *
     * @param purchase 认购积分对象
     * @param prize    扣除积分
     */
    private void updatePurchase(PointPurchase purchase, Integer prize) {
        PointPurchase pointPurchase = lotteryDrawDao.getEntityById(PointPurchase.class, purchase.getId());
//        pointPurchase.setUseableAmount(pointPurchase.getUseableAmount() - prize);
        lotteryDrawDao.merge(pointPurchase);
    }

    /**
     * 添加商家扣除奖项 记录
     *
     * @param user            用户
     * @param purchase        认购积分对象
     * @param newAmount       积分数量
     * @param orderIdentifier 编号
     * @return
     */
    private LotteryDraw insertLotteryDraw(User user, PointPurchase purchase, Integer newAmount, String orderIdentifier) {
        LotteryDraw lotteryDraw = new LotteryDraw();
        lotteryDraw.setUserId(user.getId());
        lotteryDraw.setUseTime(new Date());
        lotteryDraw.setPointPurchaseId(purchase.getId());
        lotteryDraw.setPrize(newAmount);
        lotteryDraw.setOrderIdentifier(orderIdentifier);
        lotteryDraw.setPointType(purchase.getPointType());

        lotteryDrawDao.persist(lotteryDraw);
        return lotteryDraw;
    }

    /**
     * 添加用户所得奖项积分明细
     *
     * @param purchase      认购积分对象
     * @param user          用户
     * @param useableAmount 积分数量
     * @return
     */
    private UserPointDetail insertPointDetail(PointPurchase purchase, User user, Integer useableAmount) {
        UserPointDetail pointDetail = new UserPointDetail();
        pointDetail.setUserId(user.getId());
        pointDetail.setPointPurchaseId(purchase.getId());
        pointDetail.setPointType(purchase.getPointType());
        pointDetail.setArea("00:00");
//        pointDetail.setSource(PointSourceEnum.LUCKY_DRAW);
        pointDetail.setAmount(useableAmount);
        pointDetail.setUseableAmount(useableAmount);
        pointDetail.setExpiryTime(purchase.getExpiryTime());
        pointDetail.setOrganizationId(purchase.getOrganizationId());

        lotteryDrawDao.persist(pointDetail);
        return pointDetail;
    }
}