package com.example.mall.controller.mall;

import com.example.mall.common.Constants;
import com.example.mall.common.MessageStatusEnum;
import com.example.mall.controller.vo.MessageVO;
import com.example.mall.controller.vo.UserVO;
import com.example.mall.entity.Message;
import com.example.mall.entity.User;
import com.example.mall.service.MessageService;
import com.example.mall.service.UserService;
import com.example.mall.util.Result;
import com.example.mall.util.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 言曌
 * @date 2018/5/1 下午6:10
 */

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 聊天页面
     *
     * @param uid
     * @param model
     * @return
     */
    @GetMapping("/message")
    public String messages(
            @RequestParam(value = "uid", required = false) Long uid,
            HttpSession session, Model model) throws Exception {
        UserVO user = (UserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (user == null) {
            return "redirect:/login";
        }
        List<Message> chatList = messageService.getChatList(user.getUserId());

        // 1、如果没有传uid，根据角色设置uid为某个值
        // 客户查看管理员,管理员查看第一条消息，
        if (uid == null) {
            if (!Constants.ADMIN_ROLE.equalsIgnoreCase(user.getUserType())) {
                uid = Constants.ADMIN_USER_ID;
            } else {
                if (chatList.size() > 0) {
                    uid = chatList.get(0).getFriendId();
                }
            }
        }

        //1、左侧的聊天列表
        List<Integer> statusList = new ArrayList<>();
        statusList.add(MessageStatusEnum.HAD_READ_MESSAGE.getCode());
        statusList.add(MessageStatusEnum.NOT_READ_MESSAGE.getCode());

        //有指定的用户，则默认打开与指定的用户的聊天框
        if (uid != null) {
            User friend = userService.findById(uid);
            if (friend == null) {
                throw new Exception("用户不存在！");
            }

            List<Message> messageList = messageService.getMessageList(user.getUserId(), uid, statusList);
            //3、将所有未读状态改为已读
            if (messageList.size() > 0) {
                for (int i = 0; i < messageList.size(); i++) {
                    //未读标记为已读
                    if (messageList.get(i).getStatus().equals(MessageStatusEnum.NOT_READ_MESSAGE.getCode())) {
                        Message temp = messageList.get(i);
                        temp.setStatus(MessageStatusEnum.HAD_READ_MESSAGE.getCode());
                        messageService.saveMessage(temp);
                    }
                }
            }

            for (Message message : chatList) {
                if (Objects.equals(message.getFriendId(), uid)) {
                    message.setChecked(true);
                }
            }

            model.addAttribute("messageList", messageList);
            model.addAttribute("friend", friend);

        }
        model.addAttribute("chatList", chatList);
        return "mall/message";
    }


    /**
     * 发布私信
     *
     * @param friendId
     * @param content
     * @return
     */
    @PostMapping("/message/send")
    public String createMessage(@RequestParam("friendId") Long friendId,
                                @RequestParam("content") String content,
                                HttpSession session) throws Exception {

        //1、判断friendId是否存在
        User friend = userService.findById(friendId);
        if (friend == null) {
            throw new Exception("用户不存在");
        }
        UserVO user = (UserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (user == null) {
            return "redirect:/login";
        }

        //2、添加两条message
        //自己的一条消息
        Message myMessage = new Message(user.getUserId(), friend.getUserId(), user.getUserId(), friend.getUserId(), content, MessageStatusEnum.HAD_READ_MESSAGE.getCode());
        //对方的一条消息
        Message yourMessage = new Message(friend.getUserId(), user.getUserId(), user.getUserId(), friend.getUserId(), content, MessageStatusEnum.NOT_READ_MESSAGE.getCode());
        messageService.saveMessage(myMessage);
        messageService.saveMessage(yourMessage);
        return "redirect:/message?uid=" + friendId;

    }

    /**
     * 清空与一个好友的私信
     *
     * @param friendId
     * @return
     */
    @PostMapping("/message/clear")
    @ResponseBody
    public Result clearMessage(@RequestParam("uid") Long friendId,
                               HttpSession session) throws Exception {
        UserVO user = (UserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (user == null) {
            return ResultGenerator.genFailResult("用户未登录");
        }
        User friend = userService.findById(friendId);
        if (friend == null) {
            throw new Exception("用户不存在");
        }
        // 修改状态
        List<Integer> statusList = new ArrayList<>();
        statusList.add(MessageStatusEnum.NOT_READ_MESSAGE.getCode());
        statusList.add(MessageStatusEnum.HAD_READ_MESSAGE.getCode());
        List<Message> messageList = messageService.getMessageList(user.getUserId(), friend.getUserId(), statusList);
        for (int i = 0; i < messageList.size(); i++) {
            Message message = messageList.get(i);
            message.setStatus(MessageStatusEnum.DELETE_MESSAGE.getCode());
            messageService.saveMessage(message);
        }
        return ResultGenerator.genSuccessResult();

    }


    /**
     * 未读消息数量
     *
     * @return
     */
    @GetMapping("/message/notReadCount")
    @ResponseBody
    public Result clearMessage(HttpSession session) {
        UserVO user = (UserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (user == null) {
            return ResultGenerator.genFailResult("用户未登录");
        }
        Integer notReadCount = messageService.countNotReadMessageSize(user.getUserId());
        return ResultGenerator.genSuccessResult(notReadCount);

    }


}
