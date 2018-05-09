package com.victoryze.web.italker.push.factory;


import com.google.common.base.Strings;

import com.victoryze.web.italker.push.bean.db.User;
import com.victoryze.web.italker.push.bean.db.UserFollow;
import com.victoryze.web.italker.push.utils.Hib;
import com.victoryze.web.italker.push.utils.TextUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;


import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by dsz on 2018/2/20.
 */
public class UserFactory {


    public static User login(String account, String password) {
        final String accountStr = account.trim();
        String encodePassword = encodePassword(password);
        User user = Hib.query(new Hib.Query<User>() {
            @Override
            public User query(Session session) {
                Query result = session.createQuery("from User where phone=:phone and" +
                        " password=:password")
                        .setParameter("phone", accountStr)
                        .setParameter("password", encodePassword);
                return (User) result.uniqueResult();
            }
        });
        if (user != null) {
            user = login(user);
        }

        return user;
    }

    public static User findByToken(String token) {
        return Hib.query(new Hib.Query<User>() {
            @Override
            public User query(Session session) {
                Query result = session.createQuery("from User where token=:token")
                        .setParameter("token", token);
                return (User) result.uniqueResult();
            }
        });
    }

    /**
     * 通过phone找到User
     *
     * @param phone
     * @return
     */
    public static User findByPhone(String phone) {
        return Hib.query(new Hib.Query<User>() {
            @Override
            public User query(Session session) {

                return (User) session.createQuery("from User where phone=:inPhone")
                        .setParameter("inPhone", phone)
                        .uniqueResult();
            }
        });
    }

    /**
     * 通过名字查找用户
     *
     * @param name
     * @return
     */
    public static User findByName(String name) {
        return Hib.query(new Hib.Query<User>() {
            @Override
            public User query(Session session) {
                return (User) session.createQuery("from User where name=:name")
                        .setParameter("name", name)
                        .uniqueResult();
            }
        });
    }

    /**
     * 用户注册
     * 注册操作需要写入数据库，并返回数据库中的User信息
     *
     * @param account
     * @param password
     * @param name
     * @return
     */
    public static User register(String account, String password, String name) {
        account = account.trim();
        password = encodePassword(password);
        User user = createUser(account, password, name);

        if (user != null) {
            user = login(user);
        }
        return user;
    }

    private static User login(User user) {
        String newToken = UUID.randomUUID().toString();
        //进行一次Base64格式化
        newToken = TextUtil.endcodeBase64(newToken);
        user.setToken(newToken);
        return update(user);
    }

    public static User update(User user) {

        return Hib.query(new Hib.Query<User>() {
            @Override
            public User query(Session session) {
                session.saveOrUpdate(user);
                return user;
            }
        });

    }

    private static User createUser(String account, String password, String name) {
        User user = new User();
        user.setName(name);
        user.setPhone(account);
        user.setPassword(password);

        return Hib.query(new Hib.Query<User>() {
            @Override
            public User query(Session session) {
                session.save(user);
                return user;
            }
        });


    }


    private static String encodePassword(String password) {

        //密码去除首位空格
        password = password.trim();
        //进行MD5非对称加密 加盐会更加安全，盐也要存储
        password = TextUtil.getMD5(password);
        //再进行一次对称Base64加密，当然可以采用加盐的方案
        return TextUtil.endcodeBase64(password);


    }

    /**
     * 给当前的账号绑定PushId
     *
     * @param user   自己的User
     * @param pushId 自己的PushId
     * @return
     */
    public static User bindPushId(User user, String pushId) {

        if (Strings.isNullOrEmpty(pushId)) {
            return null;
        }
        Hib.queryOnly(new Hib.QueryOnly() {
            @Override
            public void query(Session session) {
                List<User> userList = session.createQuery("from User where lower(pushId)=:pushId and id!=:userId")
                        .setParameter("pushId", pushId.toLowerCase())
                        .setParameter("userId", user.getId())
                        .list();
                //其他用户取消绑定
                for (User u : userList) {
                    u.setPushId(null);
                    session.saveOrUpdate(u);
                }
            }
        });
        //如果当前需要绑定的设备id，之前已经绑定了
        //不需要额外绑定
        if (pushId.equalsIgnoreCase(user.getPushId())) {
            return user;
        } else {
            //如果当前账号之前的设备id和需要的绑定的id不同
            //那么需要单点登录 让之前的设备退出账号
            //给之前的设备推送以条退出消息
            if (!Strings.isNullOrEmpty(user.getPushId())) {
                //推送一个退出消息
                PushFactory.pushLogout(user, user.getPushId());
            }
            user.setPushId(pushId);
            return update(user);
        }

    }

