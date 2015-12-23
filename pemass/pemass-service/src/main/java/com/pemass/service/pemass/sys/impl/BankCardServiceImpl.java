/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.sys.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.core.util.AESUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.sys.BankCardDao;
import com.pemass.persist.domain.jpa.ec.Clearing;
import com.pemass.persist.domain.jpa.sys.BankCard;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.ClearSourceEnum;
import com.pemass.persist.enumeration.SettlementRoleEnum;
import com.pemass.persist.enumeration.TransactionAccountTypeEnum;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.pemass.sys.BankCardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description: BankCardServiceImpl
 * @Author: vahoa.ma
 * @CreateTime: 2015-08-03 17:33
 */
@Service
public class BankCardServiceImpl implements BankCardService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private BankCardDao bankCardDao;

    @Override
    public BankCard saveOrUpdateBankCard(String bankCardInfo,BankCard bankCard) {
        Preconditions.checkNotNull(bankCard); //如果这卡为空，则抛出异常

        /**加密银行卡号和证件号*/
        if (bankCard.getCardNo() != null) {
            String cardNo = AESUtil.encrypt(bankCard.getCardNo(), PemassConst.AES_CIPHER);
            bankCard.setCardNo(cardNo);
        }
        if (bankCard.getCertNo() != null) {
            String certNo = AESUtil.encrypt(bankCard.getCertNo(), PemassConst.AES_CIPHER);
            bankCard.setCertNo(certNo);
        }
        if (bankCardInfo != null &&bankCardInfo != "") {
            String[] result = bankCardInfo.split(",");
            String bankName = result[0];//开户行名称
            String bankId = result[1];//开户ID
            bankCard.setBankName(bankName);
            bankCard.setBankId(bankId);
        }
        /**保存或更新数据*/
        BankCard primeBankCard = this.getBankCard(bankCard.getTargetUUID());
        if (primeBankCard != null) {
            MergeUtil.merge(bankCard, primeBankCard);
            jpaBaseDao.merge(primeBankCard);
            return primeBankCard;
        } else {
            bankCard.setCreateTime(new Date());
            bankCard.setCpAuditStatus(AuditStatusEnum.ING_AUDIT);
            jpaBaseDao.persist(bankCard);
            return bankCard;
        }
    }

    @Override
    public BankCard getBankCardByTargetUUID(String targetUUID) {
        BankCard bankCard = this.getBankCard(targetUUID);
        if (bankCard != null) {
            /*解密银行卡号*/
            bankCard.setCardNo(AESUtil.decrypt(bankCard.getCardNo(), PemassConst.AES_CIPHER));
            /*解密证件号*/
            bankCard.setCertNo(AESUtil.decrypt(bankCard.getCertNo(), PemassConst.AES_CIPHER));
        }
        return bankCard;
    }

    @Override
    public BankCard getBankCardById(Long id) {
        BankCard bankCard = jpaBaseDao.getEntityById(BankCard.class, id);
        /*解密银行卡号*/
        bankCard.setCardNo(AESUtil.decrypt(bankCard.getCardNo(), PemassConst.AES_CIPHER));
        /*解密证件号*/
        bankCard.setCertNo(AESUtil.decrypt(bankCard.getCertNo(), PemassConst.AES_CIPHER));
        return bankCard;
    }

    /**
     * 获取BankCard
     *
     * @param targetUUID
     * @return
     */
    private BankCard getBankCard(String targetUUID) {
        return jpaBaseDao.getEntityByField(BankCard.class, "targetUUID", targetUUID);
    }

    @Override
    public BankCard getBankCardByBankId(Long id) {
        return jpaBaseDao.getEntityById(BankCard.class, id);
    }

    @Override
    public BankCard saveBankCard(String bankCardInfo, BankCard bankCard) {
        Preconditions.checkNotNull(bankCard); //如果这卡为空，则抛出异常

        /**加密银行卡号和证件号*/
        if (bankCard.getCardNo() != null) {
            String cardNo = AESUtil.encrypt(bankCard.getCardNo(), PemassConst.AES_CIPHER);
            bankCard.setCardNo(cardNo);
        }
        if (bankCard.getCertNo() != null) {
            String certNo = AESUtil.encrypt(bankCard.getCertNo(), PemassConst.AES_CIPHER);
            bankCard.setCertNo(certNo);
        }
        if (bankCardInfo != null &&bankCardInfo != "") {
            String[] result = bankCardInfo.split(",");
            String bankName = result[0];//开户行名称
            String bankId = result[1];//开户ID
            bankCard.setBankName(bankName);
            bankCard.setBankId(bankId);
        }
        /**保存数据*/
        bankCard.setCpAuditStatus(AuditStatusEnum.ING_AUDIT);
        bankCardDao.persist(bankCard);
        return bankCard;
    }

    @Override
    public BankCard getBankCardTestInfo(BankCard bankCard, Organization organization) {
        /**增加清分明细**/
        saveClearing(bankCard,organization);

        return null;
    }


    private BankCard getNoBankCardByTargetUUID(String targetUUID){
        BankCard bankCard = bankCardDao.getEntityByField(BankCard.class, "targetUUID", targetUUID);
        if (bankCard != null) {
            /*解密银行卡号*/
            bankCard.setCardNo(AESUtil.decrypt(bankCard.getCardNo(), PemassConst.AES_CIPHER));
            /*解密证件号*/
            bankCard.setCertNo(AESUtil.decrypt(bankCard.getCertNo(), PemassConst.AES_CIPHER));
        }
        return bankCard;
    }


    private void saveClearing(BankCard bankCard,Organization organization) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
        Clearing clearing = new Clearing();
        clearing.setClearSource(ClearSourceEnum.SERVICE_PRICE);
        clearing.setSourceTargetId(bankCard.getId());
        BankCard outgoBankCard = this.getNoBankCardByTargetUUID(organization.getUuid());

        if (outgoBankCard == null){
            clearing.setOutgoBankCardId(0L);
        }else {
            clearing.setOutgoBankCardId(outgoBankCard.getId());
        }


//        Company totalCompany = companyService.getGroupCompanyInfo();
//        BankCard incomeBankCard = bankCardService.getBankCardByTargetUUID(totalCompany.getUuid());
//        if (incomeBankCard == null) {
//            flag = 3;
//            clearing.setIncomeBankCardId(0L);
//        } else {
//            clearing.setIncomeBankCardId(incomeBankCard.getId());
//        }

        clearing.setIncomeBankCardId(0L);
        clearing.setAmount(2.0d);//金额为认购的E通币数量
        clearing.setIsSettle(0);
        clearing.setSettlementRole(SettlementRoleEnum.POINT_E_PRINCIPAL);
        clearing.setSettlementRoleTargetId(organization.getId());
        clearing.setProvinceId(organization.getProvinceId());
        clearing.setCityId(organization.getCityId());
        clearing.setDistrictId(organization.getDistrictId());
        clearing.setTargetAmount(-2.0d);
        clearing.setTargetBankCardId(outgoBankCard.getId());
        clearing.setSettlementDate(calendar.getTime());
        clearing.setTransactionAccountTypeEnum(TransactionAccountTypeEnum.ORGANIZATION);
        clearing.setSettlementDate(new Date());
        jpaBaseDao.persist(clearing);
    }
}