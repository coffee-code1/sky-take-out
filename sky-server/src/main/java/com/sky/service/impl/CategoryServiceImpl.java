package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    DishMapper dishMapper;

    @Autowired
    SetmealMapper setmealMapper;

    /**
     * 新增分类
     * @param categoryDTO
     */
    public void addCategory(CategoryDTO categoryDTO){
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());
        category.setStatus(0);
        categoryMapper.add(category);
    }

    /**
     * 根据id查询
     * @param id
     */
    public void getById(Long id){
        categoryMapper.select(id);
    }

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO){
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        if(categoryPageQueryDTO.getType()==null) {
            Page<Category> pages = categoryMapper.pageSelect(categoryPageQueryDTO);
            return new PageResult(pages.getTotal(), pages.getResult());
        }
        Page<Category> pagess=categoryMapper.listfind(categoryPageQueryDTO.getType());
        return new PageResult(pagess.getTotal(),pagess.getResult());
    }

    /**
     * 修改套餐状态
     * @param status
     * @param id
     */
    public void startandstop(int status,Long id){
        Category category=Category.builder()
                        .status(status)
                        .id(id)
                        .updateTime(LocalDateTime.now())
                        .updateUser(BaseContext.getCurrentId())
                        .build();
        categoryMapper.update(category);
    }

    /**
     * 套餐信息编辑
     * @param categoryDTO
     */
    public void change(CategoryDTO categoryDTO){
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);
    }

    public void deleteCategory(Long id){
        categoryMapper.select(id);
        Integer count=dishMapper.getById(id);
        if(count>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        count= setmealMapper.getById(id);
        if(count>0){
            throw new DeletionNotAllowedException((MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL));
        }

        categoryMapper.delete(id);
    }

    public List<Category> list(Long type){
       List<Category> lists = categoryMapper.listfind(type);
       if (lists == null || lists.isEmpty() || type == null) {
           return lists;
       }

       List<Category> filteredList = new ArrayList<>();
       for (Category category : lists) {
           if (category == null || category.getId() == null) {
               continue;
           }
           if (type == 1 && dishMapper.getById(category.getId()) > 0) {
               filteredList.add(category);
           }
           if (type == 2 && setmealMapper.getById(category.getId()) > 0) {
               filteredList.add(category);
           }
       }
       return filteredList;
    }
}
