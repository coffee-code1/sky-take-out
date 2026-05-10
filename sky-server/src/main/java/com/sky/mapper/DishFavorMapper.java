package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFavorMapper {
    /**
     * 批量插入菜品口味
     * @param list
     */
    void insertBatch(List<DishFlavor>list);

    /**
     * 根据菜品id查询菜品口味
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor>getById(Long id);

    /**
     * 根据id删除口味
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id = #{id}")
    void deleteBydishId(Long id);
}
