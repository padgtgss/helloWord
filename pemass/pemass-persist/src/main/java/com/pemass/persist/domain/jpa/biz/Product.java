package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.core.pojo.Body;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.AuditStatusEnum;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * 商品
 *
 * @author 向阳
 */
@Entity
@Table(name = "biz_product")
public class Product extends BaseDomain {

    @Column(name = "organization_id")
    private Long organizationId;//所属账户

    @Column(name = "product_identifier", length = 50, nullable = false)
    private String productIdentifier;//商品批号

    @Column(name = "product_name", length = 50, nullable = false)
    private String productName;//商品名称

    @Column(name = "product_category_id", nullable = false)
    private Long productCategoryId;//所属商品类别

    @Type(type = "com.pemass.persist.type.BodyUserType")
    @Column(name = "product_detail", length = 4000)
    private List<Body> productDetail;//详情

    @Column(name = "parent_product_id", nullable = false)
    private Long parentProductId;//父商品ID

    @Column(name = "origin_product_id", nullable = false)
    private Long originProductId;//原始产品ID(即该商品的根商品ID)

    @Column(name = "path", length = 100, nullable = false)
    private String path;//路径

    @Column(name = "stock_number", nullable = false)
    private Integer stockNumber;//库存数量【-1不限库存】

    @Column(name = "is_distribution", nullable = false)
    private Integer isDistribution;//是否分销【0-不允许分销、1-允许分销】

    @Column(name = "lower_price", nullable = false)
    private Double lowerPrice;//下级分销价格

    @Column(name = "market_price", nullable = false)
    private Double marketPrice;//市场价

    @Column(name = "out_price", nullable = false)
    private Double outPrice;//对外售价

    @Column(name = "is_present_point", nullable = false)
    private Integer isPresentPoint;//是否赠送积分【0-不赠送、1-赠送】

    @Column(name = "giving_Pnumber", nullable = false)
    private Integer givingPnumber;//赠送E积分数量

    @Column(name = "giving_Enumber", nullable = false)
    private Integer givingEnumber;//赠送E通币数量

    @Column(name = "deduction_point", nullable = false)
    private Integer deductionPoint;//积分抵扣数

    @Column(name = "is_return", nullable = false)
    private Integer isReturn;//是否退货【0-不退、1-可退】

    @Column(name = "is_invoice", nullable = false)
    private Integer isInvoice;//是否开发票【0-不可开、1-可以开】

    @Column(name = "product_status", nullable = false)
    private Integer productStatus;//状态【0-商品初始化、1-商品未上架、2-商品上架、3-移除商品库】

    @Column(name = "one_product_status")
    private Integer oneProductStatus;//一元购上下架状态0-商品初始化1-商品未上架、2-商品上架、3-移除商品库】

    @Column(name = "preview_image", length = 200)
    private String previewImage;  //商品预览图(图片的URL相对地址)

    @Column(name = "sale_number", nullable = false)
    private Integer saleNumber;//销售数量

    @Column(name = "is_time_period")
    private Integer isTimePeriod;//电子票是否为时间段(0、不是，1、是)

    @Column(name = "expiry_period")
    private Integer expiryPeriod;//该商品形成订单的过期时间，使用过期时间段来描述(单位：天)

    @Column(name = "expiry_point_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryPointTime;//该商品形成订单的过期时间，使用过期时间点(商品具体哪天过期时间点)

    @Column(name = "share_content", length = 500)
    private String shareContent;  //分享内容

    @Column(name = "one_price")
    private Double onePrice;//一元购价格

    @Column(name = "base_price")
    private Double basePrice;//底价

    @Column(name = "is_cashback")
    private Integer isCashback;//是否返现(0、不设置返现；1、设置返现)

    @Column(name = "cashback_ratio")
    private Double cashbackRatio;  //返现比例

    @Column(name = "loan_price")
    private Double loanPrice;//一元购贷款本金

    @Column(name = "one_product_apply_status")
    @Enumerated(EnumType.ORDINAL)
    private AuditStatusEnum oneProductApplyStatus;//一元购商品申请状态（0、未审核，1、审核中，2、审核成功，3、审核失败）

    @Column(name = "charges_ratio")
    private Double chargesRatio;//佣金比例

    //============================ getter and setter ==========================\\

    public Integer getGivingPnumber() {
        return givingPnumber;
    }

    public void setGivingPnumber(Integer givingPnumber) {
        this.givingPnumber = givingPnumber;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(Integer stockNumber) {
        this.stockNumber = stockNumber;
    }

    public Integer getIsDistribution() {
        return isDistribution;
    }

    public void setIsDistribution(Integer isDistribution) {
        this.isDistribution = isDistribution;
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

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public Integer getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(Integer saleNumber) {
        this.saleNumber = saleNumber;
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

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public Integer getIsTimePeriod() {
        return isTimePeriod;
    }

    public void setIsTimePeriod(Integer isTimePeriod) {
        this.isTimePeriod = isTimePeriod;
    }

    public Integer getOneProductStatus() {
        return oneProductStatus;
    }

    public void setOneProductStatus(Integer oneProductStatus) {
        this.oneProductStatus = oneProductStatus;
    }


    public AuditStatusEnum getOneProductApplyStatus() {
        return oneProductApplyStatus;
    }

    public void setOneProductApplyStatus(AuditStatusEnum oneProductApplyStatus) {
        this.oneProductApplyStatus = oneProductApplyStatus;
    }

    public Integer getDeductionPoint() {
        return deductionPoint;
    }

    public void setDeductionPoint(Integer deductionPoint) {
        this.deductionPoint = deductionPoint;
    }

    public Integer getIsCashback() {
        return isCashback;
    }

    public void setIsCashback(Integer isCashback) {
        this.isCashback = isCashback;
    }

    public Double getLowerPrice() {
        return lowerPrice;
    }

    public void setLowerPrice(Double lowerPrice) {
        this.lowerPrice = lowerPrice;
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

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
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

    public Double getChargesRatio() {
        return chargesRatio;
    }

    public void setChargesRatio(Double chargesRatio) {
        this.chargesRatio = chargesRatio;
    }
}
