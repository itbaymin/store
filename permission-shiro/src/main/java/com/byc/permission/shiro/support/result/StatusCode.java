package com.byc.permission.shiro.support.result;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

/**状态码**/
@Getter
@AllArgsConstructor
public enum StatusCode {
    CODE_SUCCESS(200, "操作成功"),
    PARAM_ERROR(1700100001, "参数错误"),
    REMEDY_ORDER_ERROR(1700100002, "补单失败");

    private Integer code;
    private String message;

    @Override
    public String toString() {
        return code.toString();
    }

    /**序列化器*/
    static class StatusCodeSerializer extends JsonSerializer<StatusCode> {
        @Override
        public void serialize(StatusCode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value.getCode());
        }
    }
}
