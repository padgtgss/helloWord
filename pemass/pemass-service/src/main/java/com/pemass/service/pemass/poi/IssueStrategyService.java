package com.pemass.service.pemass.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.IssueStrategy;
import com.pemass.persist.domain.jpa.poi.StrategyContent;

import java.util.List;
import java.util.Map;

/**
 * 积分红包营销策略
 * Created by hejch on 2014/12/3.
 */
public interface IssueStrategyService {

    /**
     * 获取满足条件的积分营销策略
     *
     * @param conditions
     * @param fuzzyConditions
     * @param domainPage
     * @return
     */
    DomainPage getIssueStrategyByCondition(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage);

    /**
     * 新增积分营销策略
     *
     * @param issueStrategy
     * @param strategyContents
     */
    void insert(IssueStrategy issueStrategy, List<StrategyContent> strategyContents);

    /**
     * 查询所有策略
     *
     * @return
     */
    List selectAllIssueStrategy();

    /**
     * 删除策略实体
     *
     * @param issueStrategyId
     */
    void deleteIssueStrategy(Long issueStrategyId);

    /**
     * 撤销红包广场展示(或不展示)红包，还有无人机策略使用的红包
     *
     * @param issueStrategyId
     */
    void repealPresentPackPresent(Long issueStrategyId);

    /**
     * 根据策略获取策略类容
     *
     * @param issueStrategy
     * @return
     */
    List<StrategyContent> getStrategyContent(IssueStrategy issueStrategy);

    /**
     * 查询发放到红包广场 的 策略
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage findById(long pageIndex, long pageSize);


    /**
     * 根据Id获取IssueStrategy
     *
     * @param issueStrategyId
     * @return
     */
    IssueStrategy selectIssueStrategyById(Long issueStrategyId);
}
