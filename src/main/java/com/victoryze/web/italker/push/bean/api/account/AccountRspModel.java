package com.victoryze.web.italker.push.bean.api.account;


import com.google.gson.annotations.Expose;
import com.victoryze.web.italker.push.bean.card.UserCard;
import com.victoryze.web.italker.push.bean.db.User;

/**
 * 账号部分返回的Model
 * Created by dsz on 2018/2/20.
 */
public class AccountRspModel {
    //用户基本信息
    @Expose
    private UserCard user;
    //当前登录的账号
    @Expose
    private String account;
    //当前登录成功后获取的Token
    //可以根据Token获取用户的所有信息
    @Expose
    private String token;
    @Expose
    private boolean isBind;

    public AccountRspModel(User user) {
        this(user, false);
    }

    public AccountRspModel(User user, boolean isBind) {
        this.user = new UserCard(user);
        this.isBind = isBind;
        this.token = user.getToken();
        this.account = user.getPhone();
    }


    public UserCard getUser() {
        return user;
    }

    public void setUser(UserCard user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
