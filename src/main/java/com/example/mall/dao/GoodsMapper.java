package com.example.mall.dao;

import com.example.mall.entity.Goods;
import com.example.mall.entity.StockNumDTO;
import com.example.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKeyWithBLOBs(Goods record);

    int updateByPrimaryKey(Goods record);

    List<Goods> findmallGoodsList(PageQueryUtil pageUtil);

    int getTotalmallGoods(PageQueryUtil pageUtil);

    List<Goods> selectByPrimaryKeys(List<Long> goodsIds);

    List<Goods> findmallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalmallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("mallGoodsList") List<Goods> mallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);



    /**
     * 根据关键词查询商品
     *
     * @param keyword
     * @return
     */
    List<String> getSearchHelper(String keyword);
