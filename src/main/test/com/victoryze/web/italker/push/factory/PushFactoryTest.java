package com.victoryze.web.italker.push.factory;


import com.victoryze.web.italker.push.bean.db.Message;
import com.victoryze.web.italker.push.bean.db.User;
import org.junit.Test;

/**
 * Created by dsz on 2018/5/7.
 */
public class PushFactoryTest {
    @Test
    public void pushNewMessage() throws Exception {

        User sender = new User();
        Message message=new Message();
        message.setReceiverId("95efae4d-8011-44eb-844b-14df189e3db7");
        message.setContent("PushFactoryTest");
        PushFactory.pushNewMessage(sender,message);

    }

}