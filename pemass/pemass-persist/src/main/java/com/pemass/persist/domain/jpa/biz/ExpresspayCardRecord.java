/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.biz.ExpresspayCard;
import com.pemass.persist.domain.jpa.ec.Invoice;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;

import javax.persistence.*;

/**
 * @Description: 中银通记录表
 * @Author: zhou hang
 * @CreateTime: 2015-04-14 16:27
 */
@Entity
@Table(name = "biz_expresspay_card_record")
public class ExpresspayCardRecord extends BaseDomain {

    @Column(name = "expresspay_card_id",nullable = false)
    private Long expresspayCardId;//中银通卡号id

    @Column(name = "cashier_user_id",nullable = false)
    private Long cashierUserId;//收银员id

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;//金额

    @Column(name = "service_charge")
    private Double serviceCharge;//服务费

    @Column(name = "username", nullable = false, length = 50)
    private String userNmae;//姓名

    @Column(name = "telephone", nullable = false, length = 50)
    private String telephone;//手机

    @Column(name = "legal_idcard" ,length = 50)
    private String legalIdcard;//法人身份证号码

    @Column(name = "legal_idcard_url", length = 200)
    private String legalIdcardUrl;//法人身份证正面URL

    @Column(name = "legal_idcard_url_back", length = 200)
    private String legalIdcardUrlBack;//法人身份证背面URL

    @Column(name = "invoice_id")
    private Long invoiceId;//发票

    //===================================getter  setter=======================


    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getUserNmae() {
        return userNmae;
    }

    public void setUserNmae(String userNmae) {
        this.userNmae = userNmae;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getLegalIdcardUrlBack() {
        return legalIdcardUrlBack;
    }

    public void setLegalIdcardUrlBack(String legalIdcardUrlBack) {
        this.legalIdcardUrlBack = legalIdcardUrlBack;
    }

    public Long getExpresspayCardId() {
        return expresspayCardId;
    }

    public void setExpresspayCardId(Long expresspayCardId) {
        this.expresspayCardId = expresspayCardId;
    }

    public Long getCashierUserId() {
        return cashierUserId;
    }

    public void setCashierUserId(Long cashierUserId) {
        this.cashierUserId = cashierUserId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
}