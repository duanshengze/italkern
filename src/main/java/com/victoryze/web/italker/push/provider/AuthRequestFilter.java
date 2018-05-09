package com.victoryze.web.italker.push.provider;

import com.google.common.base.Strings;

import com.victoryze.web.italker.push.bean.api.base.ResponseModel;
import com.victoryze.web.italker.push.bean.db.User;
import com.victoryze.web.italker.push.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;

/**
 * Created by dsz on 2018/2/20.
 * 用于所有的请求的接口的过滤和拦截
 */
public class AuthRequestFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        if (relationPath.startsWith("account/login") ||
                relationPath.startsWith("account/register")) {
            //直接走正常逻辑，不做拦截
            return;
        }

        //从Headers中去找到第一个token节点
        String token = requestContext.getHeaders().getFirst("token");
        if (!Strings.isNullOrEmpty(token)) {
            //查询自己的信息
            final User self = UserFactory.findByToken(token);
            if (self != null) {
                //给当前请求添加一个上下文
                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        //User实现 Principal接口
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        //可以在这里写入用户的权限 role 是权限名


                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        //默认false即可 HTTPS
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        //默认即可
                        return null;
                    }
                });
                return;
            }
        }
        //直接返回一个账号需要登录的Model
        ResponseModel model = ResponseModel.buildAccountError();
        //构建一个返回
        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();
        //拦截 停止一个请求的继续下发 调用该方法后返回请求
        //不会走到Service中去
        requestContext.abortWith(response);


    }
}
