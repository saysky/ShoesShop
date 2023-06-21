package com.example.mall.controller.vo;

import com.example.mall.entity.GoodsCategory;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索页面分类数据VO
 */
public class SearchPageCategoryVO implements Serializable {

    private String firstLevelCategoryName;
    private Long firstLevelCategoryId;

    private List<GoodsCategory> secondLevelCategoryList;

    private String secondLevelCategoryName;
    private Long secondLevelCategoryId;

    private List<GoodsCategory> thirdLevelCategoryList;

    private String currentCategoryName;

    public String getFirstLevelCategoryName() {
        return firstLevelCategoryName;
    }

    public void setFirstLevelCategoryName(String firstLevelCategoryName) {
        this.firstLevelCategoryName = firstLevelCategoryName;
    }

    public List<GoodsCategory> getSecondLevelCategoryList() {
        return secondLevelCategoryList;
    }

    public void setSecondLevelCategoryList(List<GoodsCategory> secondLevelCategoryList) {
        this.secondLevelCategoryList = secondLevelCategoryList;
    }

    public String getSecondLevelCategoryName() {
        return secondLevelCategoryName;
    }

    public void setSecondLevelCategoryName(String secondLevelCategoryName) {
        this.secondLevelCategoryName = secondLevelCategoryName;
    }

    public List<GoodsCategory> getThirdLevelCategoryList() {
        return thirdLevelCategoryList;
    }

    public void setThirdLevelCategoryList(List<GoodsCategory> thirdLevelCategoryList) {
        this.thirdLevelCategoryList = thirdLevelCategoryList;
    }

    public String getCurrentCategoryName() {
        return currentCategoryName;
    }

    public void setCurrentCategoryName(String currentCategoryName) {
        this.currentCategoryName = currentCategoryName;
    }

    public Long getFirstLevelCategoryId() {
        return firstLevelCategoryId;
    }

    public void setFirstLevelCategoryId(Long firstLevelCategoryId) {
        this.firstLevelCategoryId = firstLevelCategoryId;
    }

    public Long getSecondLevelCategoryId() {
        return secondLevelCategoryId;
    }

    public void setSecondLevelCategoryId(Long secondLevelCategoryId) {
        this.secondLevelCategoryId = secondLevelCategoryId;
    }
}