    /**
     * 查询联系人
     *
     * @param self User
     * @return
     */
    public static List<User> contacts(User self) {
        return Hib.query(new Hib.Query<List<User>>() {
            @Override
            public List<User> query(Session session) {
                //重新加载一次用户信息到self 和当前的session中
                session.load(self, self.getId());
                Set<UserFollow> follows = self.getFollowing();

                return follows.stream().map(new Function<UserFollow, User>() {
                    @Override
                    public User apply(UserFollow input) {

                        return input.getTarget();
                    }
                }).collect(Collectors.toList());
            }
        });
    }


    /**
     * 通过id查询user
     *
     * @param id
     * @return
     */
    public static User findById(String id) {


        return Hib.query(new Hib.Query<User>() {
            @Override
            public User query(Session session) {

                //通过id 进行查询
                return session.get(User.class, id);
            }
        });
    }

    /**
     * 关注人的操作
     *
     * @param origin 发起者
     * @param target 被关注的人
     * @param alias  备注名
     * @return
     */
    public static User follow(User origin, User target, String alias) {
        UserFollow follow = getUserFollow(origin, target);
        if (follow != null) {
            //已关注,直接返回
            return follow.getTarget();
        }

        return Hib.query(new Hib.Query<User>() {
            @Override
            public User query(Session session) {
                //想要操作懒加载的数据 需要重新load一次
                session.load(origin, origin.getId());
                session.load(target, target.getId());
                //我关注人的时候,同时他也关注我
                //所有需要添加两条UserFollow数据
                UserFollow originFollow = new UserFollow();
                originFollow.setOrigin(origin);
                originFollow.setTarget(target);
                //关注是我对他的备注
                originFollow.setAlias(alias);
                UserFollow targetFollow = new UserFollow();
                //TODO修改
                targetFollow.setOrigin(target);
                targetFollow.setTarget(origin);
                session.save(originFollow);
                session.save(targetFollow);
                return target;
            }
        });
    }


    /**
     * 查询两个是否已经关注
     *
     * @param origin  发起者
     * @param tragert 被关注人
     * @return
     */
    public static UserFollow getUserFollow(User origin, User tragert) {
        return Hib.query(new Hib.Query<UserFollow>() {
            @Override
            public UserFollow query(Session session) {
                return (UserFollow) session.createQuery("from " +
                        "UserFollow  where originId=:originId and targetId=:targetId")
                        .setParameter("originId", origin.getId())
                        .setParameter("targetId", tragert.getId())
                        .uniqueResult();
            }
        });
    }

    /**
     * 搜索联系人的实现
     *
     * @param name 查询name 允许为空
     * @return 查询到的用户集合，如果为空，则返回最近的用户
     */

    public static List<User> search(String name) {

        if (Strings.isNullOrEmpty(name)) {
            name = "";//保证不能为null 减少后面的判断和额外的错误
        }
        String searchName = "%" + name + "%";//模糊匹配
        return Hib.query(new Hib.Query<List<User>>() {
            @Override
            public List<User> query(Session session) {
                //查询的条件：name忽略大小写 并且使用like (模糊)查询
                //头像和描述必须完善才能查询到
                return (List<User>) session.createQuery("from User where lower(name)like :name" +
                        " and portrait is not null and description is not null")
                        .setParameter("name", searchName)
                        .setMaxResults(20)//最多20条
                        .list();
            }
        });

    }
}
