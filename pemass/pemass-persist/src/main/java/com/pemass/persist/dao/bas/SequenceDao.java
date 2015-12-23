/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.bas;

import com.pemass.persist.enumeration.SequenceEnum;

/**
 * @Description: SequenceDao
 * @Author: estn.zuo
 * @CreateTime: 2015-07-10 00:19
 */
public interface SequenceDao {

    /**
     * sequence递增, 默认步长为1
     *
     * @param field 字段名称
     * @return
     */
    Long increase(SequenceEnum field);

    /**
     * sequence递增
     *
     * @param field 字段名称
     * @param step  步长
     * @return
     */
    Long increase(SequenceEnum field, int step);
}
