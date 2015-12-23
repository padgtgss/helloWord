package com.pemass.service.pemass.poi.impl;

import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;
import com.pemass.service.pemass.poi.CollectMoneyStrategyService;
import common.AbstractPemassServiceTest;
import org.testng.annotations.Test;

import javax.annotation.Resource;

public class CollectMoneyStrategyServiceImplTest extends AbstractPemassServiceTest {

    @Resource
    private CollectMoneyStrategyService collectMoneyStrategyService;

    @Test
    public void testSelectByTerminalUserId() throws Exception {
        CollectMoneyStrategySnapshot snapshot = collectMoneyStrategyService.selectByTerminalUserId(12L);
        System.out.println(snapshot);
    }
}