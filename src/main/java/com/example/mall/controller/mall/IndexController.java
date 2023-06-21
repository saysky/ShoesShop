package com.example.mall.controller.mall;

import com.example.mall.entity.GoodsCategory;
import com.example.mall.service.CarouselService;
import com.example.mall.service.CategoryService;
import com.example.mall.service.IndexConfigService;
import com.example.mall.common.Constants;
import com.example.mall.common.IndexConfigTypeEnum;
import com.example.mall.controller.vo.IndexCarouselVO;
import com.example.mall.controller.vo.IndexCategoryVO;
import com.example.mall.controller.vo.IndexConfigGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private CarouselService mallCarouselService;

    @Resource
    private IndexConfigService mallIndexConfigService;

    @Resource
    private CategoryService mallCategoryService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<IndexCategoryVO> categories = mallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }
        List<IndexCarouselVO> carousels = mallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<IndexConfigGoodsVO> hotGoodses = mallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<IndexConfigGoodsVO> newGoodses = mallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<IndexConfigGoodsVO> recommendGoodses = mallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
        return "mall/index";
    }


    @Autowired
    CategoryService categoryService;
    @GetMapping("/demo")
    @ResponseBody
    public String test() {
        List<GoodsCategory> allLevel1 = categoryService.findAllLevel1();

        String[] arr = {"春秋鞋", "冬鞋", "夏鞋"};

        for(GoodsCategory category : allLevel1) {
          for(String str : arr) {
              GoodsCategory goodsCategory = new GoodsCategory();
              goodsCategory.setCategoryName(str);
              goodsCategory.setParentId(category.getCategoryId());
              mallCategoryService.saveCategory(goodsCategory);



          }
        }
        System.out.println(allLevel1);
        return "success";
    }

}
