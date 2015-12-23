/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: EmailVo
 * @Author: zhou hang
 * @CreateTime: 2015-01-28 11:24
 */
public class EmailVo {

    private String subject;   //主题

    private String to; //收件人

    private String template;//模板名称

    private  Map<String ,String> paraMap=new HashMap<String,String>();//参数名称和值：[key,value]


    private List<String> attachmentPaths=new ArrayList<String>(); //附件路径

   //======================getter setter==================


    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, String> getParaMap() {
        return paraMap;
    }

    public void setParaMap(Map<String, String> paraMap) {
        this.paraMap = paraMap;
    }

}