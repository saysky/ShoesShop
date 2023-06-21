package com.example.mall.service.impl;

import com.example.mall.common.Constants;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.controller.vo.ShoppingCartItemVO;
import com.example.mall.entity.Goods;
import com.example.mall.entity.ShoppingCartItem;
import com.example.mall.dao.GoodsMapper;
import com.example.mall.dao.ShoppingCartItemMapper;
import com.example.mall.service.ShoppingCartService;
import com.example.mall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartItemMapper mallShoppingCartItemMapper;

    @Autowired
    private GoodsMapper mallGoodsMapper;

    //todo 修改session中购物项数量

    @Override
    public String savemallCartItem(ShoppingCartItem mallShoppingCartItem) {
        ShoppingCartItem temp = mallShoppingCartItemMapper.selectByUserIdAndGoodsId(mallShoppingCartItem.getUserId(), mallShoppingCartItem.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            //todo count = tempCount + 1
            temp.setGoodsCount(mallShoppingCartItem.getGoodsCount());
            mallShoppingCartItem.setCartItemId(temp.getCartItemId());
            return updatemallCartItem(temp);
        }
        Goods mallGoods = mallGoodsMapper.selectByPrimaryKey(mallShoppingCartItem.getGoodsId());
        //商品为空
        if (mallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = mallShoppingCartItemMapper.selectCountByUserId(mallShoppingCartItem.getUserId());
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (mallShoppingCartItemMapper.insertSelective(mallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updatemallCartItem(ShoppingCartItem mallShoppingCartItem) {
        ShoppingCartItem mallShoppingCartItemUpdate = mallShoppingCartItemMapper.selectByPrimaryKey(mallShoppingCartItem.getCartItemId());
        if (mallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出最大数量
        if (mallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        mallShoppingCartItemUpdate.setGoodsCount(mallShoppingCartItem.getGoodsCount());
        mallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (mallShoppingCartItemMapper.updateByPrimaryKeySelective(mallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void update(ShoppingCartItem shoppingCartItem) {
        mallShoppingCartItemMapper.updateByPrimaryKeySelective(shoppingCartItem);
    }

    @Override
    public ShoppingCartItem getMallCartItemById(Long mallShoppingCartItemId) {
        return mallShoppingCartItemMapper.selectByPrimaryKey(mallShoppingCartItemId);
    }

    @Override
    public ShoppingCartItem getMallCartItemById(Long userId, Long goodsId) {
        return mallShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, goodsId);
    }

    @Override
    public Boolean deleteById(Long mallShoppingCartItemId) {
        return mallShoppingCartItemMapper.deleteByPrimaryKey(mallShoppingCartItemId) > 0;
    }

    @Override
    public List<ShoppingCartItemVO> getMyShoppingCartItems(Long mallUserId) {
        List<ShoppingCartItemVO> mallShoppingCartItemVOS = new ArrayList<>();
        List<ShoppingCartItem> mallShoppingCartItems = mallShoppingCartItemMapper.selectByUserId(mallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(mallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> mallGoodsIds = mallShoppingCartItems.stream().map(ShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<Goods> mallGoods = mallGoodsMapper.selectByPrimaryKeys(mallGoodsIds);
            Map<Long, Goods> mallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(mallGoods)) {
                mallGoodsMap = mallGoods.stream().collect(Collectors.toMap(Goods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (ShoppingCartItem mallShoppingCartItem : mallShoppingCartItems) {
                ShoppingCartItemVO mallShoppingCartItemVO = new ShoppingCartItemVO();
                BeanUtil.copyProperties(mallShoppingCartItem, mallShoppingCartItemVO);
                if (mallGoodsMap.containsKey(mallShoppingCartItem.getGoodsId())) {
                    Goods mallGoodsTemp = mallGoodsMap.get(mallShoppingCartItem.getGoodsId());
                    mallShoppingCartItemVO.setGoodsCoverImg(mallGoodsTemp.getGoodsCoverImg());
                    String goodsName = mallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    mallShoppingCartItemVO.setGoodsName(goodsName);
                    mallShoppingCartItemVO.setSellingPrice(mallGoodsTemp.getSellingPrice());
                    mallShoppingCartItemVOS.add(mallShoppingCartItemVO);
                }
            }
        }
        return mallShoppingCartItemVOS;
    }

    @Override
    public List<ShoppingCartItem> findByUserId(Long mallUserId) {
        List<ShoppingCartItem> mallShoppingCartItems = mallShoppingCartItemMapper.selectByUserId(mallUserId, 100);
        return mallShoppingCartItems;
    }
}
