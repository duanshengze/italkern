package com.victoryze.web.italker.push.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * 用于设置Jersey的Json转换器
 * 用于替换JacksonJsonProvider
 * 将Http请求的数据转换为Model实体
 * 同时也实现了把返回的Model实体转换为Json字符串
 * 并输出到Http的返回体重
 * Created by dsz on 2018/2/10.
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GsonProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {

    //共用一个全局的Gson
    private static final Gson gson;

    static {

        //Gson序列化
        GsonBuilder builder = new GsonBuilder()
                //序列化为null的字段
                .serializeNulls()
                //仅仅处理带有@Expose注解的变量
                .excludeFieldsWithoutExposeAnnotation()
                //支持Map
                .enableComplexMapKeySerialization();
        //添加对Java8 localDateTime类型时间的支持
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter());
        gson = builder.create();
    }

    public static Gson getGson() {
        return gson;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return false;
    }

    /**
     * 将Json的字符串数据 转换为T类型的实例
     *
     * @param type
     * @param genericType
     * @param annotations
     * @param mediaType
     * @param httpHeaders
     * @param entityStream
     * @return
     * @throws IOException
     * @throws WebApplicationException
     */
    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {

        try (JsonReader reader = new JsonReader(new InputStreamReader(entityStream, "UTF-8"))) {
            return gson.fromJson(reader, genericType);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    /**
     * 将一个T类的实例输出到Http输出流中
     *
     * @param t
     * @param type
     * @param genericType
     * @param annotations
     * @param mediaType
     * @param httpHeaders
     * @param entityStream
     * @throws IOException
     * @throws WebApplicationException
     */
    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        try (JsonWriter jsonWriter = gson.newJsonWriter(new OutputStreamWriter(entityStream, Charset.forName("UTF-8")))) {
            gson.toJson(t, genericType, jsonWriter);
            jsonWriter.close();
        }
    }
}
