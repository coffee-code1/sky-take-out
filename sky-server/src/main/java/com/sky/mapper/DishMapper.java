package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    @Select("select count(*) from dish where status = #{status}")
    Integer countByStatus(@Param("status") Integer status);

    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer getById(@Param("categoryId") Long categoryId);

    @AutoFill(values = OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish getById2(@Param("id") Long id);

    /**
     * 根据套餐id查询菜品
     * @param id
     * @return
     */
    @Select("select d.* from dish d left join setmeal_dish sd on d.id = sd.dish_id where sd.setmeal_id = #{id}")
    List<Dish> getBySetmealId(@Param("id") Long id);

    List<DishVO> list(@Param("categoryId") Long categoryId);

    List<Dish> listByCondition(Dish dish);

    void delete(@Param("ids") List<Long> ids);

    @AutoFill(values = OperationType.UPDATE)
    void update(Dish dish);
}
