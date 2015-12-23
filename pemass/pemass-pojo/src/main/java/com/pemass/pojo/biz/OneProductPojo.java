package com.pemass.pojo.biz;

import com.pemass.common.core.pojo.Body;
import com.pemass.pojo.serializer.ResourceUrlSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * @Description: ProductController
 * @Author: huang zhuo
 * @CreateTime: 2014-11-10 11:40
 */

public class OneProductPojo {

    private Long id;//ID

    private String productIdentifier;//商品批号

    private String productName;//商品名称

    private List<Body> productDetail;//详情

    private Integer stockNumber;//库存数量【-1不限库存】

    private Double marketPrice;//市场价

    private Double outPrice;//对外售价

    private Double basePrice;//底价

    private Double onePrice;//一元购价格

    private Integer onePoint;//一元购积分

    private Integer isReturn;//是否退货【0-不退、1-可退】

    private Integer isInvoice;//是否开发票【0-不可开、1-可以开】

    private String location; //省市区地址(如：四川省成都市高新区)

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String previewImage;  //商品预览图(图片的URL相对地址)

    private Integer timeLimit;//还款期限（单位：年）


   //======================== getter and setter ====================================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getProductIdentifier() {
        return productIdentifier;
    }

    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<Body> getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(List<Body> productDetail) {
        this.productDetail = productDetail;
    }

    public Integer getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(Integer stockNumber) {
        this.stockNumber = stockNumber;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Double getOutPrice() {
        return outPrice;
    }

    public void setOutPrice(Double outPrice) {
        this.outPrice = outPrice;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getOnePrice() {
        return onePrice;
    }

    public void setOnePrice(Double onePrice) {
        this.onePrice = onePrice;
    }

    public Integer getOnePoint() {
        return onePoint;
    }

    public void setOnePoint(Integer onePoint) {
        this.onePoint = onePoint;
    }

    public Integer getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Integer isReturn) {
        this.isReturn = isReturn;
    }

    public Integer getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(Integer isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
}
