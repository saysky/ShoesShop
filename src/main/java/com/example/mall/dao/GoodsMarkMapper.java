package com.example.mall.dao;

import com.example.mall.entity.GoodsMark;
import com.example.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 言曌
 * @date 2021/2/27 4:05 下午
 */
@Mapper
public interface GoodsMarkMapper {


    /**
     * 查询该用户的所有收藏
     *
     * @param userId
     * @return
     */
    List<GoodsMark> findAll(PageQueryUtil pageUtil);

    /**
     * 根据用户ID和商品ID查询
     * @param userId
     * @param goodsId
     * @return
     */
    GoodsMark findByUserIdAndGoodsId(@Param("userId") Long userId,
                                     @Param("goodsId") Long goodsId);


    int getTotal(PageQueryUtil pageUtil);

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


    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    GoodsMark findById(Long id);

}
