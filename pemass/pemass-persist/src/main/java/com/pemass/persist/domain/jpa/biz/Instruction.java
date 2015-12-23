package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.InstructionTypeEnum;

import javax.persistence.*;

/**
 * Description: 招募书表
 * Author: luoc
 * CreateTime: 2014-10-10 14:15
 */
@Entity
@Table(name = "biz_instruction")
public class Instruction extends BaseDomain {

    @Column(name = "organization_id",nullable = false)
    private Long organizationId;//所属商户

    @Column(name = "company_profile",length = 1000)
    private String companyProfile;//关于我们

    @Column(name = "company_advantage",length = 1000)
    private String companyAdvantage;//我们的优势

    @Column(name = "conditions",length = 1000)
    private String conditions;//分销条件

    @Column(name = "policy",length = 1000)
    private String policy;//政策支持

    @Column(name = "instruction_type")
    @Enumerated
    private InstructionTypeEnum instructionType;//类型

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        this.companyProfile = companyProfile;
    }

    public String getCompanyAdvantage() {
        return companyAdvantage;
    }

    public void setCompanyAdvantage(String companyAdvantage) {
        this.companyAdvantage = companyAdvantage;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public InstructionTypeEnum getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(InstructionTypeEnum instructionType) {
        this.instructionType = instructionType;
    }
}
