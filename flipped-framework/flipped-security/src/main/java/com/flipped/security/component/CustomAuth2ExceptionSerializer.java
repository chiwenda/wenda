package com.flipped.security.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.flipped.common.core.contants.CommonConstants;
import com.flipped.security.exception.CustomAuth2Exception;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * OAuth2 异常格式化
 *
 * @author chiwenda
 * @date 2022/2/8 下午8:47
 */
public class CustomAuth2ExceptionSerializer extends StdSerializer<CustomAuth2Exception> {

    protected CustomAuth2ExceptionSerializer() {
        super(CustomAuth2Exception.class);
    }

    @SneakyThrows
    @Override
    public void serialize(CustomAuth2Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("code", CommonConstants.FAIL);
        gen.writeStringField("msg", value.getMessage());
        gen.writeStringField("data", value.getErrorCode());
        // 资源服务器会读取这个字段
        gen.writeStringField("error", value.getMessage());
        gen.writeEndObject();
    }
}
