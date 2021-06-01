package com.lin.missyou.api.v1;

import com.lin.missyou.exception.http.NotFoundEcxeption;
import com.lin.missyou.model.Category;
import com.lin.missyou.model.GridCategory;
import com.lin.missyou.service.CategoryService;
import com.lin.missyou.service.GridCategoryService;
import com.lin.missyou.vo.CategoriesAllVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class categoryController {
  
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GridCategoryService gridCategoryService;

    @GetMapping("/all")
    public CategoriesAllVo getAll(){
        Map<Integer, List<Category>> all = categoryService.getAll();
        CategoriesAllVo categoriesAllVo = new CategoriesAllVo(all);
        return  categoriesAllVo;
    }

    @GetMapping("/grid/all")
    public List<GridCategory>  getGridCategoryList(){
        List<GridCategory> gridCategoryList = gridCategoryService.getGridCategoryList();
        if (gridCategoryList.isEmpty()){
            throw new NotFoundEcxeption(30009);
        }
        return gridCategoryList;
    }



}
