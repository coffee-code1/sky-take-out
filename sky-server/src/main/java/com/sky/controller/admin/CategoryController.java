package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/admin/category")
@RestController
@ApiModel("分类接口")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 新添加分类
     * @param categoryDTO
     * @return
     */
    @ApiOperation("新增分类")
    @PostMapping
    public Result addCatagory(@RequestBody CategoryDTO categoryDTO){
        log.info("新增加的种类为:{}",categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    @ApiOperation("分页查询")
    @GetMapping("page")
    public Result<PageResult> pageQuery( CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询：{}",categoryPageQueryDTO);
        PageResult pages=categoryService.page(categoryPageQueryDTO);
        return Result.success(pages);
    }
}
