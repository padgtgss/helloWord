package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.RecycleCategoryEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Description: 回收记录表
 * @Author: luoc
 * @CreateTime: 2015-07-10 16:42
 */
@Entity
@Table(name = "poi_recycle_record")
public class RecycleRecord extends BaseDomain{

    @Column(name = "recycle_category")
    private RecycleCategoryEnum recycleCategory; //回收类别

    @Column(name = "recycle_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recycleTime;  //回收时间

    @Column(name = "from_id")
    private Long fromId;//from那里

    @Column(name = "to_id")
    private Long toId;//to那里

    public RecycleCategoryEnum getRecycleCategory() {
        return recycleCategory;
    }

    public void setRecycleCategory(RecycleCategoryEnum recycleCategory) {
        this.recycleCategory = recycleCategory;
    }

    public Date getRecycleTime() {
        return recycleTime;
    }

    public void setRecycleTime(Date recycleTime) {
        this.recycleTime = recycleTime;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }
}
