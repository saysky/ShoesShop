package com.example.mall.service.impl;

import com.example.mall.dao.GoodsMapper;
import com.example.mall.dao.GoodsMarkMapper;
import com.example.mall.entity.GoodsMark;
import com.example.mall.service.GoodsMarkService;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author 言曌
 * @date 2021/2/27 4:11 下午
 */
@Service
public class GoodsMarkServiceImpl implements GoodsMarkService {

    @Autowired
    private GoodsMarkMapper goodsMarkMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public GoodsMark findById(Long id) {
        return goodsMarkMapper.findById(id);
    }

    @Override
    public GoodsMark findByUserIdAndGoodsId(Long userId, Long goodsId) {
        return goodsMarkMapper.findByUserIdAndGoodsId(userId, goodsId);
    }

    @Override
    public PageResult getMyMarks(PageQueryUtil pageUtil) {
        int total = goodsMarkMapper.getTotal(pageUtil);
        List<GoodsMark> goodsMarks = goodsMarkMapper.findAll(pageUtil);
        for (GoodsMark goodsMark : goodsMarks) {
            goodsMark.setGoods(goodsMapper.selectByPrimaryKey(goodsMark.getGoodsId()));
        }
        PageResult pageResult = new PageResult(goodsMarks, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public void deleteById(Long id) {
        goodsMarkMapper.deleteById(id);
    }

    @Override
    public void insert(GoodsMark goodsMark) {
        goodsMarkMapper.insert(goodsMark);
    }
}
