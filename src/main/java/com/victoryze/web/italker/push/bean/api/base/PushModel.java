package com.victoryze.web.italker.push.bean.api.base;

import com.google.gson.annotations.Expose;
import com.victoryze.web.italker.push.utils.TextUtil;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个推送的具体内部维持一个数组，可以添加多个实体
 * 每次推送的详细数据是：把实体数组进行Json操作，然后发送Json字符串
 * 目的是为了：减少推送的次数，有多个消息需要合并之后推送
 * <p>
 * Created by dsz on 2018/4/20.
 */
public class PushModel {

    public static final int ENTITY_TYPE_MESSAGE = 200;


    private List<Entity> entities = new ArrayList<>();


    public PushModel add(Entity entity) {
        entities.add(entity);
        return this;
    }


    public PushModel add(int type, String content) {

        return add(new Entity(type, content));
    }


    /**
     * 拿到一个推送的字符串
     *
     * @return
     */
    public String getPushString() {

        if (entities.isEmpty()) {
            return null;
        }
        return TextUtil.toJson(entities);

    }

    /**
     * 具体的消息实体 在这个实体中封装了实体的内容和类型
     */
    public static class Entity {

        public Entity(int type, String content) {
            this.type = type;
            this.content = content;
        }

        /**
         * 实体的类型
         */
        @Expose
        public int type;
        /**
         * 实体的内容
         */
        @Expose
        public String content;

        public LocalDateTime createAt = LocalDateTime.now();

    }
}
