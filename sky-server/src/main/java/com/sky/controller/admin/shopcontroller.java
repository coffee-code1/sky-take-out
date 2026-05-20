package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("ShopCOntroller_Shop")
@RequestMapping("/user/shop")
@Slf4j
public class shopcontroller {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 设置店铺的状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置店铺的状态为{}：",status);
        redisTemplate.opsForValue().set("SHOP_STATUS",status);
        return Result.success();
    }

    @GetMapping("/status")
    public Result<Integer>getStatus(){
        Integer status=(Integer)redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("店铺状态为：{}",status==1?"营业中":"打样中");
        return Result.success(status);
    }
}
