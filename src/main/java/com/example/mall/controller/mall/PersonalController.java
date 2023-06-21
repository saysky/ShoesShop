package com.example.mall.controller.mall;

import com.example.mall.service.GoodsMarkService;
import com.example.mall.service.UserService;
import com.example.mall.common.Constants;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.controller.vo.UserVO;
import com.example.mall.entity.User;
import com.example.mall.util.MD5Util;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.Result;
import com.example.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

@Controller
public class PersonalController {

    @Resource
    private UserService mallUserService;

    @Autowired
    private GoodsMarkService goodsMarkService;

    @GetMapping("/personal")
    public String personalPage(HttpServletRequest request,
                               HttpSession httpSession) {
        request.setAttribute("path", "personal");
        return "mall/personal";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute(Constants.MALL_USER_SESSION_KEY);
        return "mall/login";
    }

    @GetMapping({"/login", "login.html"})
    public String loginPage() {
        return "mall/login";
    }

    @GetMapping({"/register", "register.html"})
    public String registerPage() {
        return "mall/register";
    }


    @GetMapping({"/active", "active.html"})
    public String activePage() {
        return "mall/active";
    }


    @GetMapping("/personal/addresses")
    public String addressesPage() {
        return "mall/addresses";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestParam("loginName") String loginName,
                        @RequestParam("verifyCode") String verifyCode,
                        @RequestParam("password") String password,
                        HttpSession httpSession) {
        if (StringUtils.isEmpty(loginName)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(password)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
        }
//        String kaptchaCode = httpSession.getAttribute(Constants.MALL_VERIFY_CODE_KEY) + "";
//        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
//            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
//        }
        //todo 清verifyCode
        String loginResult = mallUserService.login(loginName, MD5Util.MD5Encode(password, "UTF-8"), httpSession);
        //登录成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(loginResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult);
    }


    @PostMapping("/active")
    @ResponseBody
    public Result active(@RequestParam("loginName") String loginName,
                         @RequestParam("verifyCode") String verifyCode,
                         @RequestParam("nickName") String nickName,
                         HttpSession httpSession) {
        if (StringUtils.isEmpty(loginName)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(nickName)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.NICK_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
        }
        String kaptchaCode = httpSession.getAttribute(Constants.MALL_VERIFY_CODE_KEY) + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
        }
        User user = mallUserService.selectByLoginName(loginName);
        if (user == null || !Objects.equals(user.getNickName(), nickName)) {
            return ResultGenerator.genFailResult("账号不存在");
        }

        // 激活
        Integer[] ids = {Integer.valueOf(user.getUserId() + "")};
        mallUserService.lockUsers(ids, 0);
        return ResultGenerator.genSuccessResult("激活成功");
    }

    @PostMapping("/register")
    @ResponseBody
    public Result register(@RequestParam("loginName") String loginName,
                           @RequestParam("verifyCode") String verifyCode,
                           @RequestParam("password") String password,
                           HttpSession httpSession) {
        if (StringUtils.isEmpty(loginName)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
        }
        if (StringUtils.isEmpty(password)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
        }
        if (password.length() < 6 || password.length() > 20) {
            return ResultGenerator.genFailResult("请输入6-20位密码");

        }
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
        }
        String kaptchaCode = httpSession.getAttribute(Constants.MALL_VERIFY_CODE_KEY) + "";
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
        }
        //todo 清verifyCode
        String registerResult = mallUserService.register(loginName, password);
        //注册成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(registerResult);
    }

    @PostMapping("/personal/updateInfo")
    @ResponseBody
    public Result updateInfo(@RequestBody User mallUser, HttpSession httpSession) throws Exception {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        mallUser.setUserId(user.getUserId());

        UserVO mallUserTemp = mallUserService.updateUserInfo(mallUser, httpSession);
        if (mallUserTemp == null) {
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        } else {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        }
    }


    @GetMapping("/marks")
    public String marksPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.MARK_PAGE_LIMIT);
        //封装我的订单数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("markPageResult", goodsMarkService.getMyMarks(pageUtil));
        request.setAttribute("path", "marks");
        return "mall/my-marks";
    }

    /**
     * 支付密码页面
     *
     * @param request
     * @return
     */
    @GetMapping("/password")
    public String personalPayPassword(HttpServletRequest request) {
        request.setAttribute("path", "password");
        return "mall/password";
    }

    /**
     * 更新密码
     *
     * @param password
     * @param httpSession
     * @return
     */
    @PostMapping("/updatePassword")
    @ResponseBody
    public Result updatePassword(@RequestParam("password") String password, HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (user == null) {
            return ResultGenerator.genFailResult("用户未登录");
        }
        if (password.length() < 6 || password.length() > 20) {
            return ResultGenerator.genFailResult("请输入6-20位密码");
        }
        mallUserService.updatePassword(user.getUserId(), password);
        return ResultGenerator.genSuccessResult("更新成功");
    }

    /**
     * 更新密码
     *
     * @param password
     * @param httpSession
     * @return
     */
    @PostMapping("/updatePayPassword")
    @ResponseBody
    public Result updatePayPassword(@RequestParam("password") String password, HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (user == null) {
            return ResultGenerator.genFailResult("用户未登录");
        }
        if (password.length() != 6) {
            return ResultGenerator.genFailResult("请输入6位支付密码");
        }
        mallUserService.updatePayPassword(user.getUserId(), password);
        return ResultGenerator.genSuccessResult("更新成功");
    }
}
