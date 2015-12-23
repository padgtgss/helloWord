package com.pemass.pojo.serializer;

import com.pemass.common.core.constant.ConfigurationConst;
import com.pemass.common.core.constant.SystemConst;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.util.regex.Matcher;

public class ResourceUrlSerializer extends JsonSerializer<String> {



    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        Matcher matcher = SystemConst.URL_REG_PATTERN.matcher(value);
        if (matcher.matches()) {
            jgen.writeString(value);
        } else {
            value = StringUtils.startsWith(value, "/") ? value : "/" + value;
            jgen.writeString(ConfigurationConst.RESOURCE_ROOT_URL + value);
        }
    }
}
