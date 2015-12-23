package com.pemass.persist.serializer;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

/**
 * 序列化ObjectId
 */
public class ObjectIdSerializer extends JsonSerializer<ObjectId> {

    @Override
    public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value != null) {
            jgen.writeString(value.toString());
        }
    }
}
