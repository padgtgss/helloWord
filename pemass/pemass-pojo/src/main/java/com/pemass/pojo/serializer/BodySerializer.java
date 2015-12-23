package com.pemass.pojo.serializer;

import com.pemass.common.core.constant.ConfigurationConst;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.Body;
import com.pemass.pojo.sys.BodyPojo;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;


public class BodySerializer extends JsonSerializer<BodyPojo> {

    @Override
    public void serialize(BodyPojo body, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        if (StringUtils.isNotBlank(body.getValue())) {

            String bodyValue = body.getValue();

            //类型为IMG并且不是绝对路径,则进行URL地址处理
            if (Body.BodyType.IMAGE ==  body.getType()
                    && !bodyValue.startsWith(SystemConst.HTTP_START)) {
                //当body的value以SystemConst.WEB_START开头时，表示为插入数据
                    String url = StringUtils.startsWith(bodyValue, "/") ? bodyValue : "/" +bodyValue;
                    body.setValue(ConfigurationConst.RESOURCE_ROOT_URL + url.trim());
            }
        } else {
            body.setValue(null);
        }

        if (body.getType() != null) {
            jgen.writeStringField("type", body.getType().name());
        }

        if (StringUtils.isNotBlank(body.getValue())) {
            jgen.writeStringField("value", body.getValue());
        }

        if (StringUtils.isNotBlank(body.getTitle())) {
            jgen.writeStringField("title", body.getTitle());
        }

        jgen.writeEndObject();
    }
}
