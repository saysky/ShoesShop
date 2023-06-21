package com.example.mall.controller.mall;

import com.example.mall.controller.vo.UserVO;
import com.example.mall.entity.Comment;
import com.example.mall.entity.GoodsMark;
import com.example.mall.entity.OrderItem;
import com.example.mall.service.*;
import com.example.mall.common.Constants;
import com.example.mall.controller.vo.GoodsDetailVO;
import com.example.mall.controller.vo.SearchPageCategoryVO;
import com.example.mall.entity.Goods;
import com.example.mall.util.BeanUtil;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.Result;
import com.example.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class GoodsController {

    @Resource
    private GoodsService mallGoodsService;
    @Resource
    private CategoryService mallCategoryService;

    @Autowired
    private GoodsMarkService goodsMarkService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping({"/search", "/search.html"})
    public String searchPage(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //封装分类数据
        if (params.containsKey("goodsCategoryId") && !StringUtils.isEmpty(params.get("goodsCategoryId") + "")) {
            Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
            SearchPageCategoryVO searchPageCategoryVO = mallCategoryService.getCategoriesForSearch(categoryId);
            if (searchPageCategoryVO != null) {
                request.setAttribute("goodsCategoryId", categoryId);
                request.setAttribute("searchPageCategoryVO", searchPageCategoryVO);
            }
        }
        //封装参数供前端回显
        if (params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")) {
            request.setAttribute("orderBy", params.get("orderBy") + "");
        }
        String keyword = "";
        //对keyword做过滤 去掉空格
        if (params.containsKey("keyword") && !StringUtils.isEmpty((params.get("keyword") + "").trim())) {
            keyword = params.get("keyword") + "";
        }
        request.setAttribute("keyword", keyword);
        params.put("keyword", keyword);
        //封装商品数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("pageResult", mallGoodsService.searchmallGoods(pageUtil));
        return "mall/search";
    }

    @GetMapping("/goods/detail/{goodsId}")
    public String detailPage(@PathVariable("goodsId") Long goodsId, HttpServletRequest request) {
        if (goodsId < 1) {
            return "error/error_5xx";
        }
        Goods goods = mallGoodsService.getMallGoodsById(goodsId);
        if (goods == null) {
            return "error/error_404";
        }
        GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
        BeanUtil.copyProperties(goods, goodsDetailVO);
        goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
        request.setAttribute("goodsDetail", goodsDetailVO);

        List<Comment> commentList = commentService.findByGoodsId(goodsId);
        for (Comment comment : commentList) {
            comment.setUser(userService.findById(comment.getUserId()));
        }
        request.setAttribute("commentList", commentList);
        return "mall/detail";
    }


    /**
     * 收藏商品
     *
     * @param goodsId
     * @param session
     * @return
     */
    @PostMapping("/goods/mark")
    @ResponseBody
    public Result markGoods(@RequestParam("goodsId") Long goodsId, HttpSession session) {
        UserVO user = (UserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (user == null) {
            return ResultGenerator.genFailResult("用户未登录");
        }

        GoodsMark temp = goodsMarkService.findByUserIdAndGoodsId(user.getUserId(), goodsId);
        if (temp != null) {
            return ResultGenerator.genFailResult("您已经收藏过了");
        }

        GoodsMark goodsMark = new GoodsMark();
        goodsMark.setGoodsId(goodsId);
        goodsMark.setCreateTime(new Date());
        goodsMark.setUserId(user.getUserId());
        goodsMarkService.insert(goodsMark);
        return ResultGenerator.genSuccessResult("收藏成功");
    }


    /**
     * 取消收藏商品
     *
     * @param id
     * @param session
     * @return
     */
    @PostMapping("/goods/unmark")
    @ResponseBody
    public Result unmarkGoods(@RequestParam("id") Long id, HttpSession session) {
        UserVO user = (UserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (user == null) {
            return ResultGenerator.genFailResult("用户未登录");
        }
        GoodsMark goodsMark = goodsMarkService.findById(id);
        if (goodsMark == null) {
            return ResultGenerator.genFailResult("收藏不存在");
        }
        if (Objects.equals(goodsMark.getUserId(), user.getUserId())) {
            goodsMarkService.deleteById(id);
        }
        return ResultGenerator.genSuccessResult("取消收藏成功");
    }

    /**
     * 评价商品
     *
     * @param session
     * @return
     */
    @PostMapping("/goods/comment")
    @ResponseBody
    @Transactional
    public Result commentGoods(@RequestParam("id") Long id,
                               @RequestParam("score") Integer score,
                               @RequestParam("content") String content, HttpSession session) {
        UserVO user = (UserVO) session.getAttribute(Constants.MALL_USER_SESSION_KEY);
        if (user == null) {
            return ResultGenerator.genFailResult("用户未登录");
        }


        OrderItem orderItem = orderService.findOrderItemById(id);
        if (orderItem == null) {
            return ResultGenerator.genFailResult("订单不存在");
        }
        if (orderItem.getIsComment() == 1) {
            return ResultGenerator.genFailResult("您已经评价过了");
        }

        Comment comment = new Comment();
        comment.setGoodsId(orderItem.getGoodsId());
        comment.setCreateTime(new Date());
        comment.setUserId(user.getUserId());
        comment.setScore(score);
        comment.setContent(content);
        commentService.insert(comment);

        orderItem.setIsComment(1);
        orderService.updateOrderItem(orderItem);
        return ResultGenerator.genSuccessResult("评价成功");
    }


}
