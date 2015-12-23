/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: VersionUpdateTypeEnum
 * @Author: estn.zuo
 * @CreateTime: 2014-12-02 17:49
 */
public enum VersionUpdateTypeEnum {
    /**
     * 无需更新
     */
    NONE_UPDATE,

    /**
     * 强制更新
     */
    ENFORCE_UPDATE,

    /**
     * 可选更新(主要用于返回客户端时使用)
     */
    OPTIONAL_UPDATE,
}
