/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.bas.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.persist.dao.bas.StampDao;
import com.pemass.service.exception.SignError;
import com.pemass.service.pemass.bas.StampService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: StampServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2015-08-19 23:51
 */
@Service
public class StampServiceImpl implements StampService {

    @Resource
    private StampDao stampDao;

    @Override
    public void verifyApiStamp(String stamp) {
        if (stamp == null) {
            throw new BaseBusinessException(SignError.STAMP_NOT_FOUND);
        }
        String value = stampDao.getApiStamp(stamp);
        if (value != null) {
            throw new BaseBusinessException(SignError.REPLAY_ERROR);
        }
        int anHour = 60 * 60;
        stampDao.setApiStamp(stamp, anHour);
    }

    @Override
    public boolean setPortalStamp(String stamp) {
        Preconditions.checkNotNull(stamp);
        int halfHour = 30 * 60;
        return stampDao.setPortalStamp(stamp, halfHour);
    }

    @Override
    public void verifyPortalStamp(String stamp) {
        Preconditions.checkNotNull(stamp);

        Long result = stampDao.incPortalStampValue(stamp);

        //当加1后变成-99的话表示 校验成功
        if (result == -99) {
            return;
        }

        if (result > 0) {
            throw new BaseBusinessException(SignError.STAMP_NOT_FOUND);
        } else {
            throw new BaseBusinessException(SignError.REPLAY_ERROR);
        }

    }
}
