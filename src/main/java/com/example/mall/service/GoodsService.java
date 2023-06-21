package com.example.mall.service;

import com.example.mall.entity.Goods;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.PageResult;

import java.util.List;

public interface GoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String savemallGoods(Goods goods);

    /**
     * 批量新增商品数据
     *
     * @param mallGoodsList
     * @return
     */
    void batchSavemallGoods(List<Goods> mallGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updatemallGoods(Goods goods);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    Goods getMallGoodsById(Long id);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchmallGoods(PageQueryUtil pageUtil);

    /**
     * 查询商品总数
     *
     * @return
     */
    Integer selectCount();

    /**
     * 根据关键词查询商品
     *
     * @param keyword
     * @return
     */
    List<String> getSearchHelper(String keyword);
}
