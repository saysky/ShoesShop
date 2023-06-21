package com.example.mall.service;

import com.example.mall.entity.Message;
import com.example.mall.entity.User;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * 私信表(Message)表服务接口
 *
 * @author makejava
 * @since 2021-03-11 15:45:25
 */
public interface MessageService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Message queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Message> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    Message insert(Message message);

    /**
     * 修改数据
     *
     * @param message 实例对象
     * @return 实例对象
     */
    Message update(Message message);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 根据用户ID查询聊天列表
     *
     * @param userId
     * @return
     */
    List<Message> getChatList(Long userId);

    /**
     * 获得用户和朋友的聊天列表，不包括状态为status的
     *
     * @param userId
     * @param friendId
     * @param statusList
     * @return
     */
    List<Message> getMessageList(Long userId, Long friendId, List<Integer> statusList);

    /**
     * 更新私信
     *
     * @param message
     */
    @Async
    void saveMessage(Message message);

    /**
     * 将私信状态改成delete
     *
     * @param id
     */
    void deleteMessage(Long id, Long userId);

    /**
     * 将多个私信状态改成delete
     *
     * @param ids
     */
    void deleteMessageBatch(List<Long> ids, Long userId);

    /**
     * 删除私信
     *
     * @param id
     */
    void removeMessage(Long id);

    /**
     * 删除多个私信
     *
     * @param ids
     */
    void removeMessage(List<Long> ids);


    /**
     * 获得最上面一条Message
     *
     * @param userId
     * @param friendId
     * @param statusList
     * @return
     */
    Message getTopMessage(Long userId, Long friendId, List<Integer> statusList);


    /**
     * 获得所有未读的数量
     *
     * @param userId
     */
    Integer countNotReadMessageSize(Long userId);

    /**
     * 获得未阅读的私信列表
     *
     * @param userId
     * @param status
     * @return
     */
    List<Message> listNotReadMessage(Long userId, Integer status);

    /**
     * 获得未阅读私信列表的大小
     *
     * @param userId
     * @param status
     * @return
     */
    List<Integer> listCountNotReadMessageSize(Long userId, Integer status);


}