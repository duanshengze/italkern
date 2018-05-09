package com.victoryze.web.italker.push.service;

import com.victoryze.web.italker.push.bean.api.account.AccountRspModel;
import com.victoryze.web.italker.push.bean.api.account.RegisterModel;
import com.victoryze.web.italker.push.bean.api.base.ResponseModel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dsz on 2018/5/9.
 */
public class AccountServiceTest {
    @Test
    public void test_register() throws Exception {
        AccountService accountService = new AccountService();
        RegisterModel register = new RegisterModel();
        register.setAccount("1222e2");
        register.setName("dsz");
        register.setPassword("dsdsda");
        ResponseModel<AccountRspModel> resopnse = accountService.register(register);
        System.out.println(resopnse.getCode());
    }

}