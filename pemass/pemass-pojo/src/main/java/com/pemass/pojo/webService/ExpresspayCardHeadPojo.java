/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.webService;

/**
 * @Description: ExpresspayCardHeadPojo
 * @Author: lin.shi
 * @CreateTime: 2015-09-14 22:28
 */
public class ExpresspayCardHeadPojo {

    private String merchantId;        //合作机构ID
    private String merchantSeq;      //合作机构流水号
    private String packetSignaTure;  //报文签名
    private String txnCode;          //交易代码
    private String txnDate;          //交易日期
    private String txnTime;         //交易时间
    private String version;         //版本号  "0000"
    private String platformSeq;     //平台流水号
    private String errCode;         //交易结果码
    private String errMsg;          //交易结果描述




    //===================getter and setter=========================

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantSeq() {
        return merchantSeq;
    }

    public void setMerchantSeq(String merchantSeq) {
        this.merchantSeq = merchantSeq;
    }

    public String getPacketSignaTure() {
        return packetSignaTure;
    }

    public void setPacketSignaTure(String packetSignaTure) {
        this.packetSignaTure = packetSignaTure;
    }

    public String getTxnCode() {
        return txnCode;
    }

    public void setTxnCode(String txnCode) {
        this.txnCode = txnCode;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlatformSeq() {
        return platformSeq;
    }

    public void setPlatformSeq(String platformSeq) {
        this.platformSeq = platformSeq;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}