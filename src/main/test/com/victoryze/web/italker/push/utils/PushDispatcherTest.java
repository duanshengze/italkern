package com.victoryze.web.italker.push.utils;

import com.victoryze.web.italker.push.bean.api.base.PushModel;
import com.victoryze.web.italker.push.bean.db.User;
import org.junit.Assert;
import org.junit.Test;

/**
 * PushDispatcher的测试类
 * Created by dsz on 2018/5/6.
 */
public class PushDispatcherTest {
    @Test
    public void test_sumbit(){
        String pushId="ea78dcdc295ce7dad1da960a2910cdb4";
        User rece=new User();
        rece.setPushId(pushId);
        PushModel pushModel=new PushModel();
        pushModel.add(PushModel.ENTITY_TYPE_MESSAGE,"test");
        PushDispatcher pushDispatcher=new PushDispatcher();
        pushDispatcher.add(rece,pushModel);
        boolean submit = pushDispatcher.submit();
        Assert.assertTrue(submit);
    }
}