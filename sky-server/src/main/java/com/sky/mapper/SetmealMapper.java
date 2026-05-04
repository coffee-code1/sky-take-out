package com.sky.mapper;

import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    @Select("select  count(id) from setmeal where category_id = #{categoryId}")
    public Integer getById(Long id);
}
