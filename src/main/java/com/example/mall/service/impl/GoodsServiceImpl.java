package com.example.mall.service.impl;

import com.example.mall.common.ServiceResultEnum;
import com.example.mall.controller.vo.SearchGoodsVO;
import com.example.mall.entity.Goods;
import com.example.mall.service.GoodsService;
import com.example.mall.dao.GoodsMapper;
import com.example.mall.util.BeanUtil;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public PageResult getMallGoodsPage(PageQueryUtil pageUtil) {
        List<Goods> goodsList = goodsMapper.findmallGoodsList(pageUtil);
        int total = goodsMapper.getTotalmallGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String savemallGoods(Goods goods) {
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSavemallGoods(List<Goods> mallGoodsList) {
        if (!CollectionUtils.isEmpty(mallGoodsList)) {
            goodsMapper.batchInsert(mallGoodsList);
        }
    }

    @Override
    public String updatemallGoods(Goods goods) {
        Goods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Goods getMallGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }


    @Override
    public PageResult searchmallGoods(PageQueryUtil pageUtil) {
        List<Goods> goodsList = goodsMapper.findmallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalmallGoodsBySearch(pageUtil);
        List<SearchGoodsVO> mallSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            mallSearchGoodsVOS = BeanUtil.copyList(goodsList, SearchGoodsVO.class);
            for (SearchGoodsVO mallSearchGoodsVO : mallSearchGoodsVOS) {
                String goodsName = mallSearchGoodsVO.getGoodsName();
                String goodsIntro = mallSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    mallSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    mallSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(mallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public Integer selectCount() {
        return goodsMapper.getTotalmallGoods(null);
    }

    @Override
    public List<String> getSearchHelper(String keyword) {
        return goodsMapper.getSearchHelper(keyword);
    }
}
