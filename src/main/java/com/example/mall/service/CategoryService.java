package com.example.mall.service;

import com.example.mall.controller.vo.IndexCategoryVO;
import com.example.mall.controller.vo.SearchPageCategoryVO;
import com.example.mall.entity.Goods;
import com.example.mall.entity.GoodsCategory;
import com.example.mall.util.PageQueryUtil;
import com.example.mall.util.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getCategorisPage(PageQueryUtil pageUtil);

    String saveCategory(GoodsCategory goodsCategory);

    String updateGoodsCategory(GoodsCategory goodsCategory);

    GoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回分类数据(首页调用)
     *
     * @return
     */
    List<IndexCategoryVO> getCategoriesForIndex();

    /**
     * 返回分类数据(搜索页调用)
     *
     * @param categoryId
     * @return
     */
    SearchPageCategoryVO getCategoriesForSearch(Long categoryId);

    /**
     * 根据parentId和level获取分类列表
     *
     * @param parentIds
     * @param categoryLevel
     * @return
     */
    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);


    /**
     * 返回分类数据(首页调用)
     *
     * @return
     */
    List<GoodsCategory> getGoodsCategoryWithGoods();

    /**
     * 查询所有
     * @return
     */
    List<GoodsCategory> findAllLevel1();

    /**
     * 更新pathTrace
     *
     * @param categoryId
     */
    void updatePathTraceAndLevel(Long categoryId);
}
