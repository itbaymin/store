package com.byc.common.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by baiyc
 * 2019/12/27/027 13:41
 * Description：对象转参数字符串
 */
@Slf4j
public class ObjectUtil {
    public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,true);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public static String buildParamStr(Object object){
        return buildParamStr(object,"&", Formatter.PATTERN1,true,false,true);
    }

    private static final ReflectionUtils.FieldFilter FIELD_FILTER = (field) -> {
        field.setAccessible(true);
        return !Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()) && !field.isAnnotationPresent(NotFormat.class);
    };

    @AllArgsConstructor
    public enum Formatter{
        PATTERN1("%s=%s","键值对"),
        PATTERN2("%s","只拼装键"),
        PATTERN3("%s","只拼装值");
        private String value;
        private String desc;
    }

    /**
     * Description: 处理对象转换特定格式字符串
     * params:[object 待处理对象, spliter 分隔符, formatter 格式,sort 是否需要排序, urlencode 是否url编码, ignoreNull 是否忽略空值]
     * return:java.lang.String
     */
    public static String buildParamStr(Object object ,String spliter,Formatter formatter,boolean sort ,boolean urlencode,boolean ignoreNull) {
        Map<String,String> treeMap = sort ? new TreeMap():new HashMap();
        ReflectionUtils.doWithFields(object.getClass(), (field) -> {
            String name = field.isAnnotationPresent(JsonProperty.class)?(field.getAnnotation(JsonProperty.class)).value():field.getName();
            Object value = field.get(object);
            if(!ignoreNull || value != null) {
                treeMap.put(name, StringUtils.defaultIfBlank(serialize(value), ""));
            }
        },FIELD_FILTER);
        StringBuilder uriBuilder = new StringBuilder();
        treeMap.entrySet().stream().forEach(e -> {
            if(uriBuilder.length() > 0) {
                uriBuilder.append(spliter);
            }
            try {
                String value = urlencode ? URLEncoder.encode(e.getValue(), "utf-8") : e.getValue();
                if(Formatter.PATTERN1==formatter)
                    uriBuilder.append(String.format(formatter.value, e.getKey(), value));
                else if(Formatter.PATTERN2==formatter)
                    uriBuilder.append(String.format(formatter.value, e.getKey()));
                else if(Formatter.PATTERN3==formatter)
                    uriBuilder.append(String.format(formatter.value, value));
            } catch (UnsupportedEncodingException e1) {
                log.error("urlencode编码{}异常",e.getValue(),e);
            }
        });
        return uriBuilder.toString();
    }

    public static String serialize(Object obj){
        String jsonStr = null;
        try {
            if(obj instanceof String)
                return (String)obj;
            StringWriter stringWriter = new StringWriter();
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
            objectMapper.writeValue(jsonGenerator, obj);
            jsonStr = stringWriter.toString();
        } catch (IOException e) {
            log.error("序列化:{}异常",obj,e);
        }
        return jsonStr;
    }

    public static <T> T deserialize(String jsonStr,Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            log.error("反序列化:{}异常",jsonStr,e);
        }
        return null;
    }

    public static <T> T convertValue(Object map,Class<T> tClass){
        return objectMapper.convertValue(map, tClass);
    }

}


