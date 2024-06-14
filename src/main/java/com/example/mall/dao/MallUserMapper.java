package com.example.mall.dao;

import com.example.mall.entity.User;
import com.example.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    User selectByLoginName(String loginName);

    /**
     * 根据openId查询用户
     *
     * @param openId
     * @return
     */
    User findByOpenId(String openId);

    /**
     * 根据skey查询用户
     *
     * @param skey
     * @return
     */
    User findBySkey(String skey);


    User selectByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("password") String password);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> findMallUserList(PageQueryUtil pageUtil);

    int getTotalMallUsers(PageQueryUtil pageUtil);

    int lockUserBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);


