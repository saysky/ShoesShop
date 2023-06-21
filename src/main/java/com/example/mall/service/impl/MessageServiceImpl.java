package com.example.mall.service.impl;

import com.example.mall.common.MessageStatusEnum;
import com.example.mall.entity.Message;
import com.example.mall.dao.MessageMapper;
import com.example.mall.entity.User;
import com.example.mall.service.MessageService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 私信表(Message)表服务实现类
 *
 * @author makejava
 * @since 2021-03-11 15:45:25
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageMapper messageDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Message queryById(Long id) {
        return this.messageDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Message> queryAllByLimit(int offset, int limit) {
        return this.messageDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    @Override
    public Message insert(Message message) {
        this.messageDao.insert(message);
        return message;
    }

    /**
     * 修改数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    @Override
    public Message update(Message message) {
        this.messageDao.update(message);
        return this.queryById(message.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.messageDao.deleteById(id) > 0;
    }


    @Override
    public List<Message> getChatList(Long userId) {
        return messageDao.getChatList(userId);
    }

    @Override
    public List<Message> getMessageList(Long userId, Long friendId, List<Integer> statusList) {
        return messageDao.findByUserAndFriendAndStatusIn(userId, friendId, statusList);
    }

    @Override
    public void saveMessage(Message message) {
        if (message.getId() == null) {
            messageDao.insert(message);
        } else {
            messageDao.update(message);
        }
    }

    @Override
    public void deleteMessage(Long id, Long userId) {
        Message originalMessage = messageDao.queryById(id);
        if (originalMessage != null && Objects.equals(originalMessage.getUserId(), userId)) {
            originalMessage.setStatus(MessageStatusEnum.DELETE_MESSAGE.getCode());
            messageDao.update(originalMessage);
        }
    }

    @Override
    public void deleteMessageBatch(List<Long> ids, Long userId) {
        for (int i = 0; i < ids.size(); i++) {
            Long id = ids.get(i);
            Message originalMessage = messageDao.queryById(id);
            if (originalMessage != null && Objects.equals(originalMessage.getUserId(), userId)) {
                originalMessage.setStatus(MessageStatusEnum.DELETE_MESSAGE.getCode());
                messageDao.update(originalMessage);
            }
        }
    }

    @Override
    public void removeMessage(Long id) {
        messageDao.deleteById(id);
    }


    @Override
    public void removeMessage(List<Long> ids) {
        for (int i = 0; i < ids.size(); i++) {
            messageDao.deleteById(ids.get(i));
        }
    }


    @Override
    public Message getTopMessage(Long userId, Long friendId, List<Integer> statusList) {
        return messageDao.findTopByUserAndFriendAndStatusInOrderByIdDesc(userId, friendId, statusList);
    }


    @Override
    public Integer countNotReadMessageSize(Long userId) {
        return messageDao.countByUserAndStatus(userId, MessageStatusEnum.NOT_READ_MESSAGE.getCode());
    }

    @Override
    public List<Message> listNotReadMessage(Long userId, Integer status) {
        return messageDao.findByUserAndStatus(userId, status);
    }


    @Override
    public List<Integer> listCountNotReadMessageSize(Long userId, Integer status) {
        return messageDao.listCountNotReadMessageSize(userId, status);
    }
}