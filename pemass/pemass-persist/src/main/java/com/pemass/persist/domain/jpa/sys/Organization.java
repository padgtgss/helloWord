package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.AccountRoleEnum;
import com.pemass.persist.enumeration.AuditStatusEnum;

import javax.persistence.*;

/**
 * Description: Organization
 * Author: zhou
 * CreateTime: 2014-10-11 11:03
 */
@Entity
@Table(name = "sys_organization")
public class Organization extends BaseDomain {

    @Column(name = "organization_identifier", length = 10, nullable = false)
    private String organizationIdentifier;  //商家编号

    @Column(name = "organization_name", length = 50)
    private String organizationName;  //组织名称

    @Column(name = "logo", length = 200)
    private String logo;  //logo(图片的URL相对地址)

    @Column(name = "organization_phone", length = 20)
    private String organizationPhone; //机构电话

    @Column(name = "business", length = 2000)
    private String business;     //经营内容

    @Column(name = "telephone", length = 20)
    private String telephone;   //验证手机

    @Column(name = "pay_password", length = 32)
    private String payPassword;  //支付密码

    @Column(name = "salt", length = 8)
    private String salt;       //盐值

    @Column(name = "account_role", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AccountRoleEnum accountRole;  //角色

    @Column(name = "audit_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AuditStatusEnum auditStatus; //审核状态

    @Column(name = "license" ,length = 200)
    private String license;//营业执照RUL

    @Column(name = "license_number" ,length = 50)
    private String licenseNumber;//营业执照证件号

    @Column(name = "tax_certificate" ,length = 200)
    private String taxCertificate;//税务登记RUL

    @Column(name = "tax_certificate_number" ,length = 50)
    private String taxCertificateNumber;//税务登记证件号

    @Column(name = "organization_certificate" ,length = 200)
    private String organizationCertificate;//组织机构代码证RUL

    @Column(name = "organization_certificate_number" ,length = 50)
    private String organizationCertificateNumber;//组织机构代码证件号

    @Column(name = "legal_name" ,length = 50)
    private String legalName;//法人姓名

    @Column(name = "legal_idcard" ,length = 50)
    private String legalIdcard;//法人身份证Id

    @Column(name = "legal_idcard_url" ,length = 200)
    private String legalIdcardUrl;//法人身份证正面URL

    @Column(name = "legal_idcard_back_url" ,length = 200)
    private String legalIdcardBackUrl;//法人身份证背面URL

    @Column(name = "location", length = 50)
    private String location; //地址

    @Column(name = "province_id")
    private Long provinceId; //省

    @Column(name = "city_id")
    private Long cityId;    //市

    @Column(name = "district_id")
    private Long districtId;    //区

    @Column(name = "entrustment_url",length = 200)
    private String entrustmentUrl;   //委托函（图片）

    @Column(name = " company_nature")
    private Integer companyNature; //公司性质(1、个人或 2、企业)

    @Column(name = " register_way")
    private Integer registerWay; //注册方式（1、个人或 2、代理商）

    @Column(name = " point_ratio")
    private Double  pointRatio;   //积分受理比例

    @Column(name = " min_cashback_ratio")
    private Double  minCashbackRatio;   //最低返现比例

    @Column(name = "one_audit_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AuditStatusEnum oneAuditStatus; //一元购积分审核状态

    @Column(name = " is_one_merchant")
    private Integer isOneMerchant; //是否受理一元购积分商户【加速商户】（0、不是 1、是）

    @Column(name = "city_service_id")
    private Long cityServiceId;   //所属城市服务商

    @Column(name = "channel_id")
    private Long channelId;   //所属渠道商/代理商

    @Column(name = "one_pay_audit_status")
    @Enumerated(EnumType.ORDINAL)
    private AuditStatusEnum onePayAuditStatus; //申请壹元购支付审核状态

    @Column(name = "area", length = 255)
    private String area;    //管辖区域(多个省份时，使用","分隔)

    @Column(name = "organization_category_id")
    private Long organizationCategoryId;//商户类型id

    @Column(name = "resource",length = 200)
    private String resource;//二维码路径;


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationPhone() {
        return organizationPhone;
    }

    public void setOrganizationPhone(String organizationPhone) {
        this.organizationPhone = organizationPhone;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public AccountRoleEnum getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(AccountRoleEnum accountRole) {
        this.accountRole = accountRole;
    }

    public AuditStatusEnum getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatusEnum auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getTaxCertificate() {
        return taxCertificate;
    }

    public void setTaxCertificate(String taxCertificate) {
        this.taxCertificate = taxCertificate;
    }

    public String getOrganizationCertificate() {
        return organizationCertificate;
    }

    public void setOrganizationCertificate(String organizationCertificate) {
        this.organizationCertificate = organizationCertificate;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getTaxCertificateNumber() {
        return taxCertificateNumber;
    }

    public void setTaxCertificateNumber(String taxCertificateNumber) {
        this.taxCertificateNumber = taxCertificateNumber;
    }

    public String getOrganizationCertificateNumber() {
        return organizationCertificateNumber;
    }

    public void setOrganizationCertificateNumber(String organizationCertificateNumber) {
        this.organizationCertificateNumber = organizationCertificateNumber;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalIdcard() {
        return legalIdcard;
    }

    public void setLegalIdcard(String legalIdcard) {
        this.legalIdcard = legalIdcard;
    }

    public String getLegalIdcardUrl() {
        return legalIdcardUrl;
    }

    public void setLegalIdcardUrl(String legalIdcardUrl) {
        this.legalIdcardUrl = legalIdcardUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getLegalIdcardBackUrl() {
        return legalIdcardBackUrl;
    }

    public void setLegalIdcardBackUrl(String legalIdcardBackUrl) {
        this.legalIdcardBackUrl = legalIdcardBackUrl;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEntrustmentUrl() {
        return entrustmentUrl;
    }

    public void setEntrustmentUrl(String entrustmentUrl) {
        this.entrustmentUrl = entrustmentUrl;
    }

    public Integer getCompanyNature() {
        return companyNature;
    }

    public void setCompanyNature(Integer companyNature) {
        this.companyNature = companyNature;
    }

    public Integer getRegisterWay() {
        return registerWay;
    }

    public void setRegisterWay(Integer registerWay) {
        this.registerWay = registerWay;
    }

    public Double getPointRatio() {
        return pointRatio;
    }

    public void setPointRatio(Double pointRatio) {
        this.pointRatio = pointRatio;
    }

    public Double getMinCashbackRatio() {
        return minCashbackRatio;
    }

    public void setMinCashbackRatio(Double minCashbackRatio) {
        this.minCashbackRatio = minCashbackRatio;
    }

    public Integer getIsOneMerchant() {
        return isOneMerchant;
    }

    public void setIsOneMerchant(Integer isOneMerchant) {
        this.isOneMerchant = isOneMerchant;
    }

    public String getOrganizationIdentifier() {
        return organizationIdentifier;
    }

    public void setOrganizationIdentifier(String organizationIdentifier) {
        this.organizationIdentifier = organizationIdentifier;
    }

    public AuditStatusEnum getOneAuditStatus() {
        return oneAuditStatus;
    }

    public void setOneAuditStatus(AuditStatusEnum oneAuditStatus) {
        this.oneAuditStatus = oneAuditStatus;
    }

    public Long getCityServiceId() {
        return cityServiceId;
    }

    public void setCityServiceId(Long cityServiceId) {
        this.cityServiceId = cityServiceId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public AuditStatusEnum getOnePayAuditStatus() {
        return onePayAuditStatus;
    }

    public void setOnePayAuditStatus(AuditStatusEnum onePayAuditStatus) {
        this.onePayAuditStatus = onePayAuditStatus;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Long getOrganizationCategoryId() {
        return organizationCategoryId;
    }

    public void setOrganizationCategoryId(Long organizationCategoryId) {
        this.organizationCategoryId = organizationCategoryId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
