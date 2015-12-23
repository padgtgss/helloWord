/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.bas.impl;

import com.google.common.base.Preconditions;
import com.pemass.persist.dao.bas.SequenceDao;
import com.pemass.persist.enumeration.SequenceEnum;
import com.pemass.service.pemass.bas.SequenceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: SequenceServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2015-07-10 00:56
 */
@Service
public class SequenceServiceImpl implements SequenceService {

    @Resource
    private SequenceDao sequenceDao;

    @Override
    public String obtainSequence(SequenceEnum field) {
        Preconditions.checkNotNull(field);
        Long currentSequence = sequenceDao.increase(field);
        return this.doGetRealSequence(field, currentSequence);
    }

    /**
     * 获得拼接后的真实Sequence字符串
     *
     * @param field           sequence字段
     * @param currentSequence 当前序号值
     * @return
     */
    private String doGetRealSequence(SequenceEnum field, Long currentSequence) {
        Preconditions.checkNotNull(currentSequence);
        String strSequence = String.format("%08d", currentSequence);
        return field.getPrefix() + strSequence;
    }
}
