package com.pemass.persist.dao.sys;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.sys.Account;
import com.pemass.persist.domain.jpa.sys.User;

import java.util.List;
import java.util.Map;

/**
 * Created by estn.zuo on 14-9-18.
 */
public interface UserDao extends BaseDao {

    Account getAccountByUsername(String username);

    Account getEntityAccount(Account account, String accountname, String accountname1);

    /**
     * 根据一个坐标和范围，获取范围内的用户
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

    User getUserByUsername(String username);
}
