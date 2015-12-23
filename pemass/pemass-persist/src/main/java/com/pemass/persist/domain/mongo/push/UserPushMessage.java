/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.mongo.push;

import com.pemass.persist.enumeration.MessageStatusEnum;
import com.pemass.persist.serializer.ObjectIdSerializer;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 消息推送消息
 * @Author: estn.zuo
 * @CreateTime: 2014-12-17 14:58
 */
@Entity("pns_user_push_message")
public class UserPushMessage implements Serializable {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private ObjectId id;

    //用户ID
    private Long uid;

    @Reference
    private PushMessage pushMessage;

    //听众
    private String audience;

    //是否推送
    private MessageStatusEnum messageStatus;

    //是否查看
    private Boolean hasRead;

    //发布时间
    private Date issueTime;

    //创建时间
    private Date createTime;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public PushMessage getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(PushMessage pushMessage) {
        this.pushMessage = pushMessage;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public MessageStatusEnum getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatusEnum messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Boolean getHasRead() {
        return hasRead;
    }

    public void setHasRead(Boolean hasRead) {
        this.hasRead = hasRead;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
