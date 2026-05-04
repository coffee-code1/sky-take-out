package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 新增分类
     * @param category
     */
    @AutoFill(values = OperationType.INSERT)
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

    /**
     * 更新分类信息
     * @param category
     */
    @AutoFill(values = OperationType.UPDATE)
    void update(Category category);

    /**
     * 根据id删除套餐
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void delete(Long id);

    /**
     * 根据type查询
     * @param type
     * @return
     */
    Page<Category>listfind(Long type);
}
