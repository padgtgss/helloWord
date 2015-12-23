/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.bas;

import com.pemass.pojo.serializer.ResourceUrlSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @Description: MessagePojo
 * @Author: estn.zuo
 * @CreateTime: 2014-12-23 10:11
 */
public class ResourcesPojo {

    @JsonSerialize(using = ResourceUrlSerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
    private String url;//图片URL

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
