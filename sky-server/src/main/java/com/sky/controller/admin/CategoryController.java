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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/page")
    public Result<PageResult> pageQuery( CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询：{}",categoryPageQueryDTO);
        PageResult pages=categoryService.page(categoryPageQueryDTO);
        return Result.success(pages);
    }

    @ApiOperation("套餐状态改变")
    @PostMapping("/status/{status}")
    public Result StartandStop(@PathVariable int status,Long id){
        log.info("修改状态为：{},修改人为:{}",status,id);
        categoryService.startandstop(status,id);
        return Result.success();
    }

    @ApiOperation("修改分类套餐")
    @PutMapping
    public Result changeSetmeal(@RequestBody CategoryDTO categoryDTO){
        log.info("修改的信息：{}",categoryDTO);
        categoryService.change(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除套餐")
    public Result deleteCategory(Long id){
        log.info("删除的套餐id为：{}",id);
        categoryService.deleteCategory(id);
        return Result.success();
    }

    /**
     * 根据菜品分类查询所有的分类名字，回显给前端，显示在下拉列表里
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询")
    public Result<List<Category>> find(Long type){
        log.info("查询的type为：{}",type);
        List<Category>lists= categoryService.list(type);
        return Result.success(lists);
    }
}
