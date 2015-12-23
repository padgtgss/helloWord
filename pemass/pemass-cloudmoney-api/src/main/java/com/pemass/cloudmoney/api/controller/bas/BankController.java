/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.cloudmoney.api.controller.bas;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.util.BankCardBinUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 银行卡信息查询
 * @Author: estn.zuo
 * @CreateTime: 2015-08-06 15:37
 */
@Controller
@RequestMapping("/bank")
public class BankController {

    /**
     * 根据银行卡卡号查询发卡行
     *
     * @param bankAccount
     * @return
     */
    @RequestMapping(value = "/bin/{bankAccount}")
    @ResponseBody
    public Object getBankName(@PathVariable("bankAccount") String bankAccount) {
        Preconditions.checkNotNull(bankAccount);
        String bankName = BankCardBinUtil.getBankName(bankAccount);
        if (bankName == null) {
            bankName = "";
        }
        return ImmutableMap.of("bankName", bankName);
    }
}
