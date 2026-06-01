package com.sky.Task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    OrderMapper orderMapper ;

    @Scheduled(cron = "0 * * * * ?")
    public void processTimeout(){
        log.info("处理订单超时{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders>list= orderMapper.getbystatusandordertime(Orders.PENDING_PAYMENT,time);

        if(list!=null&&list.size()>0){
            for(Orders orders : list){
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryout(){
        log.info("处理配送超时{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders>list= orderMapper.getbystatusandordertime(Orders.DELIVERY_IN_PROGRESS,time);

        if(list!=null&&list.size()>0){
            for(Orders orders : list){
                orders.setStatus(Orders.COMPLETED);
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }
}
