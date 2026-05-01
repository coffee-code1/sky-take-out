package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper {
    /**
     * 新增分类
     * @param category
     */
    void add(Category category);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from category where id = #{id}")
    Category select(Long id);

    /**
     * 分页展示
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category>pageSelect(CategoryPageQueryDTO categoryPageQueryDTO);
}
