package com.pemass.persist.enumeration;

/**
 * @Description: ConfigEnum
 * @Author: estn.zuo
 * @CreateTime: 2014-12-03 17:42
 */
public enum ConfigEnum {
    JFT_ORGANIZATION_ID("积分通公司商户ID"),
    LOAN_RATE("贷款利率"),
    CASHBACK_MIN_RATIO("系统受理一元购最低返现比例"),
    POINT_MIN_RATIO("系统积分最低受理比例"),
    POINT_O_GIVING_RATIO("壹购积分发放比例"),
    POINT_E_TAX_RATE("返E通币税率"),
    POINT_P_GIVING_MAX_RATIO("随商品赠送积分比例"),
    POINT_RATE("积分认购手续费换算率"),
    PREPAYMENT_POUNDAGE("提前还款手续费"),
    ONE_SERVICE_RATIO("壹元GO服务费率"),
    JFT_ORGANIZATION_RATIO("积分通总公司分润率"),
    PROVINCE_ORGANIZATION_RATIO("省公司总的分润率"),
    SEND_POINT_PROVINCE_ORGANIZATION_RATIO("发行积分所属省公司分润率"),
    SEND_POINT_PROVINCE_AGENT_RATIO("发行积分所属核心代理商分润率"),
    SECOND_AGENT_RATIO("二代分润率"),
    DEPOSIR_POUNDAGE("E通币圈存手续费"),
    ORDER_CANCEL_INTERVAL_TIME("自定义订单未支付取消时间"),
    CLEARING_PROFIT_TIME("利润分配时间"),
    STATISTICS_MAIL_USERNAME("每日数据统计发送邮件地址"),
    IOS_AUDIT_STATUS("IOS版本发布状态"),
    ;
    private String description;

    ConfigEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ConfigEnum getByDescription(String description) {
        for (ConfigEnum configEnum : ConfigEnum.values()) {
            if (configEnum.getDescription().equals(description)) {
                return configEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in ConfigEnum");
    }

    public static ConfigEnum getByKey(String key) {
        for (ConfigEnum configEnum : ConfigEnum.values()) {
            if (configEnum.toString().equals(key)) {
                return configEnum;
            }
        }
        throw new IllegalArgumentException(key + "Is not in ConfigEnum");
    }


}
