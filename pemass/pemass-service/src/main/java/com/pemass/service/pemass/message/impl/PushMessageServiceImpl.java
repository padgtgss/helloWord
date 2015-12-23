/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.message.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mongodb.WriteResult;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.domain.mongo.push.PushMessage;
import com.pemass.persist.domain.mongo.push.UserPushMessage;
import com.pemass.persist.domain.vo.PushMessageVO;
import com.pemass.persist.enumeration.MessageStatusEnum;
import com.pemass.persist.enumeration.PushMessageTypeEnum;
import com.pemass.service.jms.Producer;
import com.pemass.service.pemass.message.PushMessageService;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: PushServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-12-17 15:16
 */
@Service
public class PushMessageServiceImpl implements PushMessageService {

    @Resource
    private Producer pushProducer;

    @Resource
    private Datastore morphiaDatastore;

    @Override
    public List<UserPushMessage> selectByUid(Long uid, int pageIndex, int pageSize) {
        Query<UserPushMessage> query = morphiaDatastore.createQuery(UserPushMessage.class);
        //需要将发给全部用户的数据也一并返回(-1表示全部)
        query.filter("uid in ", new Long[]{-1L, uid})
                .filter("messageStatus = ", MessageStatusEnum.HAS_PUSH)
                .order("-issueTime")
                .offset((pageIndex - 1) * pageSize).limit(pageSize);
        return query.asList();
    }

    @Override
    public DomainPage getPushMessageByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage) {
        Query<PushMessage> query = morphiaDatastore.createQuery(PushMessage.class);

        this.setCriteria(conditions, fuzzyConditions, domainPage, query);
        List<PushMessage> pushMessages = query.asList();
        Long totalCount = query.countAll();

        domainPage.setTotalCount(totalCount);
        domainPage.setDomains(pushMessages);
        return domainPage;
    }

    @Override
    public PushMessage getPushMessageById(String pushMessageId) {
        PushMessage pushMessage = morphiaDatastore.get(PushMessage.class, new ObjectId(pushMessageId));
        return pushMessage;
    }

    private void setCriteria(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage, Query<PushMessage> query) {
        /*-- 精确条件 --*/
        if (conditions != null) {
            Set<String> keySet = conditions.keySet();
            Iterator<String> keySetIterator = keySet.iterator();
            while (keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                query.filter(key, conditions.get(key));
            }
        }

        /*-- 模糊条件 --*/
        if (fuzzyConditions != null) {
            Set<String> keySet = fuzzyConditions.keySet();
            Iterator<String> keySetIterator = keySet.iterator();
            while (keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                query.field(key).contains(fuzzyConditions.get(key).toString());
            }
        }

        /*-- 分页条件 --*/
        Long offset = (domainPage.getPageIndex() - 1) * domainPage.getPageSize();
        Long pageSize = domainPage.getPageSize();
        query.offset(Integer.parseInt(offset.toString())).limit(Integer.parseInt(pageSize.toString()));

        /*-- 排序条件 --*/
        query.order("-createTime");
    }

    @Override
    public UserPushMessage selectByUserPushMessageId(String userPushMessageId) {
        UserPushMessage userPushMessage = morphiaDatastore.get(UserPushMessage.class, new ObjectId(userPushMessageId));

        /*-- 如果消息未读，则将消息置为已读 --*/
        if (!userPushMessage.getHasRead()) {
            userPushMessage.setHasRead(true);
            UpdateOperations<UserPushMessage> ops = morphiaDatastore.createUpdateOperations(UserPushMessage.class).set("hasRead", true);
            morphiaDatastore.update(userPushMessage, ops);
        }
        return userPushMessage;
    }

    @Override
    public Boolean insert(PushMessage pushMessage) {
        List param = Lists.newArrayList(pushMessage);
        PushMessageVO vo = new PushMessageVO(null, PushMessageTypeEnum.SYSTEM_PUSH, param);
        pushProducer.send(vo);
        return true;
    }

    @Override
    public void deleteById(String pushMessageId) {
        PushMessage targetPushMessage = morphiaDatastore.get(PushMessage.class, new ObjectId(pushMessageId));
        targetPushMessage.setAvailable(AvailableEnum.UNAVAILABLE);
        morphiaDatastore.merge(targetPushMessage);
    }

    @Override
    public boolean updateStatus(UserPushMessage userPushMessage) {
        UpdateOperations<UserPushMessage> ops = morphiaDatastore.createUpdateOperations(UserPushMessage.class).set("messageStatus", userPushMessage.getMessageStatus());
        morphiaDatastore.update(userPushMessage, ops);
        return true;
    }

    @Override
    public boolean deleteUserPushMessageById(String userPushMessageId) {
        morphiaDatastore.delete(UserPushMessage.class, new ObjectId(userPushMessageId));
        return true;
    }

    @Override
    public Boolean deleteAllByUid(Long uid) {
        Preconditions.checkNotNull(uid);
        WriteResult result = morphiaDatastore.delete(morphiaDatastore.createQuery(UserPushMessage.class).filter("uid", uid));
        return result.getN() != 0;
    }


}
