package com.example.mall.service.impl;

import com.example.mall.common.Constants;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.controller.vo.UserVO;
import com.example.mall.dao.MallUserMapper;
import com.example.mall.entity.User;
import com.example.mall.service.MessageService;
import com.example.mall.service.UserService;
import com.example.mall.util.BeanUtil;
import com.example.mall.util.MD5Util;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MallUserMapper mallUserMapper;

    @Autowired
    private MessageService messageService;

    @Override
    public PageResult getMallUsersPage(PageQueryUtil pageUtil) {
        List<User> mallUsers = mallUserMapper.findMallUserList(pageUtil);
        int total = mallUserMapper.getTotalMallUsers(pageUtil);
        PageResult pageResult = new PageResult(mallUsers, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String register(String loginName, String password) {
        if (mallUserMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        User registerUser = new User();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        if (mallUserMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String insertUser(User user) {
        if (mallUserMapper.insertSelective(user) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5, HttpSession httpSession) {
        User user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null && httpSession != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //姓名太长 影响页面展示
            if (user.getNickName() != null && user.getNickName().length() > 7) {
                String tempNickName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tempNickName);
            }
            UserVO mallUserVO = new UserVO();
            BeanUtil.copyProperties(user, mallUserVO);
            //设置购物车中的数量
            httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, mallUserVO);
            httpSession.setAttribute("loginUser", user.getNickName());
            httpSession.setAttribute("loginUserId", user.getUserId());

            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public String adminLogin(String loginName, String passwordMD5, HttpSession httpSession) {
        User user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null && Constants.ADMIN_ROLE.equalsIgnoreCase(user.getUserType()) && httpSession != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //姓名太长 影响页面展示
            if (user.getNickName() != null && user.getNickName().length() > 7) {
                String tempNickName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tempNickName);
            }
            UserVO mallUserVO = new UserVO();
            BeanUtil.copyProperties(user, mallUserVO);

            //设置购物车中的数量
            httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, mallUserVO);
            httpSession.setAttribute("loginUser", user.getNickName());
            httpSession.setAttribute("loginUserId", user.getUserId());
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public UserVO updateUserInfo(User mallUser, HttpSession httpSession) throws Exception {
        User checkLoginName = mallUserMapper.selectByLoginName(mallUser.getLoginName());
        User user = mallUserMapper.selectByPrimaryKey(mallUser.getUserId());
        if (user != null) {
            if (checkLoginName != null && !Objects.equals(checkLoginName.getUserId(), user.getUserId())) {
                throw new Exception("账号已占用");
            }
            user.setNickName(mallUser.getNickName());
            user.setAddress(mallUser.getAddress());
            user.setIntroduceSign(mallUser.getIntroduceSign());
            user.setPhone(mallUser.getPhone());
            user.setSkey(mallUser.getSkey());
            user.setOpenId(mallUser.getOpenId());
            user.setAvatar(mallUser.getAvatar());
            if (mallUserMapper.updateByPrimaryKeySelective(user) > 0) {
                UserVO mallUserVO = new UserVO();
                user = mallUserMapper.selectByPrimaryKey(mallUser.getUserId());
                if (httpSession != null) {
                    BeanUtil.copyProperties(user, mallUserVO);
                    httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, mallUserVO);
                }
                return mallUserVO;
            }
        }
        return null;
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        User user = mallUserMapper.selectByPrimaryKey(userId);
        if (user != null) {
            String passwordMD5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            user.setPasswordMd5(passwordMD5);
            mallUserMapper.updateByPrimaryKeySelective(user);
        }

    }

    @Override
    public void updatePayPassword(Long userId, String newPayPassword) {
        User user = mallUserMapper.selectByPrimaryKey(userId);
        if (user != null) {
            String passwordMD5 = MD5Util.MD5Encode(newPayPassword, "UTF-8");
            user.setPayPasswordMd5(passwordMD5);
            mallUserMapper.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    public Boolean lockUsers(Integer[] ids, int lockStatus) {
        if (ids.length < 1) {
            return false;
        }
        return mallUserMapper.lockUserBatch(ids, lockStatus) > 0;
    }

    @Override
    public User findById(Long userId) {
        return mallUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public User selectByLoginName(String loginName) {
        return mallUserMapper.selectByLoginName(loginName);
    }

    @Override
    public User findByOpenId(String openId) {
        return mallUserMapper.findByOpenId(openId);
    }

    @Override
    public User findBySkey(String skey) {
        return mallUserMapper.findBySkey(skey);
    }
}
