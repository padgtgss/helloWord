/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.sys;

import com.pemass.common.server.enumeration.GenderEnum;
import com.pemass.pojo.serializer.ResourceUrlSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * @Description: UserPojo
 * @Author: estn.zuo
 * @CreateTime: 2014-11-24 20:20
 */
public class UserPojo {

    private Long id;

    private String username;    //用户名

    private String nickname;    //昵称

    private Date birthday;  //生日

    private GenderEnum gender;  //性别

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String avatar;  //头像

    private String email;   //邮箱

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String qrCode;  //二维码

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String barCode;  //条形码

    private Integer isCredited; //是否授信【0-未授信、1-授信】

    private Date registerTime; //注册时间

    //=================================getter and setter=========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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
