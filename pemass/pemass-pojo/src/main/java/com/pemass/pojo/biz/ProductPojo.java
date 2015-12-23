package com.pemass.pojo.biz;

import com.pemass.pojo.serializer.ResourceUrlSerializer;
import com.pemass.pojo.sys.BodyPojo;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * @Description: ProductController
 * @Author: huang zhuo
 * @CreateTime: 2014-11-10 11:40
 */

public class ProductPojo {
    private Long id;

    private String productName;//商品名称

    private List<BodyPojo> productDetail;//详情

    private Integer stockNumber;//库存数量【-1不限库存】

    private Double marketPrice;//市场价

    private Double poundage;//手续费

    private Double outPrice;//对外售价

    private Integer givingPnumber;//赠送E积分数量

    private Integer givingEnumber;//赠送E通币数量

    private Integer givingOnumber;//赠送一元购积分数量

    private Integer deductionPoint;//积分抵扣数

    private Integer isReturn;//是否退货【0-不退、1-可退】

    private Integer isInvoice;//是否开发票【0-不可开、1-可以开】

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String previewImage;  //商品预览图(图片的URL相对地址)

    private Integer isTimePeriod;//电子票是否为时间段(-1、实体商品，0、不是，1、是)

    private Integer expiryPeriod;//该商品形成订单的过期时间，使用过期时间段来描述(单位：天)

    private Date expiryPointTime;//该商品形成订单的过期时间，使用过期时间点(商品具体哪天过期时间点)

    private Double onePrice;//一元购价格

    private Integer isCashback;//是否返现(0、不设置返现；1、设置返现)

    private Double cashbackRatio;  //返现比例

    private Double loanPrice;//一元购贷款价格

    private List<ProductImagePojo> productImagePojoList;//商品图片

    //======================== getter and setter ====================================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<BodyPojo> getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(List<BodyPojo> productDetail) {
        this.productDetail = productDetail;
    }

    public Integer getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(Integer stockNumber) {
        this.stockNumber = stockNumber;
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

    public Integer getIsTimePeriod() {
        return isTimePeriod;
    }

    public void setIsTimePeriod(Integer isTimePeriod) {
        this.isTimePeriod = isTimePeriod;
    }

    public Integer getExpiryPeriod() {
        return expiryPeriod;
    }

    public void setExpiryPeriod(Integer expiryPeriod) {
        this.expiryPeriod = expiryPeriod;
    }

    public Date getExpiryPointTime() {
        return expiryPointTime;
    }

    public void setExpiryPointTime(Date expiryPointTime) {
        this.expiryPointTime = expiryPointTime;
    }

    public List<ProductImagePojo> getProductImagePojoList() {
        return productImagePojoList;
    }

    public void setProductImagePojoList(List<ProductImagePojo> productImagePojoList) {
        this.productImagePojoList = productImagePojoList;
    }

    public Integer getGivingOnumber() {
        return givingOnumber;
    }

    public void setGivingOnumber(Integer givingOnumber) {
        this.givingOnumber = givingOnumber;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Double getPoundage() {
        return poundage;
    }

    public void setPoundage(Double poundage) {
        this.poundage = poundage;
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
}
