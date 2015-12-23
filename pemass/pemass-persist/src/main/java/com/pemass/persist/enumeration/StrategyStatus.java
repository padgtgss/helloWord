package com.pemass.persist.enumeration;

/**
 * 积分红包发放策略以及把红包放到红包广场的执行状态
 * Created by hejch on 2015/2/9.
 */
public enum StrategyStatus {

    NON_EXECUTION("未执行"),
    EXECUTED("已执行"),
    REPEAL("已撤销");

    private String description;

    StrategyStatus(String ds) {
        this.description = ds;
    }

    public String getDescription() {
        return this.description;
    }

}