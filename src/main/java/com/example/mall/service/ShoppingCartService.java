package com.example.mall.service;

import com.example.mall.controller.vo.ShoppingCartItemVO;
import com.example.mall.entity.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param mallShoppingCartItem
     * @return
     */
    String savemallCartItem(ShoppingCartItem mallShoppingCartItem);

    /**
     * 修改购物车中的属性
     *
     * @param mallShoppingCartItem
     * @return
     */
    String updatemallCartItem(ShoppingCartItem mallShoppingCartItem);

    /**
     * 简单更新
     *
     * @param shoppingCartItem
     */
    void update(ShoppingCartItem shoppingCartItem);

    /**
     * 获取购物项详情
     *
     * @param mallShoppingCartItemId
     * @return
     */
    ShoppingCartItem getMallCartItemById(Long mallShoppingCartItemId);

    /**
     * 获取购物项详情
     *
     * @param userId
     * @param goodsId
     * @return
     */
    ShoppingCartItem getMallCartItemById(Long userId, Long goodsId);

    /**
     * 删除购物车中的商品
     *
     * @param mallShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long mallShoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param mallUserId
     * @return
     */
    List<ShoppingCartItemVO> getMyShoppingCartItems(Long mallUserId);


    /**
     * 获取我的购物车中的列表数据
     *
     * @param mallUserId
     * @return
     */
    List<ShoppingCartItem> findByUserId(Long mallUserId);
}
