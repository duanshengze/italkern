package com.victoryze.web.italker.push.service;


import com.victoryze.web.italker.push.bean.api.base.ResponseModel;
import com.victoryze.web.italker.push.bean.api.message.MessageCreateModel;
import com.victoryze.web.italker.push.bean.card.MessageCard;
import com.victoryze.web.italker.push.bean.db.Message;
import com.victoryze.web.italker.push.bean.db.User;
import com.victoryze.web.italker.push.factory.MessageFactory;
import com.victoryze.web.italker.push.factory.PushFactory;
import com.victoryze.web.italker.push.factory.UserFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by dsz on 2018/5/6.
 */
@Path("/msg")
public class MessageService extends BaseService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<MessageCard> pushMessage(MessageCreateModel model) {
        if (!MessageCreateModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User self = getSelf();
        Message message = MessageFactory.findById(model.getId());
        if (message != null) {
            return ResponseModel.buildOK(new MessageCard(message));
        }

        if (model.getReceiverType() == Message.RECEIVER_TYPE_GROUP) {
            //发送群消息
            return null;
        } else {
            //发送个人消息
            return pushToUser(self, model);
        }
    }

    private ResponseModel<MessageCard> pushToUser(User sender, MessageCreateModel model) {
        User receiver = UserFactory.findById(model.getReceiverId());
        if (receiver == null) {
            System.out.println("pushToUser receiver is null");
        }

        if (sender.getId().equalsIgnoreCase(receiver.getId())) {
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }

        Message message = MessageFactory.add(sender, receiver, model);
        return buildAndPushResponse(sender, message);
    }

    private ResponseModel<MessageCard> buildAndPushResponse(User sender, Message message) {

        if (message == null) {
            //存储数据库失败
            return ResponseModel.buildCreateError(ResponseModel.ERROR_CREATE_MESSAGE);
        }
        PushFactory.pushNewMessage(sender, message);
        return ResponseModel.buildOK(new MessageCard(message));
    }


}
