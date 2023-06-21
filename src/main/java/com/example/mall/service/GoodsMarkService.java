package com.example.mall.service;

import com.example.mall.entity.GoodsMark;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.PageResult;

import java.util.List;

/**
 * @author 言曌
 * @date 2021/2/27 4:11 下午
 */

public interface GoodsMarkService {

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    GoodsMark findById(Long id);

    /**
     * 根据用户ID和商品ID查询
     *
     * @param userId
     * @param goodsId
     * @return
     */
    GoodsMark findByUserIdAndGoodsId(Long userId, Long goodsId);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyMarks(PageQueryUtil pageUtil);

    /**
     * 删除收藏
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 添加收藏
     *
     * @param goodsMark
     */
    void insert(GoodsMark goodsMark);
}
