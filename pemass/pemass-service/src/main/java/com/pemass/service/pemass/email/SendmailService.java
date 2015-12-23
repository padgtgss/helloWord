package com.pemass.service.pemass.email;

/**
 * Created by POKL on 2014/10/14.
 */
public interface SendmailService {
    public  void send(String subject, String content, String receiveUser);
}
