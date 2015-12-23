/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.email.impl;

import com.pemass.common.core.constant.ConfigurationConst;
import com.pemass.common.core.util.AESUtil;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.pemass.email.SendmailService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
/**
 * @Description: SendmailServiceImpl
 * @Author: huang zhuo
 * @CreateTime: 2014-10-14 16:10
 */
@Service
public class SendmailServiceImpl implements SendmailService {

    private Log logger = LogFactory.getLog(SendmailServiceImpl.class);
    @Override
    public void send(String subject, String content, String receiveUser) {
        if (StringUtils.isBlank(receiveUser)) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("email send start");
        }


        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true"); // 是否进行验证
        Session session = Session.getInstance(properties);
        MimeMessage message = new MimeMessage(session);
        Transport transport = null;
        try {
            // 发件人
            InternetAddress from = new InternetAddress(ConfigurationConst.MAIL_USERNAME);
            message.setFrom(from);

            // 收件人
            InternetAddress to = new InternetAddress(receiveUser);
            message.setRecipient(Message.RecipientType.TO, to);

            // 邮件主题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(content, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();

            transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(ConfigurationConst.MAIL_HOST,ConfigurationConst.MAIL_USERNAME, AESUtil.decrypt(ConfigurationConst.MAIL_PASSWORD, PemassConst.AES_CIPHER));
            // 发送
            transport.sendMessage(message, message.getAllRecipients());
            if (logger.isDebugEnabled()) {
                logger.debug("email send success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (javax.mail.MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

