/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.sys;

import com.pemass.common.server.enumeration.GenderEnum;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.pojo.serializer.ResourceUrlSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @Description: UserPojo
 * @Author: estn.zuo
 * @CreateTime: 2014-11-16 16:11
 */
public class TerminalUserPojo {

    private Long id;

    private String terminalUsername;

    private String nickname;

    private GenderEnum gender;

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String avatar;

    private String email;

    private String authority;//权限

    private String organizationName;//组织名称

    private Long organizationId;    //机构ID

    private String linkPhone;   //联系电话

    private AuditStatusEnum onePayAuditStatus; //申请壹元购支付审核状态

    //================================getter and setter=============================================================

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getTerminalUsername() {
        return terminalUsername;
    }

    public void setTerminalUsername(String terminalUsername) {
        this.terminalUsername = terminalUsername;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public AuditStatusEnum getOnePayAuditStatus() {
        return onePayAuditStatus;
    }

    public void setOnePayAuditStatus(AuditStatusEnum onePayAuditStatus) {
        this.onePayAuditStatus = onePayAuditStatus;
    }
}
