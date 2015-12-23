package com.pemass.cloudpos.api.controller.poi;

import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.poi.CollectMoneyScheme;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategy;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;
import com.pemass.persist.enumeration.CollectMoneySchemeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.pojo.poi.CollectMoneySchemePojo;
import com.pemass.pojo.poi.CollectMoneyStategyPojo;
import com.pemass.pojo.poi.CollectMoneyStrategySnapshotPojo;
import com.pemass.service.pemass.poi.CollectMoneySchemeService;
import com.pemass.service.pemass.poi.CollectMoneyStrategyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/strategy")
public class StrategyController {

    @Resource
    private CollectMoneyStrategyService collectMoneyStrategyService;

    @Resource
    private CollectMoneySchemeService collectMoneySchemeService;

    /**
     * 根据商户id,查询出商户的所有“策略”
     *
     * @param terminalUserId 收银员id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object selectByTerminalUserId(Long terminalUserId) {
        CollectMoneyStrategySnapshot snapshot = collectMoneyStrategyService.selectByTerminalUserId(terminalUserId);
        return MergeUtil.merge(snapshot, new CollectMoneyStrategySnapshotPojo());
    }

    /**
     * 查询策略明细
     *
     * @param collectMoneyStrategyId 策略id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/{collectMoneyStrategyId}", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@PathVariable("collectMoneyStrategyId") Long collectMoneyStrategyId) {

        CollectMoneyStrategy strategy = collectMoneyStrategyService.getByDetail(collectMoneyStrategyId);

        return detailMerge(strategy);
    }

    /**
     * 封装数据‘策略’详细
     *
     * @return
     */
    private CollectMoneyStategyPojo detailMerge(CollectMoneyStrategy strategy) {
        CollectMoneyStategyPojo moneyStrategy = new CollectMoneyStategyPojo();

        moneyStrategy.setStartTime(strategy.getStartTime());
        moneyStrategy.setEndTime(strategy.getEndTime());
        moneyStrategy.setCollectMoneyDiscount(strategy.getDiscount() == null ? null : strategy.getDiscount());
        moneyStrategy.setPaiDeduction(strategy.getIsDeductionPointP());
        moneyStrategy.setIsBeiDeduction(strategy.getIsDeductionPointE());
        moneyStrategy.setSchemePojoP(schemePojoPMerge(strategy));
        moneyStrategy.setSchemePojoE(schemePojoEMerge(strategy));

        return moneyStrategy;
    }

    /**
     * 解析'派'积分类型方案
     *
     * @param strategy
     * @return
     */
    private CollectMoneySchemePojo schemePojoPMerge(CollectMoneyStrategy strategy) {
        CollectMoneySchemePojo pojo = new CollectMoneySchemePojo();
        CollectMoneyScheme paiCollectMoneyScheme = null;
        if (strategy.getPointPSchemeId() != null) {
            paiCollectMoneyScheme = collectMoneySchemeService.getById(strategy.getPointPSchemeId());
        }
        if (paiCollectMoneyScheme != null && PointTypeEnum.P.equals(paiCollectMoneyScheme.getPointType())) {
            pojo = (show(paiCollectMoneyScheme));
        }
        return pojo;
    }

    /**
     * 解析'贝'积分类型方案
     *
     * @param strategy
     * @return
     */
    private CollectMoneySchemePojo schemePojoEMerge(CollectMoneyStrategy strategy) {
        CollectMoneySchemePojo pojo = new CollectMoneySchemePojo();
        CollectMoneyScheme beiCollectMoneyScheme = null;
        if (strategy.getPointESchemeId() != null) {
            beiCollectMoneyScheme = collectMoneySchemeService.getById(strategy.getPointESchemeId());
        }
        if (beiCollectMoneyScheme != null && PointTypeEnum.E.equals(beiCollectMoneyScheme.getPointType())) {
            pojo = (show(beiCollectMoneyScheme));
        }
        return pojo;
    }

    /**
     * 根据方案封装数据
     *
     * @param moneyScheme
     * @return
     */
    private CollectMoneySchemePojo show(CollectMoneyScheme moneyScheme) {
        CollectMoneySchemePojo schemePojoE = new CollectMoneySchemePojo();

        if (CollectMoneySchemeEnum.SHOPPING_AMOUNT.equals(moneyScheme.getSchemeType())) {
            schemePojoE.setMiniAmount(moneyScheme.getMiniAmount());
            schemePojoE.setMiniGiveAmount(moneyScheme.getMiniGiveAmount());
        }
        if (CollectMoneySchemeEnum.PERCENTAGE.equals(moneyScheme.getSchemeType())) {
            schemePojoE.setConversionFactor(moneyScheme.getConversionFactor());
        }
        if (CollectMoneySchemeEnum.IMMOBILIZATION.equals(moneyScheme.getSchemeType())) {
            schemePojoE.setImmobilizationPresented(moneyScheme.getImmobilizationPresented());
        }
        schemePojoE.setSchemeType(moneyScheme.getSchemeType().toString());
        return schemePojoE;
    }

}