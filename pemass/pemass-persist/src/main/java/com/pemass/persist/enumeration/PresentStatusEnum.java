/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: 红包状态
 * @Author: estn.zuo
 * @CreateTime: 2014-11-25 11:31
 */
public enum PresentStatusEnum {
    /**
     * 未发放
     */
    NONE_ISSUE,
    /**
     * 红包广场
     */
    PRESENT_SQUARE,
    /**
     * 已发放未拆开
     */
    HAS_ISSUE,
    /**
     * 已发放已拆开
     */
    HAS_RECEIVE,
    /**
     * 已过期
     */
    OUT_OF_DATE,
}
