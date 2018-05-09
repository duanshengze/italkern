package com.victoryze.web.italker.push.provider;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.Locale;

/**
 * Created by dsz on 2018/2/10.
 * LocalDateTime是一个Java8的新时间类型
 * 使用起来比常规的Date要好用
 * 但是Gson目前没有默认支持对LocalTime与Json字符串相互转换的问题
 */
public class LocalDateTimeConverter implements JsonSerializer<LocalDateTime>,JsonDeserializer<LocalDateTime> {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);



    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return FORMATTER.parse(json.getAsString(), new TemporalQuery<LocalDateTime>() {
            @Override
            public LocalDateTime queryFrom(TemporalAccessor temporal) {
                return null;
            }
        });
    }

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(FORMATTER.format(src));
    }
}
