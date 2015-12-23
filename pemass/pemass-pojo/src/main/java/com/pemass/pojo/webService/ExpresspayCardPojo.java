/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.webService;

/**
 * @Description: ExpresspayCardPojo
 * @Author: lin.shi
 * @CreateTime: 2015-09-15 11:11
 */
public class ExpresspayCardPojo {

    private ExpresspayCardHeadPojo expresspayCardHeadPojo;
    private ExpresspayCardBodyPojo expresspayCardBodyPojo;

    public ExpresspayCardPojo() {
    }

    public ExpresspayCardPojo(ExpresspayCardHeadPojo expresspayCardHeadPojo, ExpresspayCardBodyPojo expresspayCardBodyPojo) {
        this.expresspayCardHeadPojo = expresspayCardHeadPojo;
        this.expresspayCardBodyPojo = expresspayCardBodyPojo;
    }

    public ExpresspayCardHeadPojo getExpresspayCardHeadPojo() {
        return expresspayCardHeadPojo;
    }

    public void setExpresspayCardHeadPojo(ExpresspayCardHeadPojo expresspayCardHeadPojo) {
        this.expresspayCardHeadPojo = expresspayCardHeadPojo;
    }

    public ExpresspayCardBodyPojo getExpresspayCardBodyPojo() {
        return expresspayCardBodyPojo;
    }

    public void setExpresspayCardBodyPojo(ExpresspayCardBodyPojo expresspayCardBodyPojo) {
        this.expresspayCardBodyPojo = expresspayCardBodyPojo;
    }
}