package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/admin/category")
@RestController
@ApiModel("分类接口")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @ApiOperation("新增分类")
    @PostMapping
    @CacheEvict(cacheNames = {"userCategoryCache", "userDishCache", "userSetmealCache"}, allEntries = true)
    public Result addCatagory(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增的分类信息: {}", categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询: {}", categoryPageQueryDTO);
        PageResult pages = categoryService.page(categoryPageQueryDTO);
        return Result.success(pages);
    }

    @ApiOperation("分类状态修改")
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = {"userCategoryCache", "userDishCache", "userSetmealCache"}, allEntries = true)
    public Result startAndStop(@PathVariable int status, Long id) {
        log.info("修改分类状态 status: {}, id: {}", status, id);
        categoryService.startandstop(status, id);
        return Result.success();
    }

    @ApiOperation("修改分类")
    @PutMapping
    @CacheEvict(cacheNames = {"userCategoryCache", "userDishCache", "userSetmealCache"}, allEntries = true)
    public Result changeSetmeal(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类信息: {}", categoryDTO);
        categoryService.change(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除分类")
    @CacheEvict(cacheNames = {"userCategoryCache", "userDishCache", "userSetmealCache"}, allEntries = true)
    public Result deleteCategory(Long id) {
        log.info("删除分类 id: {}", id);
        categoryService.deleteCategory(id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据类型查询")
    public Result<List<Category>> find(Long type) {
        log.info("查询分类 type: {}", type);
        List<Category> lists = categoryService.list(type);
        return Result.success(lists);
    }
}
