package com.pemass.service.pemass.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.ConfigEnum;
import common.AbstractPemassServiceTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * Created by estn on 15/7/30.
 */
public class ConfigServiceTest extends AbstractPemassServiceTest {

    @Resource
    private ConfigService configService;

    @Test
    public void testSearch() throws Exception {
        DomainPage domainPage = configService.search(1, 10);
        Assert.assertNotNull(domainPage);
    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testGetConfigByKey() throws Exception {
        Config config = configService.getConfigByKey(ConfigEnum.CASHBACK_MIN_RATIO);
        Assert.assertNotNull(config);
    }

    @Test
    public void testInsert() throws Exception {
        Config config = new Config();
        config.setKey("IOS_AUDIT_STATUS");
        config.setValue("ING_AUDIT");
        config.setTitle("IOS版本发布状态");
        configService.insert(config);

//        config.setKey("POINT_MIN_RATIO");
//        config.setValue("0.01");
//        config.setTitle("积分受理比例");
//        configService.insert(config);
//
//        config.setKey("POINT_E_TAX_RATE");
//        config.setValue("0.01");
//        config.setTitle("返E通币税率");
//        configService.insert(config);
//
//        config.setKey("POINT_P_GIVING_MAX_RATIO");
//        config.setValue("0.5");
//        config.setTitle("随商品赠送积分比例");
//        configService.insert(config);
//
//        config.setKey("POINT_O_GIVING_RATIO");
//        config.setValue("3");
//        config.setTitle("壹购积分发放比例");
//        configService.insert(config);
//
//        config.setKey("CASHBACK_MIN_RATIO");
//        config.setValue("0.01");
//        config.setTitle("受理一元购返现比例1");
//        configService.insert(config);
//
//        config.setKey("LOAN_RATE");
//        config.setValue("0.04");
//        config.setTitle("贷款利率");
//        configService.insert(config);
//
//        config.setKey("PREPAYMENT_POUNDAGE");
//        config.setValue("0.1");
//        config.setTitle("提前还款手续费");
//        configService.insert(config);
//
//        config.setKey("POINT_RATE");
//        config.setValue("0.3");
//        config.setTitle("积分认购手续费换算率");
//        configService.insert(config);
//
//        config.setKey("ONE_SERVICE_RATIO");
//        config.setValue("0.01");
//        config.setTitle("壹元GO服务费率");
//        configService.insert(config);
//
//        config.setKey("JFT_ORGANIZATION_RATIO");
//        config.setValue("0.1");
//        config.setTitle("积分通总公司分润率");
//        configService.insert(config);
//
//        config.setKey("PROVINCE_ORGANIZATION_RATIO");
//        config.setValue("0.2");
//        config.setTitle("省公司总的分润率");
//        configService.insert(config);
//
//        config.setKey("SEND_POINT_PROVINCE_ORGANIZATION_RATIO");
//        config.setValue("0.5");
//        config.setTitle("发行积分所属省公司分润率");
//        configService.insert(config);
//
//        config.setKey("SEND_POINT_PROVINCE_AGENT_RATIO");
//        config.setValue("0.5");
//        config.setTitle("发行积分所属核心代理商分润率");
//        configService.insert(config);
//
//        config.setKey("SECOND_AGENT_RATIO");
//        config.setValue("0.8");
//        config.setTitle("二代分润率");
//        configService.insert(config);
//
//
//        config.setKey("DEPOSIR_POUNDAGE");
//        config.setValue("0.1");
//        config.setTitle("E通币圈存手续费");
//        configService.insert(config);
//
//        config.setKey("ORDER_CANCEL_INTERVAL_TIME");
//        config.setValue("5");
//        config.setTitle("自定义订单未支付取消时间");
//        configService.insert(config);
//
//        config.setKey("CLEARING_PROFIT_TIME");
//        config.setValue("10");
//        config.setTitle("T+N,每个月的10号执行");
//        configService.insert(config);
    }
}