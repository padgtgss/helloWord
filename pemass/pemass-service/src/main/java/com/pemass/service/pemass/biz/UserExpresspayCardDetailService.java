package com.pemass.service.pemass.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.UserExpresspayCardDetail;

/**
 * @author luoc
 * @Description: ${todo}
 * @date 2014/10/13
 */
public interface UserExpresspayCardDetailService {

    /**
     * 获取用户圈存明细
     *
     * @param uid
     * @return
     */
    DomainPage selectDetailByUid(Long uid,Long pageIndex, Long pageSize);


    /**
     * 用户圈存
     *
     * @param uid
     * @param pointEAmount
     * @return
     */
    UserExpresspayCardDetail deposit(Long uid, Integer pointEAmount);


    /**
     * 增加明细
     *
     * @param uid
     * @param pointEAmount
     * @return
     */
    UserExpresspayCardDetail insertDetail(Long uid, Integer pointEAmount);

    /**
     * 获取用户的 圈存总额
     * @param uid
     * @return
     */
    Float getDepositMoneyCount(Long uid);
}
