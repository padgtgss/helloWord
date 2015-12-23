/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.vo;

import java.io.Serializable;

/**
 * @Description: 清分商户信息
 * @Author: oliver.he
 * @CreateTime: 2015-09-12 14:55
 */
public class OrgAndCompanyVO<T extends OrgAndCompanyVO> implements Serializable, Cloneable, Comparable<T> {

    private static final long serialVersionUID = 1633246146009895104L;

    private OrgAndCompanyVO() {
        super();
    }

    private OrgAndCompanyVO(OrgAndCompanyVO orgAndCompanyVO) {
        this.orgIDAndType = orgAndCompanyVO.orgIDAndType;
        this.organizationName = orgAndCompanyVO.organizationName;
        this.shortOrganizationName = orgAndCompanyVO.shortOrganizationName;
    }

    public enum GroupType {

        COMPANY("公司"),
        ORGANIZATION("商户"),
        JFT("积分通");

        private String description;

        GroupType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private String orgIDAndType;    // 商户的ID(也可以是审核后台Company ID):商户类型(Organization或者Company)

    private String organizationName;    // 商户名 (也可以是审核后台的Company name)

    private String shortOrganizationName; // 五位长度的商户名，其后添加来商户的类型

    public static OrgAndCompanyVO getInstance() {
        return new OrgAndCompanyVO();
    }

    public static OrgAndCompanyVO getInstance(OrgAndCompanyVO orgAndCompanyVO) {
        return new OrgAndCompanyVO(orgAndCompanyVO);
    }

    // ================= getter and setter ================ \\


    public String getOrgIDAndType() {
        return orgIDAndType;
    }

    public void setOrgIDAndType(String orgIDAndType) {
        this.orgIDAndType = orgIDAndType;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getShortOrganizationName() {
        return shortOrganizationName;
    }

    public void setShortOrganizationName(String shortOrganizationName) {
        this.shortOrganizationName = shortOrganizationName;
    }

    public OrgAndCompanyVO clone() {
        OrgAndCompanyVO orgAndCompanyVO = null;
        try {
            orgAndCompanyVO = (OrgAndCompanyVO) super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("clone happened exception." + this);
        }
        return orgAndCompanyVO;
    }

    public int compareTo(T o) {
        String thisCompareStr = this.organizationName + this.orgIDAndType;
        String otherCompareStr = o.getOrganizationName() + o.getOrgIDAndType();
        return thisCompareStr.compareTo(otherCompareStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrgAndCompanyVO<?> that = (OrgAndCompanyVO<?>) o;

        if (orgIDAndType != null ? !orgIDAndType.equals(that.orgIDAndType) : that.orgIDAndType != null) return false;
        if (organizationName != null ? !organizationName.equals(that.organizationName) : that.organizationName != null)
            return false;
        return !(shortOrganizationName != null ? !shortOrganizationName.equals(that.shortOrganizationName) : that.shortOrganizationName != null);

    }

    @Override
    public int hashCode() {
        int result = orgIDAndType != null ? orgIDAndType.hashCode() : 0;
        result = 31 * result + (organizationName != null ? organizationName.hashCode() : 0);
        result = 31 * result + (shortOrganizationName != null ? shortOrganizationName.hashCode() : 0);
        return result;
    }
}