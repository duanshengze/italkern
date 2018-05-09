package com.victoryze.web.italker.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by dsz on 2018/2/4.
 */
@Entity
@Table(name = "TB_GROUP")
public class Group {
    //这是一个主键
    @Id
    @PrimaryKeyJoinColumn
    //主键生成存储类型为UUID 自动生成UUID
    @GeneratedValue(generator = "uuid")
    //把uuid的生成器的定义为uuid2 ,uuid2是常规的UUID toString (uuid2 -有横杠)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    //不允许更改，不允许为null
    @Column(updatable = false, nullable = false)
    private String id;


    //群名称
    @Column(nullable = false)
    private String name;


    //群描述
    @Column(nullable = false)
    private String descrption;

    @Column(nullable = false)
    private String picture;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();
    //定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    //群的创建者
    //optional：可选为false 必须有一个创建者
    //fetch:加载方式为FetchType.EAGER 急加载 意味着加载群的信息时候必须加载owner信息
    //casecade：联机级别为ALL 所有的更改（更新 删除等）都将关系更新
    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerId")
    private User user;

    @Column(nullable = false,updatable = false,insertable = false)
    private String ownerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
