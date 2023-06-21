package com.example.mall.dao;

import com.example.mall.entity.ShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(ShoppingCartItem record);

    int insertSelective(ShoppingCartItem record);

    ShoppingCartItem selectByPrimaryKey(Long cartItemId);

    ShoppingCartItem selectByUserIdAndGoodsId(@Param("mallUserId") Long mallUserId, @Param("goodsId") Long goodsId);

    List<ShoppingCartItem> selectByUserId(@Param("mallUserId") Long mallUserId, @Param("number") int number);

    int selectCountByUserId(Long mallUserId);

    int updateByPrimaryKeySelective(ShoppingCartItem record);

    int updateByPrimaryKey(ShoppingCartItem record);

    int deleteBatch(List<Long> ids);
}