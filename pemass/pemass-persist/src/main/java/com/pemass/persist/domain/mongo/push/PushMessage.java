/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.mongo.push;

import com.pemass.common.core.pojo.Body;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.enumeration.MessageStatusEnum;
import com.pemass.persist.serializer.ObjectIdSerializer;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: Message
 * @Author: estn.zuo
 * @CreateTime: 2014-12-17 21:26
 */
@Entity(value = "pns_push_message")
public class PushMessage implements Serializable {

    @Id
    @JsonSerialize(using = ObjectIdSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private ObjectId id;

    //消息标题
    private String title;

    //消息简介
    private String summary;

    //详情
    private List<Body> detail;

    //听众
    private String audience;

    //是否推送
    private MessageStatusEnum messageStatus;

    //发布时间
    private Date issueTime;

    //创建时间
    private Date createTime;

    //是否可用
    @Enumerated(EnumType.ORDINAL)
    private AvailableEnum available = AvailableEnum.AVAILABLE;

    //========================= getter and setter =======================\\

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Body> getDetail() {
        return detail;
    }

    public void setDetail(List<Body> detail) {
        this.detail = detail;
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

    public AvailableEnum getAvailable() {
        return available;
    }

    public void setAvailable(AvailableEnum available) {
        this.available = available;
    }
}
