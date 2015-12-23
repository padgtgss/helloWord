package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.common.server.enumeration.GenderEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Description: C端用户表
 * Author: estn.zuo
 * CreateTime: 2014-10-13 09:26
 */
@Entity
@Table(name = "sys_user")
public class User extends BaseDomain {
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 32)
    private String password;

    @Column(name = "salt", nullable = false, length = 8)
    private String salt;//盐值

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "gender", nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(name = "birthday")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date birthday;

    @Column(name = "avatar", length = 200)
    private String avatar;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "qr_code", nullable = false, length = 200)
    private String qrCode;  //二维码

    @Column(name = "bar_code", nullable = false, length = 200)
    private String barCode;  //条形码

    @Column(name = "register_time", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date registerTime; //注册时间

    @Column(name = "longitude", length = 10)
    private String longitude;   //最近一次登录点的经度

    @Column(name = "latitude", length = 10)
    private String latitude;    //最近一次登录点的纬度

    @Column(name = "authority", nullable = false, length = 100)
    private String authority;   //权限

    @Column(name="is_credited",nullable = false)
    private Integer isCredited; //是否授信

    //=============getter and setter ===================


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getIsCredited() {
        return isCredited;
    }

    public void setIsCredited(Integer isCredited) {
        this.isCredited = isCredited;
    }
}
