package com.example.mall.dao;

import com.example.mall.entity.Message;
import com.example.mall.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 私信表(Message)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-11 15:45:25
 */
public interface MessageMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Message queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Message> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param message 实例对象
     * @return 对象列表
     */
    List<Message> queryAll(Message message);

    /**
     * 新增数据
     *
     * @param message 实例对象
     * @return 影响行数
     */
    int insert(Message message);

    /**
     * 修改数据
     *
     * @param message 实例对象
     * @return 影响行数
     */
    int update(Message message);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);


    /**
     * 获得聊天列表
     *
     * @param userId
     * @return
     */
    List<Message> getChatList(Long userId);

    /**
     * 聊天详情页
     *
     * @param userId
     * @param friendId
     * @param statusList
     * @return
     */
    List<Message> findByUserAndFriendAndStatusIn(@Param("userId") Long userId,
                                                 @Param("friendId") Long friendId,
                                                 @Param("statusList") List<Integer> statusList);


    /**
     * 查找未读消息的最后一条
     *
     * @param userId
     * @param friendId
     * @param statusList
     * @return
     */
    Message findTopByUserAndFriendAndStatusInOrderByIdDesc(@Param("userId") Long userId,
                                                           @Param("friendId") Long friendId,
                                                           @Param("statusList") List<Integer> statusList);


    /**
     * 获得未读私信的总数
     *
     * @param userId
     * @param status
     * @return
     */
    Integer countByUserAndStatus(@Param("userId") Long userId,
                                 @Param("status") Integer status);


    /**
     * 查询未读的列表(每个好友只显示一个)
     *
     * @param userId
     * @param status
     * @return
     */
    List<Message> findByUserAndStatus(@Param("userId") Long userId,
                                      @Param("status") Integer status);


    /**
     * 查询未读的列表
     *
     * @param userId
     * @param status
     * @return
     */
    List<Integer> listCountNotReadMessageSize(@Param("userId") Long userId,
                                              @Param("status") Integer status);


