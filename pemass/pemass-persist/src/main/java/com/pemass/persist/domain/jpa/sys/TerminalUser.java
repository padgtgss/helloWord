package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.common.server.enumeration.GenderEnum;
import com.pemass.persist.enumeration.AccountStatusEnum;

import javax.persistence.*;

/**
 * Description: 收银员、检票员用户表
 * Author: luoc
 * CreateTime: 2014-10-20 16:26
 */
@Entity
@Table(name = "sys_terminal_user")
public class TerminalUser extends BaseDomain {

    @Column(name = "terminal_username", length = 50, unique = true)
    private String terminalUsername;   //账户名称,即登录名 ,是唯一的

    @Column(name = "password", length = 32, nullable = false)
    private String password;     //密码

    @Column(name = "salt", length = 8, nullable = false)
    private String salt;       //盐值

    @Column(name = "nickname", length = 50)
    private String nickname;   //昵称

    @Column(name = "gender", length = 6, nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;   //性别

    @Column(name = "avatar", length = 200)
    private String avatar;

    @Column(name = "site_id")
    private Long siteId;  //所属营业点

    @Column(name = "organization_id")
    private Long organizationId;  //所属商户

    @Column(name = "link_phone", length = 20, nullable = false)
    private String linkPhone;   //联系电话

    @Column(name = "email", length = 50)
    private String email;     //邮箱

    @Column(name = "authority", length = 100, nullable = false)
    private String authority;    //角色对应权限 ROLE_CASHIER-收银员  ROLE_TICKETER-检票员

    @Column(name = "account_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AccountStatusEnum accountStatus;  //账号状态

    //==============getter and setter ===============

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTerminalUsername() {
        return terminalUsername;
    }

    public void setTerminalUsername(String terminalUsername) {
        this.terminalUsername = terminalUsername;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public AccountStatusEnum getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatusEnum accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
