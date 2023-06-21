package com.example.mall.service;

import com.example.mall.controller.vo.UserVO;
import com.example.mall.entity.User;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.PageResult;

import javax.servlet.http.HttpSession;

public interface UserService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getMallUsersPage(PageQueryUtil pageUtil);

    /**
     * 用户注册
     *
     * @param loginName
     * @param password
     * @return
     */
    String register(String loginName, String password);

    /**
     * 新增用户
     *
     * @param user
     */
    String insertUser(User user);


    /**
     * 普通用户登录
     *
     * @param loginName
     * @param passwordMD5
     * @param httpSession
     * @return
     */
    String login(String loginName, String passwordMD5, HttpSession httpSession);


    /**
     * 管理员登录
     *
     * @param loginName
     * @param passwordMD5
     * @param httpSession
     * @return
     */
    String adminLogin(String loginName, String passwordMD5, HttpSession httpSession);

    /**
     * 用户信息修改并返回最新的用户信息
     *
     * @param mallUser
     * @return
     */
    UserVO updateUserInfo(User mallUser, HttpSession httpSession) throws Exception;

    /**
     * 更新登录密码
     *
     * @param userId
     * @param newPassword
     */
    void updatePassword(Long userId, String newPassword);


    /**
     * 更新登录支付密码
     *
     * @param userId
     * @param newPayPassword
     */
    void updatePayPassword(Long userId, String newPayPassword);

    /**
     * 用户禁用与解除禁用(0-未锁定 1-已锁定)
     *
     * @param ids
     * @param lockStatus
     * @return
     */
    Boolean lockUsers(Integer[] ids, int lockStatus);

    /**
     * 根据ID查询
     *
     * @param userId
     * @return
     */
    User findById(Long userId);

    /**
     * 根据账号查询
     *
     * @param loginName
     * @return
     */
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


}
