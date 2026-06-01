package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);

    @Select("select * from orders where number = #{orderNumber} and user_id = #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    void update(Orders orders);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id} and user_id = #{userId}")
    Orders getByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Select("select count(*) from orders where status = #{status}")
    Integer countByStatus(Integer status);

    @Select("select * from orders where status = #{status} and order_time < #{localDateTime}")
    List<Orders> getbystatusandordertime(Integer status, LocalDateTime localDateTime);
}
