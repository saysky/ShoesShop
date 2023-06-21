package com.example.mall.service;

import com.example.mall.entity.Comment;

import java.util.List;

/**
 * @author 言曌
 * @date 2021/2/28 12:49 下午
 */

public interface CommentService {

    /**
     * 根据商品ID查询
     * @param goodsId
     * @return
     */
    List<Comment> findByGoodsId(Long goodsId);

    /**
     * 根据商品ID和用户ID查询
     * @param userId
     * @param goodsId
     * @return
     */
    Comment findByUserIdAndGoodsId(Long userId, Long goodsId);

    /**
     * 添加评论
     *
     * @param comment
     */
    void insert(Comment comment);

}
