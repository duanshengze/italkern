package com.victoryze.web.italker.push.service;



import com.victoryze.web.italker.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by dsz on 2018/2/20.
 */
public class BaseService {

    @Context
    protected SecurityContext securityContext;

    /**
     * 从上下文中直接获取自己的信息
     *
     * @return User
     */
    User getSelf() {
        return (User) securityContext.getUserPrincipal();
    }
}
