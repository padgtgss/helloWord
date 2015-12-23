/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.message;


import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.mongo.push.PushMessage;
import com.pemass.persist.domain.mongo.push.UserPushMessage;

import java.util.List;
import java.util.Map;

/**
 * @Description: PushService
 * @Author: estn.zuo
 * @CreateTime: 2014-12-17 15:13
 */
public interface PushMessageService {

    /**
     * 根据用户ID，分页获取推送消息
     * <p/>
     *
     * @param uid       用户ID
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<UserPushMessage> selectByUid(Long uid, int pageIndex, int pageSize);

    /**
     * 分页获取满足条件的消息
     *
     * @param conditions
     * @param fuzzyConditions
     * @param domainPage
     * @return
     */
    DomainPage getPushMessageByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage);

    /**
     * 获取某条推送消息
     *
     * @param pushMessageId
     * @return
     */
    PushMessage getPushMessageById(String pushMessageId);

    /**
     * 根据用户消息ID来获取消息
     * <p/>
     * 如果该条消息未读则置该消息为已读
     *
     * @param userPushMessageId 用户消息ID
     * @return
     */
    UserPushMessage selectByUserPushMessageId(String userPushMessageId);

    /**
     * 插入一条MESSAGE信息
     *
     * @param message
     * @return
     */
    Boolean insert(PushMessage message);

    /**
     * 删除某条推送消息
     *
     * @return
     */
    void deleteById(String pushMessageId);

    /**
     * 更新消息的状态
     *
     * @param userPushMessage
     * @return
     */
    boolean updateStatus(UserPushMessage userPushMessage);

    /**
     * 删除用户消息
     *
     * @param userPushMessageId 用户消息ID
     * @return
     */
    boolean deleteUserPushMessageById(String userPushMessageId);

    /**
     * 根据用户ID批量删除改用户的推送消息
     *
     * @param uid 用户ID
     * @return
     */
    Boolean deleteAllByUid(Long uid);

}
