package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.common.server.enumeration.GenderEnum;
import com.pemass.persist.enumeration.AccountStatusEnum;
import com.pemass.persist.enumeration.PositionRoleEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Description: Account
 * Author: z
 * CreateTime: 2014-10-11 10:03
 */
@Entity
@Table(name = "sys_account")
public class Account extends BaseDomain {

    @Column(name = "organization_id")
    private Long organizationId;   //所属机构

    @Column(name = "accountname", length = 50, unique = true)
    private String accountname;   //账户名称,即登录名 ,是唯一的

    @Column(name = "password", length = 32, nullable = false)
    private String password;     //密码

    @Column(name = "salt", length = 8, nullable = false)
    private String salt;       //盐值

    @Column(name = "nickname", length = 50)
    private String nickname;   //昵称

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderEnum gender; //联系人性别

    @Column(name = "telephone", length = 20)
    private String telephone;   //电话

    @Column(name = "avatar", length = 200)
    private String avatar;  //头像(图片的URL相对地址)

    @Column(name = "registration_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationTime;   //注册时间

    @Column(name = "last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;   //最后登录时间

    @Column(name = "reason", length = 500)
    private String reason;   //注册失败原因

    @Column(name = "account_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AccountStatusEnum accountStatus;  //账号状态

    @Column(name = "position_role",length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private PositionRoleEnum positionRole; //职务角色

    @Column(name = "authority", length = 100)
    private String authority;    //角色对应权限

    //=================== getter and setter =========


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AccountStatusEnum getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatusEnum accountStatus) {
        this.accountStatus = accountStatus;
    }

    public PositionRoleEnum getPositionRole() {
        return positionRole;
    }

    public void setPositionRole(PositionRoleEnum positionRole) {
        this.positionRole = positionRole;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
