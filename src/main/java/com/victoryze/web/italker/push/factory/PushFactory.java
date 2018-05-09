package com.victoryze.web.italker.push.factory;


import com.google.common.base.Strings;

import com.victoryze.web.italker.push.bean.api.base.PushModel;
import com.victoryze.web.italker.push.bean.card.MessageCard;
import com.victoryze.web.italker.push.bean.card.UserCard;
import com.victoryze.web.italker.push.bean.db.Message;
import com.victoryze.web.italker.push.bean.db.PushHistory;
import com.victoryze.web.italker.push.bean.db.User;
import com.victoryze.web.italker.push.utils.Hib;
import com.victoryze.web.italker.push.utils.PushDispatcher;
import com.victoryze.web.italker.push.utils.TextUtil;
import org.hibernate.Session;

/**
 * 消息存储于处理的工具
 * Created by dsz on 2018/2/25.
 */
public class PushFactory {


    /**
     * 发送消息 并在当前的发送记录中存储这条消息
     * 1.判断是发送给群还是发送给个人
     * 2.若是群 则进行群消息的发送 并将消息记录存储起来
     * 3.若是个人 则进行个人消息的发送
     *
     * @param sender
     * @param message
     */
    public static void pushNewMessage(User sender, Message message) {

        if (sender == null || message == null) {
            return;
        }
        //1.将消息转换成消息卡片
        MessageCard card = new MessageCard(message);
        //2.将消息卡片对象转换成 json字符串进行发送
        String entity = TextUtil.toJson(card);
        //3.进行消息的发送
        PushDispatcher dispatcher = new PushDispatcher();
        //判断是发给群还是个人
        if (message.getGroup() == null && Strings.isNullOrEmpty(message.getGroupId())) {
            //发送给个人 查找到接受者
            User receiver = UserFactory.findById(message.getReceiverId());
            if (receiver == null || Strings.isNullOrEmpty(receiver.getPushId())) {
                System.out.println("pushfactory receiver is null or pushId is null");
                return;
            }
            System.out.println("pushfactory receiver pushId " + receiver.getPushId());
            PushHistory history = new PushHistory();
            history.setEntityType(PushModel.ENTITY_TYPE_MESSAGE);
            history.setEntity(entity);
            history.setSender(sender);
            history.setReceiver(receiver);
            history.setReceiverPushId(receiver.getPushId());

            PushModel pushModel = new PushModel();
            pushModel.add(history.getEntityType(), history.getEntity());
            dispatcher.add(receiver, pushModel);

            Hib.queryOnly(new Hib.QueryOnly() {
                @Override
                public void query(Session session) {
                    session.save(history);
                }
            });

        } else {
            // TODO 给群发消息


        }


        dispatcher.submit();


    }

    /**
     * 推送账号退出消息
     *
     * @param receiver
     * @param pushId
     */
    public static void pushLogout(User receiver, String pushId) {


    }

    /**
     * 给一个朋友推送我的信息过去
     * 类型是我关注了他
     *
     * @param followUser 接受者
     * @param userCard   我的卡片信息
     */
    public static void pushFollow(User followUser, UserCard userCard) {
    }
}
