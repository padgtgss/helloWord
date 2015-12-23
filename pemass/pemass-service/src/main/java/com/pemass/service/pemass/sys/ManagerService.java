/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.sys.Manager;

import java.util.List;
import java.util.Map;

/**
 * @Description: ManagerService
 * @Author: estn.zuo
 * @CreateTime: 2014-10-15 18:32
 */
public interface ManagerService {

    /**
     * 插入一个管理员账号
     *
     * @param manager
     * @return
     */
    void insert(Manager manager);

    /**
     * 更新管理员账号
     *
     * @param manager
     * @return
     */
    Manager update(Manager manager);

    /**
     * 分页获取账号信息
     *
     * @param manager
     * @param page
     * @return
     */
    List<Manager> select(Manager manager, DomainPage page);

    /**
     * 根据管理员账号获取管理员信息
     *
     * @param managername 名称
     * @return
     */
    Manager selectByManagername(String managername);

    /**
     * 根据id获取管理员信息
     *
     * @param managerId
     * @return
     */
    Manager getManagerById(Long managerId);

    /**
     * 查询满足条件的管理员
     *
     * @param conditions
     * @param fuzzyConditions
     * @param collectionConditions
     * @param intervalConditions
     * @param orderByConditions
     * @param domainPage
     * @return
     */
    DomainPage getManagerByCondition(Map<String, Object> conditions, Map<String, Object> fuzzyConditions,
                                     Map<String, List<Object>> collectionConditions, Map<String, Object[]> intervalConditions,
                                     Map<String, BaseDao.OrderBy> orderByConditions, DomainPage domainPage);

    /**
     * 修改密码时，校验输入的旧密码是否正确
     *
     * @param manager
     * @return
     */
    boolean checkPassword(Manager manager);

    /**
     * 验证账户名是否重复
     *
     * @param managerName
     * @return
     */
    boolean checkManagerName(String managerName);

}
