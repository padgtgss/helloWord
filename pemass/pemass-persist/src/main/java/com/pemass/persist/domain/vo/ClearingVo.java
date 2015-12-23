/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.vo;

import com.pemass.persist.enumeration.ClearSourceEnum;

/**
 * @Description: ClearingVo
 * @Author: luoc
 * @CreateTime: 2015-08-14 14:45
 */
public class ClearingVo {

    private Long sourceTargetId; //清分来源Id

    private ClearSourceEnum clearSource;    //清分来源



    //=======================getter and setter =======================


    public Long getSourceTargetId() {
        return sourceTargetId;
    }

    public void setSourceTargetId(Long sourceTargetId) {
        this.sourceTargetId = sourceTargetId;
    }

    public ClearSourceEnum getClearSource() {
        return clearSource;
    }

    public void setClearSource(ClearSourceEnum clearSource) {
        this.clearSource = clearSource;
    }
}