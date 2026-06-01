package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO){
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);

        shoppingCart.setUserId(BaseContext.getCurrentId());
       List<ShoppingCart>lists= shoppingCartMapper.list(shoppingCart);

       if(lists!=null && lists.size()>0){
           ShoppingCart care = lists.get(0);
           care.setNumber(care.getNumber()+1);
           shoppingCartMapper.updateNumber(care);
       }
       else {
          Long dishId=shoppingCart.getDishId();
           if(dishId != null){
               Dish dish = dishMapper.getById2(dishId);
               shoppingCart.setName(dish.getName());
               shoppingCart.setImage(dish.getImage());
               shoppingCart.setAmount(dish.getPrice());
           }
        else{
            Long setmealId = shoppingCartDTO.getSetmealId();
               Setmeal setmeal = setmealMapper.getById2(setmealId);
               shoppingCart.setName(setmeal.getName());
               shoppingCart.setImage(setmeal.getImage());
               shoppingCart.setAmount(setmeal.getPrice());
           }
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartMapper.insert(shoppingCart);
       }
    }

    /**
     * 查询购物车
     * @return
     */
    @Override
    public List<ShoppingCart>showlist(){
        Long id = BaseContext.getCurrentId();
        ShoppingCart shoppingcart = ShoppingCart.builder()
                .userId(id)
                .build();
        List<ShoppingCart>list=shoppingCartMapper.getById(shoppingcart);
        return list;
    }

    public void clean(){
        Long id = BaseContext.getCurrentId();
        ShoppingCart shoppingcart = ShoppingCart.builder()
                .userId(id)
                .build();
        shoppingCartMapper.deleteByUserId(shoppingcart);
    }

    public void sub(ShoppingCartDTO shoppingCartDTO){
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list != null && !list.isEmpty()){
            ShoppingCart cart = list.get(0);
            Integer number = cart.getNumber();
            if(number > 1){
                cart.setNumber(number - 1);
                shoppingCartMapper.updateNumber(cart);
            }else{
                shoppingCartMapper.deleteById(cart);
            }
        }
    }
}
