/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.sys.User;

import java.util.List;
import java.util.Map;


/**
 * @Description: UserService
 * @Author: estn.zuo
 * @CreateTime: 2014-10-30 15:52
 */
public interface UserService {

    /**
     * 根据UUID检索用户信息
     *
     * @param uuid
     * @return
     */
    User retrieveByUUID(String uuid);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return
     */
    User getByUsername(String username);

    /**
     * 通过用户ID查询用户
     *
     * @param uid
     * @return
     */
    User getById(Long uid);

    /**
     * 修改密码
     *
     * @param username    用户名
     * @param password    旧密码
     * @param newPassword 新密码
     * @return
     */
    User updatePassword(String username, String password, String newPassword);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    User update(User user);

    /**
     * 判断用户是否已注册
     *
     * @param username
     * @return
     */
    boolean hasBeing(String username);

    /**
     * 查询所有的用户
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<User> selectAllUser(String filedName, Object startDate, Object endDate, Map<String, Object> filedMap, long pageIndex, long pageSize);

    /**
     * 校验userNames字符串，返回一个包含Object[]的数组集合（
     * 其中Object[]长度为3,object[0]为校验结果，object[1]为user对象，当user对象存在时才有user对象
     * 否则object[1]为null，Object[2]为原校验账号）
     *
     * @param userNames
     * @return
     */
    List<Object[]> checkUserByUserNames(String userNames);

    /**
     * 根据坐标和范围，获取范围内的所有用户
     *
     * @param coordinate
     * @param distance
     * @return
     */
    List<User> getUserByRange(String coordinate, Integer distance,Map<String,Object> conditions);

    /**
     * 统计所有注册用户的数量
     * @return
     */
    Long getRegisterUserAmount();

    /**
     * 统计当日新增用户的数量
     * @return
     */
    Long getAddUsersAmountByDay();


}