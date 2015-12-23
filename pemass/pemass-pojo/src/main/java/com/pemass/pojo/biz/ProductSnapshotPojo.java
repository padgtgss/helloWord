/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.pojo.biz;

import com.pemass.pojo.serializer.ResourceUrlSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @Description: ProductSnapshotPojo
 * @Author: estn.zuo
 * @CreateTime: 2015-07-16 10:12
 */
public class ProductSnapshotPojo {

    private String productName;//商品名称

    private Long parentProductId;//父商品ID

    private Long originProductId;//原始产品ID(即该商品的根商品ID)

    private Double marketPrice;//市场价

    private Double outPrice;//对外售价

    private Integer isPresentPoint;//是否赠送积分【0-不赠送、1-赠送】

    private Integer givingPnumber;//赠送E积分数量

    private Integer givingEnumber;//赠送E通币数量

    private Integer deductionPoint;//积分抵扣数

    private Integer isReturn;//是否退货【0-不退、1-可退】

    private Integer isInvoice;//是否开发票【0-不可开、1-可以开】

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String previewImage;  //商品预览图(图片的URL相对地址)

    private String shareContent;  //分享内容

    private Double onePrice;//一元购价格

    private Integer isCashback;//是否返现(0、不设置返现；1、设置返现)

    private Double cashbackRatio;  //返现比例

    private Double loanPrice;//一元购贷款价格

    private Double poundage;//手续费


    //=======================================getter and setter=====================================================



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getParentProductId() {
        return parentProductId;
    }

    public void setParentProductId(Long parentProductId) {
        this.parentProductId = parentProductId;
    }

    public Long getOriginProductId() {
        return originProductId;
    }

    public void setOriginProductId(Long originProductId) {
        this.originProductId = originProductId;
    }

    public Integer getIsPresentPoint() {
        return isPresentPoint;
    }

    public void setIsPresentPoint(Integer isPresentPoint) {
        this.isPresentPoint = isPresentPoint;
    }

    public Integer getGivingPnumber() {
        return givingPnumber;
    }

    public void setGivingPnumber(Integer givingPnumber) {
        this.givingPnumber = givingPnumber;
    }

    public Integer getGivingEnumber() {
        return givingEnumber;
    }

    public void setGivingEnumber(Integer givingEnumber) {
        this.givingEnumber = givingEnumber;
    }

    public Integer getDeductionPoint() {
        return deductionPoint;
    }

    public void setDeductionPoint(Integer deductionPoint) {
        this.deductionPoint = deductionPoint;
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

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }


    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
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

    public Double getOnePrice() {
        return onePrice;
    }

    public void setOnePrice(Double onePrice) {
        this.onePrice = onePrice;
    }

    public Integer getIsCashback() {
        return isCashback;
    }

    public void setIsCashback(Integer isCashback) {
        this.isCashback = isCashback;
    }

    public Double getCashbackRatio() {
        return cashbackRatio;
    }

    public void setCashbackRatio(Double cashbackRatio) {
        this.cashbackRatio = cashbackRatio;
    }

    public Double getLoanPrice() {
        return loanPrice;
    }

    public void setLoanPrice(Double loanPrice) {
        this.loanPrice = loanPrice;
    }

    public Double getPoundage() {
        return poundage;
    }

    public void setPoundage(Double poundage) {
        this.poundage = poundage;
    }
}
