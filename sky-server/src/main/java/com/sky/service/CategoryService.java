package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 新增分类
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 根据id查询
     * @param id
     */
    void getById(Long id);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 套餐的起售状态
     * @param id
     */
    void startandstop(int status,Long id);

    /**
     * 修改信息
     * @param categoryDTO
     */
    void change(CategoryDTO categoryDTO);

    /**
     * 删除套餐
     * @param id
     */
    void deleteCategory(Long id);

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    List<Category>list(Long type);
}
