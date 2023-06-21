package com.example.mall.controller.admin;

import com.example.mall.common.Constants;
import com.example.mall.common.ServiceResultEnum;

import com.example.mall.entity.User;
import com.example.mall.service.UserService;
import com.example.mall.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/***/
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }

    @GetMapping({"/test"})
    public String test() {
        return "admin/test";
    }


    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        request.setAttribute("categoryCount", 0);
        request.setAttribute("blogCount", 0);
        request.setAttribute("linkCount", 0);
        request.setAttribute("tagCount", 0);
        request.setAttribute("commentCount", 0);
        request.setAttribute("path", "index");
        return "admin/index";
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session) {
        if (StringUtils.isEmpty(verifyCode)) {
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "账号或密码不能为空");
            return "admin/login";
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            session.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }
        String result = userService.adminLogin(userName, MD5Util.MD5Encode(password, "UTF-8"), session);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return "redirect:/admin/index";
        } else {
            session.setAttribute("errorMsg", "登录失败");
            return "admin/login";
        }
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        User adminUser = userService.findById(loginUserId);
        if (adminUser == null || !Constants.ADMIN_ROLE.equalsIgnoreCase(adminUser.getUserType())) {
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", adminUser.getLoginName());
        request.setAttribute("nickName", adminUser.getNickName());
        return "admin/profile";
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空";
        }
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        userService.updatePassword(loginUserId, newPassword);
        //修改成功后清空session中的数据，前端控制跳转至登录页
//        request.getSession().removeAttribute("loginUserId");
//        request.getSession().removeAttribute("loginUser");
//        request.getSession().removeAttribute("errorMsg");
        return ServiceResultEnum.SUCCESS.getResult();
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request,
                             @RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName,
                             HttpSession session) throws Exception {
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }
        Long loginUserId = (Long) request.getSession().getAttribute("loginUserId");
        User user = userService.findById(loginUserId);
        user.setLoginName(loginUserName);
        user.setNickName(nickName);
        try {
            userService.updateUserInfo(user, session);
            return ServiceResultEnum.SUCCESS.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return "修改失败";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }
}
