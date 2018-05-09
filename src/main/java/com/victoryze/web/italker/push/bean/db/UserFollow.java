package com.victoryze.web.italker.push.bean.db;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户关系模型
 * 用于用户直接进行好友关系的实现
 * Created by dsz on 2018/2/4.
 */

@Entity
@Table(name = "TB_USER_FOLLOW")
public class UserFollow {


    //这是一个主键
    @Id
    @PrimaryKeyJoinColumn
    //主键生成存储类型为UUID
    @GeneratedValue(generator = "uuid")
    //把uuid的生成器的定义为uuid2 ,uuid2是常规的UUID toString (uuid2 -有横杠)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    //不允许更改，不允许为null
    @Column(updatable = false, nullable = false)
    private String id;


    //定义一个发起人  你关注某人 这是你
    //多对一 关系  你可以关注很多人  你的每一次关注都是一条记录
    //可以创建很多关注信息，所以是多对一
    //这里的多对一  是User 对应 多个UserFollow
    //optional不可选  必须存储 一条关注记录一定要有一个 "你"
    @ManyToOne(optional = false)
    //定义关联的表字段名为originId,对应的是User.id
    //定义的是数据库中的存储字段
    @JoinColumn(name = "originId")
    private User origin;
    //把这个列提取到我们的Model中 不允许为空 不允许更新插入
    @Column(nullable = false, updatable = false, insertable = false)
    private String originId;

    //定义关注的目标，你关注的人
    //也是多对一  你可以被很多人关注 每次 关注就是一条记录
    //多个UserFollie对应 一个USer
    @ManyToOne(optional = false)
    //定义关联的表字段名为originId,对应的是User.id
    @JoinColumn(name = "targetId")
    private User target;

    //把这个列提取到我们的Model中 不允许为空 不允许更新插入
    @Column(nullable = false, updatable = false, insertable = false)
    private String targetId;
    @Column
    private String alias;

    //定义为创建时间戳 在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();
    //定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();


    //我关注人的列表方法
    //对应的数据库表的字段为TB_USER_FOLLOW.originId
    @JoinColumn(name = "originId")
    //定义为懒加载
    @LazyCollection(LazyCollectionOption.EXTRA)
    //一个用户 可以有很多关注，每次关注都是一个记录 要和上面一起使用
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFollow> following = new HashSet<UserFollow>();

    //关注我的列表方法
    //对应的数据库表的字段为TB_USER_FOLLOW.originId
    @JoinColumn(name = "targetId")
    //定义为懒加载
    @LazyCollection(LazyCollectionOption.EXTRA)
    //一个用户 可以可以被多个关注，每次关注都是一个记录 要和上面一起使用
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFollow> followers = new HashSet<UserFollow>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrigin() {
        return origin;
    }

    public void setOrigin(User origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Set<UserFollow> getFollowing() {
        return following;
    }

    public void setFollowing(Set<UserFollow> following) {
        this.following = following;
    }

    public Set<UserFollow> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<UserFollow> followers) {
        this.followers = followers;
    }

}
