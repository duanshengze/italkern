package com.victoryze.web.italker.push.service;

import com.google.common.base.Strings;
import com.victoryze.web.italker.push.bean.api.base.PushModel;
import com.victoryze.web.italker.push.bean.api.base.ResponseModel;
import com.victoryze.web.italker.push.bean.api.user.UpdateInfoModel;
import com.victoryze.web.italker.push.bean.card.UserCard;
import com.victoryze.web.italker.push.bean.db.User;
import com.victoryze.web.italker.push.factory.UserFactory;
import com.victoryze.web.italker.push.utils.PushDispatcher;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Created by dsz on 2018/2/20.
 */
@Path("/user")
public class UserService extends BaseService {


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model) {
        if (!UpdateInfoModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User self = getSelf();
        self = model.updateToUser(self);
        self = UserFactory.update(self);
        //构建自己的用户信息
        UserCard card = new UserCard(self, true);
        //返回
        return ResponseModel.buildOK(card);
    }

    /**
     * 拉取联系人
     *
     * @return
     */
    @GET
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact() {

        User self = getSelf();

        PushModel model = new PushModel();
        model.add(new PushModel.Entity(0, "hello"));
        PushDispatcher dispatcher = new PushDispatcher();
        dispatcher.add(self, model);
        dispatcher.submit();


        //拿到我的联系人
        List<User> users = UserFactory.contacts(self);
        List<UserCard> userCards = users.stream().map(new Function<User, UserCard>() {
            @Override
            public UserCard apply(User user) {
                return new UserCard(user, true);
            }
        }).collect(Collectors.toList());//map操作相当于转置操作
        return ResponseModel.buildOK(userCards);
    }


    //关注人
    //简化操作：关注人的操作其实是双方同时关注
    @PUT //修改类使用put
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
        User self = getSelf();
        //不能关注自己
        if (self.getId().equalsIgnoreCase(followId)) {
            //返回参数异常
            return ResponseModel.buildParameterError();
        }

        User followUser = UserFactory.findById(followId);
        if (followUser == null) {
            //未找到人
            return ResponseModel.buildNotFoundUserError(null);
        }
        //备注默认没有 后面扩展
        followUser = UserFactory.follow(self, followUser, null);
        if (followUser == null) {
            //关注失败了 返回服务器异常
            return ResponseModel.buildServiceError();
        }
        //TODO 通知我关注的人 我关注了他
        //通知我关注的人 我关注他
        //给他发送一个  我信息过去
//        PushFactory.pushFollow(followUser, new UserCard(self));

        //返回关注的人的信息
        return ResponseModel.buildOK(new UserCard(followUser, true));

    }

    @GET
    @Path("{id}")////127.0.0.1:8080/api/user/{id}
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> getUser(@PathParam("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            //参数异常
            return ResponseModel.buildParameterError();
        }
        User self = getSelf();
        if (self.getId().equalsIgnoreCase(id)) {
            //返回自己，不必查询数据库
            return ResponseModel.buildOK(new UserCard(self, true));
        }
        User user = UserFactory.findById(id);
        if (user == null) {
            //没有找到用户
            return ResponseModel.buildNotFoundUserError(null);
        }

        //如果我们之间有关注的记录，则我已关注需要查询信息的用户
        boolean isFollow = UserFactory.getUserFollow(self, user) != null;
        return ResponseModel.buildOK(new UserCard(user, isFollow));
    }


    @GET
    @Path("/search/{name:(.*)?}")//名字为任意字符可以为空
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@PathParam("name") String name) {
        User self = getSelf();
        List<User> searchUsers = UserFactory.search(name);
        //把查询的人封装为UserCard
        //判断这些人是否有我关注的人
        //如果有，则返回关系状态中应该设置好的状态
        List<User> contacts = UserFactory.contacts(self);
        List<UserCard> userCards = searchUsers.stream().map(new Function<User, UserCard>() {
            @Override
            public UserCard apply(User user) {
                //判断这个人是否在我的联系人中
                boolean isFollow = user.getId().equalsIgnoreCase(self.getId())
                        //进行联系人的任意匹配 匹配其中的Id字段
                        || contacts.stream().anyMatch(new Predicate<User>() {
                    @Override
                    public boolean test(User contact) {
                        return contact.getId().equalsIgnoreCase(user.getId());
                    }
                });
                return new UserCard(user, isFollow);
            }
        }).collect(Collectors.toList());
        return ResponseModel.buildOK(userCards);
    }


}
