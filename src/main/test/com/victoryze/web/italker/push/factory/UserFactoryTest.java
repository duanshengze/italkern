package com.victoryze.web.italker.push.factory;


import com.victoryze.web.italker.push.bean.db.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by dsz on 2018/2/24.
 */
public class UserFactoryTest {
    @Test
    public void test_findByToken() throws Exception {
    }

    @Test
    public void test_findByPhone() throws Exception {
        User byName = UserFactory.findByPhone("1222e2");
        System.out.println(byName.getCreateAt());
    }

    @Test
    public void test_findByName() throws Exception {
        List<User> search = UserFactory.search("w");
        boolean empty = search.isEmpty();
        Assert.assertFalse(empty);
    }

    @Test
    public void test_register() throws Exception {

//        User update = UserFactory.findByToken("NzBiMzhlNzktOTBlYS00OGZlLTk4MWQtMmVmZmExMjIyYWVh");
//        update.setDescription("test");
//        update.setName("dddd");
//        User updated = UserFactory.update(update);
//        Assert.assertNotNull(update);
        UserFactory.register("18881","11222","w");
    }

}