package com.pemass.pojo.sys;

import com.pemass.common.core.pojo.Body;
import com.pemass.pojo.serializer.BodySerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(using = BodySerializer.class, include = JsonSerialize.Inclusion.NON_NULL)
public class BodyPojo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Body.BodyType type;

    private String value;

    private String title;

    public Body.BodyType getType() {
        return type;
    }

    public void setType(Body.BodyType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}