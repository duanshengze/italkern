package com.victoryze.web.italker.push.service;


import com.google.common.base.Strings;
import com.victoryze.web.italker.push.bean.api.account.AccountRspModel;
import com.victoryze.web.italker.push.bean.api.account.LoginModel;
import com.victoryze.web.italker.push.bean.api.account.RegisterModel;
import com.victoryze.web.italker.push.bean.api.base.ResponseModel;
import com.victoryze.web.italker.push.bean.db.User;
import com.victoryze.web.italker.push.factory.UserFactory;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


/**
 * Created by dsz on 2018/1/14.
 */
//127.0.0.1:8080/api/account
@Path("/account")
public class AccountService extends BaseService {


    @POST
    @Path("/login")
    public ResponseModel<AccountRspModel> login(LoginModel model) {
        if (!LoginModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.login(model.getAccount(), model.getPassword());

        if (user != null) {

            if (!Strings.isNullOrEmpty(model.getPushId())) {
                //TODO绑定pushID
            }
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOK(rspModel);
        } else {
            return ResponseModel.buildLoginError();
        }


    }


    //127.0.0.1:8080/api/account/register
    @POST
    @Path("/register")
    //指定请求与返回的响应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {
        if (!RegisterModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null) {
            return ResponseModel.buildHaveAccountError();
        }
        user = UserFactory.findByName(model.getName().trim());

        if (user != null) {
            return ResponseModel.buildHaveNameError();
        }
        user = UserFactory.register(model.getAccount(), model.getPassword(), model.getName());
        if (user != null) {
            if (Strings.isNullOrEmpty(model.getPushId())) {
                bind(user, model.getPushId());
            }
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOK(rspModel);
        } else {
            return ResponseModel.buildRegisterError();
        }
    }


    @POST
    @Path("/bind/{pushId}")
    //指定请求与返回的相应体为Json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public ResponseModel<AccountRspModel> bind(@HeaderParam("token") String token, @PathParam("pushId") String pushId) {
        if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(pushId)) {
            return ResponseModel.buildParameterError();
        }
        User self = getSelf();
        return bind(self, pushId);
    }


    private ResponseModel<AccountRspModel> bind(User self, String pushId) {
        User user = UserFactory.bindPushId(self, pushId);
        if (user == null) {
            return ResponseModel.buildRegisterError();
        }
        //返回当前的账户
        AccountRspModel rspModel = new AccountRspModel(user, true);
        return ResponseModel.buildOK(rspModel);
    }


}
