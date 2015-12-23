/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.sys.ServiceControll;
import com.pemass.persist.enumeration.AppTypeEnum;

import java.util.List;
import java.util.ListIterator;

/**
 * @Description: UserService
 * @Author: estn.zuo
 * @CreateTime: 2014-10-30 15:52
 */
public interface ServiceControllService {

    /**
     * 插入
     *
     * @param serviceControll
     * @return
     */
    ServiceControll insert(ServiceControll serviceControll);


    /**
     * 更新
     *
     * @param serviceControll
     * @return
     */
    ServiceControll update(ServiceControll serviceControll);

    /**
     * 删除
     *
     * @param serviceControll
     * @return
     */
    ServiceControll delete(ServiceControll serviceControll);


    /**
     * 分页获取
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectAll(Long pageIndex,Long pageSize);

    List<ServiceControll> selectAll();

    /**
     * 根据id获取接口信息
     * @param id
     * @return
     */
    ServiceControll getById(Long id);


    /**
     * 赋权
     *
     * @param appid
     * @param serviceControllList
     */
    Boolean grant(String appid, List<String> urls);

    /**
     * 判断该APPID是否拥有调用此接口地址的权利
     *
     * @param appid
     * @param url
     * @return
     */
    Boolean hasAuth(String appid,String url);


    /**
     * 根据接口名称分页查询接口信息
     * @param servicename
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getByServiceName(String servicename, long pageIndex, long pageSize);

    /**
     * 获取某个appId 的接口权限
     * @param appId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getByAppId(String appId,Long pageIndex,Long pageSize);


    /**
     * 根据接口名称，或是客户端类型查询接口
     * @param servicename
     * @param appType
     * @return
     */
    List<ServiceControll> selectServiceControlls(String servicename, AppTypeEnum appType);


    /**
     * 根据类型分页获取接口
     * @param appType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage<ServiceControll> selectServiceControlls(AppTypeEnum appType,long pageIndex,long pageSize);

    /**
     * 取消授权
     * @param appid
     * @param id
     */
    void unGrant(String appid, String url);


}