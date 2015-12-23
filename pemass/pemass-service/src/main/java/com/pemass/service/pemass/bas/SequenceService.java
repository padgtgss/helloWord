/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.bas;

import com.pemass.persist.enumeration.SequenceEnum;

/**
 * @Description: SequenceService
 * @Author: estn.zuo
 * @CreateTime: 2015-07-10 00:52
 */
public interface SequenceService {
    /**
     * 获取对应字段的编号
     *
     * @param field 字段名称
     * @return
     */
    String obtainSequence(SequenceEnum field);
}
