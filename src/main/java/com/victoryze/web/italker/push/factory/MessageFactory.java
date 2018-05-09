package com.victoryze.web.italker.push.factory;


import com.victoryze.web.italker.push.bean.api.message.MessageCreateModel;
import com.victoryze.web.italker.push.bean.db.Message;
import com.victoryze.web.italker.push.bean.db.User;
import com.victoryze.web.italker.push.utils.Hib;
import org.hibernate.Session;

/**
 * 消息数据存储类
 * Created by dsz on 2018/5/6.
 */
public class MessageFactory {

    /**
     * 根据id查找消息
     *
     * @param id
     * @return
     */
    public static Message findById(String id) {
        return Hib.query(new Hib.Query<Message>() {
            @Override
            public Message query(Session session) {
                return session.get(Message.class, id);
            }
        });
    }


    public static Message add(User sender, User receiver, MessageCreateModel model) {

        Message message = new Message(sender, receiver, model);
        return save(message);

    }

    private static Message save(Message message) {
        return Hib.query(new Hib.Query<Message>() {
            @Override
            public Message query(Session session) {
                session.save(message);
                session.flush();
                session.refresh(message);
                return message;
            }
        });
    }
}
