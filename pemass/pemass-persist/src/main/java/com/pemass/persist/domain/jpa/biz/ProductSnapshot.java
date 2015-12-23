package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;
/**
 * 商品快照表
 */
@Entity
@Table(name = "biz_product_snapshot")
public class ProductSnapshot extends BaseDomain {

    @Column(name = "organization_id")
    private Long organizationId;//所属账户

    @Column(name = "product_id")
    private Long productId;//产品ID 与产品表ID关联

    @Column(name = "parent_product_id")
    private Long parentProductId;//父商品ID

    @Column(name = "product_name")
    private String productName;//商品名称

    @Column(name = "market_price")
    private Double marketPrice;//市场价

    @Column(name = "out_price")
    private Double outPrice;//对外售价

    @Column(name = "giving_Pnumber")
    private Integer givingPnumber;//赠送E积分数量

    @Column(name = "giving_Enumber")
    private Integer givingEnumber;//赠送E通币数量

    @Column(name = "is_present_point")
    private Integer isPresentPoint;//是否赠送积分【0-不赠送、1-赠送】

    @Column(name = "deduction_point")
    private Integer deductionPoint;//积分抵扣数

    @Column(name = "preview_image", length = 200)
    private String previewImage;  //商品预览图(图片的URL相对地址)

    @Column(name = "is_cashback")
    private Integer isCashback;//是否返现(0、不设置返现；1、设置返现)

    @Column(name = "cashback_ratio")
    private Double cashbackRatio;  //返现比例

    @Column(name = "one_price")
    private Double onePrice;//一元购价格

    @Column(name = "loan_price")
    private Double loanPrice;//一元购贷款价格

    @Column(name = "base_price")
    private Double basePrice;//底价

    @Column(name = "share_content", length = 500)
    private String shareContent;  //分享内容


    //========================================getter and setter===========================================


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public Integer getIsPresentPoint() {
        return isPresentPoint;
    }

    public void setIsPresentPoint(Integer isPresentPoint) {
        this.isPresentPoint = isPresentPoint;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public Integer getDeductionPoint() {
        return deductionPoint;
    }

    public void setDeductionPoint(Integer deductionPoint) {
        this.deductionPoint = deductionPoint;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public Long getParentProductId() {
        return parentProductId;
    }

    public void setParentProductId(Long parentProductId) {
        this.parentProductId = parentProductId;
    }

    public Integer getIsCashback() {
        return isCashback;
    }

    public void setIsCashback(Integer isCashback) {
        this.isCashback = isCashback;
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

    public Double getCashbackRatio() {
        return cashbackRatio;
    }

    public void setCashbackRatio(Double cashbackRatio) {
        this.cashbackRatio = cashbackRatio;
    }

    public Double getOnePrice() {
        return onePrice;
    }

    public void setOnePrice(Double onePrice) {
        this.onePrice = onePrice;
    }

    public Double getLoanPrice() {
        return loanPrice;
    }

    public void setLoanPrice(Double loanPrice) {
        this.loanPrice = loanPrice;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }
}
