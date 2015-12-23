/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.sys;

import com.pemass.persist.domain.jpa.sys.BankCard;
import com.pemass.persist.domain.jpa.sys.Organization;

/**
 * @Description: BankCardService
 * @Author: vahoa.ma
 * @CreateTime: 2015-08-03 15:54
 */
public interface BankCardService {


    /**
     * 添加、更新 银行卡信息
     *
     * @param bankCard  银行卡信息
     */
    BankCard saveOrUpdateBankCard(String bankCardInfo,BankCard bankCard);

    /**
     * 根据UUID获取银行卡信息
     *
     * @param targetUUID
     * @return
     */
    BankCard getBankCardByTargetUUID(String targetUUID);

    /**
     * 根据ID获取银行卡信息
     *
     * @param id
     * @return
     */
    BankCard getBankCardById(Long id);

    BankCard getBankCardByBankId(Long id);

    BankCard saveBankCard(String bankCardInfo,BankCard bankCard);

    /**
     * 验证银行卡账户信息
     *
     * @param bankCard
     * @param organization
     * @return
     */
    BankCard getBankCardTestInfo(BankCard bankCard, Organization organization);
}