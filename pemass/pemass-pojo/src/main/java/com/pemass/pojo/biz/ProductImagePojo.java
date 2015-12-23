package com.pemass.pojo.biz;

import com.pemass.pojo.serializer.ResourceUrlSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @Description: ProductController
 * @Author: huang zhuo
 * @CreateTime: 2014-11-10 11:40
 */

public class ProductImagePojo {

    private String title;//标题

    private String content;//描述

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String url;//资源URL地址(相对地址)

   //======================== getter and setter ====================================


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
