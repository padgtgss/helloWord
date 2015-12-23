package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.*;


/**
 * @Description 商品类型
 * @Author luoc
 */
@Entity
@Table(name = "biz_product_category")
public class ProductCategory extends BaseDomain {

    @Column(name = "parent_category_id")
    private Long parentCategoryId;//商品上级类别id

    @Column(name = "category_name", length = 50)
    private String categoryName;//商品类别

    @Column(name = "category_code")
    private String categoryCode;//商品类别编码

    @Column(name = "state")
    private Integer state;//状态

    @Column(name = "is_create_ticket", nullable = false)
    private Integer isCreateTicket;//是否生产票码【0实体商品不需要 1虚拟商品需要】

    @Column(name = "is_distribution", nullable = false)
    private Integer isDistribution;//是否分销【0-不允许分销、1-允许分销】

    //============================ getter and setter ==========================\\

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getIsCreateTicket() {
        return isCreateTicket;
    }

    public void setIsCreateTicket(Integer isCreateTicket) {
        this.isCreateTicket = isCreateTicket;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Integer getIsDistribution() {
        return isDistribution;
    }

    public void setIsDistribution(Integer isDistribution) {
        this.isDistribution = isDistribution;
    }
}
