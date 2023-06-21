package com.example.mall.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 私信表(Message)实体类
 *
 * @author makejava
 * @since 2021-03-11 15:45:25
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 618818843942812612L;
    /**
     * ID
     */
    private Long id;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 状态 //消息状态：1未读，2已读，3删除
     */
    private Integer status;
    /**
     * 类型 //消息类型，1普通消息，2系统消息
     */
    private Integer type;
    /**
     * 对方ID
     */
    private Long friendId;
    /**
     * 接收人ID
     */
    private Long receiverId;
    /**
     * 发送人ID
     */
    private Long senderId;
    /**
     * 创建人
     */
    private Long userId;

    private User user;

    private User friend;

    private Boolean checked = false;


    public String easyCreateTime;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getEasyCreateTime() {
        return easyCreateTime;
    }

    public void setEasyCreateTime(String easyCreateTime) {
        this.easyCreateTime = easyCreateTime;
    }

    public Message() {
    }


    public Message(Long userId, Long friendId, Long senderId, Long receiverId, String content, Integer status) {
        this.userId = userId;
        this.friendId = friendId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.status = status;
    }
}