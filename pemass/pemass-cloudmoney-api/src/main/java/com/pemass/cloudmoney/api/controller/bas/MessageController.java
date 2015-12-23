/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.bas;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.pojo.Body;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.mongo.push.UserPushMessage;
import com.pemass.pojo.bas.MessagePojo;
import com.pemass.pojo.sys.BodyPojo;
import com.pemass.service.pemass.message.PushMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 消息推送
 * @Author: estn.zuo
 * @CreateTime: 2014-12-23 09:55
 */
@Controller
@RequestMapping("/message")
public class MessageController {

    @Resource
    private PushMessageService pushMessageService;

    /**
     * 分页获取用户的消息
     *
     * @param uid       用户ID,当<code>uid==null</code>时，表示获取匿名用户的推送列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object list(Long uid, int pageIndex, int pageSize) {

        List<UserPushMessage> userPushMessageList = pushMessageService.selectByUid(uid, pageIndex, pageSize);

        /*-- 封装返回数据 --*/
        List<MessagePojo> messagePojoList = new ArrayList<MessagePojo>();
        for (UserPushMessage userPushMessage : userPushMessageList) {
            MessagePojo messagePojo = new MessagePojo();
            messagePojo.setId(userPushMessage.getId().toString());
            messagePojo.setHasRead(userPushMessage.getHasRead());
            messagePojo.setIssueTime(userPushMessage.getIssueTime());
            messagePojo.setSummary(userPushMessage.getPushMessage().getSummary());
            messagePojo.setTitle(userPushMessage.getPushMessage().getTitle());
            messagePojoList.add(messagePojo);
        }
        return messagePojoList;
    }

    /**
     * 获取某条消息的详情
     *
     * @param userPushMessageId
     * @return
     */
    @RequestMapping(value = "/{userPushMessageId}", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@PathVariable("userPushMessageId") String userPushMessageId) {

        UserPushMessage userPushMessage = pushMessageService.selectByUserPushMessageId(userPushMessageId);

        /*-- 封装返回数据格式 --*/
        MessagePojo messagePojo = new MessagePojo();
        messagePojo.setId(userPushMessage.getId().toString());
        messagePojo.setHasRead(userPushMessage.getHasRead());
        messagePojo.setIssueTime(userPushMessage.getIssueTime());
        messagePojo.setSummary(userPushMessage.getPushMessage().getSummary());
        messagePojo.setTitle(userPushMessage.getPushMessage().getTitle());
        List<BodyPojo> bodyPojoList = new ArrayList<BodyPojo>();
        for (Body body : userPushMessage.getPushMessage().getDetail()) {
            BodyPojo bodyPojo = new BodyPojo();
            MergeUtil.merge(body, bodyPojo);
            bodyPojoList.add(bodyPojo);
        }
        messagePojo.setDetail(bodyPojoList);

        return messagePojo;
    }

    @RequestMapping(value = "/{userPushMessageId}", method = {RequestMethod.POST, RequestMethod.DELETE})
    @ResponseBody
    public Object delete(@PathVariable("userPushMessageId") String userPushMessageId) {
        return pushMessageService.deleteUserPushMessageById(userPushMessageId);
    }

    /**
     * 根据用户ID批量删除改用户的推送消息
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.DELETE})
    @ResponseBody
    public Object deleteAllByUserId(Long uid) {
        return ImmutableMap.of("result", pushMessageService.deleteAllByUid(uid));
    }

}
