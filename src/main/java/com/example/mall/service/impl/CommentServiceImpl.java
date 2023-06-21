package com.example.mall.service.impl;

import com.example.mall.dao.CommentMapper;
import com.example.mall.entity.Comment;
import com.example.mall.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 言曌
 * @date 2021/2/28 12:50 下午
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> findByGoodsId(Long goodsId) {
        return commentMapper.findByGoodsId(goodsId);
    }

    @Override
    public Comment findByUserIdAndGoodsId(Long userId, Long goodsId) {
        return commentMapper.findByUserIdAndGoodsId(userId, goodsId);
    }

    @Override
    public void insert(Comment comment) {
        commentMapper.insert(comment);
    }
}
