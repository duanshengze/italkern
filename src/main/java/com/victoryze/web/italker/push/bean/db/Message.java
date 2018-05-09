package com.victoryze.web.italker.push.bean.db;


import com.victoryze.web.italker.push.bean.api.message.MessageCreateModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by dsz on 2018/2/4.
 * 消息Model
 */
@Entity
@Table(name = "TB_MESSAGE")
public class Message {

    public static final int RECEIVER_TYPE_NONE = 1;

    public static final int RECEIVER_TYPE_GROUP = 2;


    public static final int TYPE_STR = 1;//字符串类型

    public static final int TYPE_PIC = 1;//图片类型

    public static final int TYPE_FILE = 3;//文件类型

    public static final int TYPE_AUDIO = 4;//语音类型

    //不自动生成 避免负责的服务端和客户端的映射guanx
    @Id
    @PrimaryKeyJoinColumn
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;
    //内容不允许为空  类型为text 可以发送比较长的内容
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    //附件 允许为空
    @Column
    private String attach;
    //类型不允许为空
    @Column(nullable = false)
    private int type;

    //发送者 不为空
    //多个消息对应一个发送者
    @JoinColumn(name = "senderId")
    @ManyToOne(optional = false)
    private User sender;
    //这个字段仅仅只是为了对应sender的数据库字段senderId
    //不允许手动的更新或者插入
    @Column(updatable = false, insertable = false)
    private String senderId;
    //接受者 可为空

    @JoinColumn(name = "receiverId")
    @ManyToOne
    private User receiver;
    //定义为创建时间戳 在创建时就已经写入
    //不允许手动的更新或者插入
    @Column(nullable = false, updatable = false, insertable = false)
    private String receiverId;


    @JoinColumn(name = "groupId")
    @ManyToOne
    private Group group;
    //定义为创建时间戳 在创建时就已经写入
    //不允许手动的更新或者插入
    //可为空
    @Column(updatable = false, insertable = false)
    private String groupId;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();
    //定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    public Message() {
    }

    public Message(User sender, User receiver, MessageCreateModel model) {
        this.sender=sender;
        this.receiver=receiver;
        this.id=model.getId();
        this.attach=model.getAttach();
        this.content=model.getContent();
        this.type=model.getType();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
