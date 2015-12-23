/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;


import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PointPurchaseDao;
import com.pemass.persist.dao.sys.UserDao;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.sys.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: UserServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-10-30 15:57
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private PointPurchaseDao pointPurchaseDao;

    @Resource
    private BaseDao jpaBaseDao;



    @Override
    public User retrieveByUUID(String uuid) {
        return userDao.getEntityByField(User.class, "uuid", uuid);
    }

    @Override
    public User getByUsername(String username) {
        return jpaBaseDao.getEntityByField(User.class, "username", username);
    }

    @Override
    public User getById(Long uid) {
        return jpaBaseDao.getEntityById(User.class, uid);
    }

    /**
     * 修改密码
     */
    @Override
    public User updatePassword(String username, String password, String newPassword) {

        User user = jpaBaseDao.getEntityByField(User.class, "username", username);

        if (MD5Util.verify(password, user.getSalt(), user.getPassword())) {
            String newpassword = MD5Util.encrypt(newPassword, user.getSalt());
            user.setPassword(newpassword);

        } else {
            throw new BaseBusinessException(SysError.PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    public User update(User user) {
        User storeUser = jpaBaseDao.getEntityById(User.class, user.getId());
        if (storeUser == null) {
            throw new BaseBusinessException(SysError.USER_NOT_EXIST);
        }
        /*-- 合并实体，排除一些不能在此更新的字段 --*/
        MergeUtil.merge(user, storeUser, new String[]{"password", "salt", "authority", "register_time"});
        userDao.merge(storeUser);
        return storeUser;
    }

    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    @Override
    public boolean hasBeing(String username) {
        User user = jpaBaseDao.getEntityByField(User.class, "username", username);
        return user != null;
    }


    @Override
    public DomainPage selectAllUser(String filedName, Object startDate, Object endDate, Map<String, Object> filedMap, long pageIndex, long pageSize) {
        return pointPurchaseDao.getEntitiesPagesByTime(User.class, filedName, startDate, endDate, filedMap, pageIndex, pageSize);
    }

    @Override
    public List<Object[]> checkUserByUserNames(String userNames) {
        List<String> userNameList = Splitter.on(SystemConst.SEPARATOR_SYMBOL)
                .omitEmptyStrings()
                .trimResults()
                .splitToList(userNames);

        List<Object[]> checkResultList = Lists.newArrayList();
        for (String userName : userNameList) {
            Object[] checkResult = new Object[3];

            List<User> users = userDao.getEntitiesByField(User.class, "username", userName);
            if (users.size() == 1) {
                checkResult[0] = true;
                checkResult[1] = users.get(0);
                checkResult[2] = userName;
            } else {
                checkResult[0] = false;
                checkResult[1] = null;
                checkResult[2] = userName;
            }

            checkResultList.add(checkResult);
        }

        return checkResultList;
    }

    @Override
    public List<User> getUserByRange(String coordinate, Integer distance, Map<String, Object> conditions) {
        return userDao.getUserByRange(coordinate, distance, conditions);
    }

    @Override
    public Long getRegisterUserAmount() {
        return userDao.getRegisterUserAmount();
    }

    @Override
    public Long getAddUsersAmountByDay() {
        return userDao.getAddUsersAmountByDay();
    }


}