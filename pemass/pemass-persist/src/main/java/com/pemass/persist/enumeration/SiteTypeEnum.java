package com.pemass.persist.enumeration;

/**
 * @Description: 营业点类型
 * @Author: estn.zuo
 * @CreateTime: 2014-10-13 15:48
 */
public enum SiteTypeEnum {

    /**
     * TRAVEL_AGENCY-旅行社
     */
    TRAVEL_AGENCY("旅行社"),
    /**
     *  SCENIC_SPOT-景点/景区
     */
    SCENIC_SPOT("景点/景区"),
    /**
     *  HOTEL-酒店
     */
    HOTEL("酒店"),
    /**
     *  SHOPPING_MARKET-商城商圈
     */
    SHOPPING_MARKET("商城商圈"),
    /**
     *  DINING_ROOM-餐厅;
     */
    DINING_ROOM("餐厅");
    private String description;

    SiteTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
