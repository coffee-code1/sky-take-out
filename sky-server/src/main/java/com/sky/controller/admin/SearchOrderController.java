package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "Admin order controller")
public class SearchOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("Order page query")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("admin query orders: {}", ordersPageQueryDTO);
        return Result.success(orderService.conditionSearch(ordersPageQueryDTO));
    }

    @GetMapping("/details/{id}")
    @ApiOperation("Order detail")
    public Result<OrderVO> details(@PathVariable Long id) {
        log.info("admin query order detail: {}", id);
        return Result.success(orderService.details(id));
    }

    @GetMapping("/statistics")
    @ApiOperation("Order statistics")
    public Result<OrderStatisticsVO> statistics() {
        return Result.success(orderService.statistics());
    }

    @PutMapping("/confirm")
    @ApiOperation("Confirm order")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("admin confirm order: {}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    @PutMapping("/rejection")
    @ApiOperation("Reject order")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("admin reject order: {}", ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("/cancel")
    @ApiOperation("Cancel order")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("admin cancel order: {}", ordersCancelDTO);
        orderService.adminCancel(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("Start delivery")
    public Result delivery(@PathVariable Long id) {
        log.info("admin delivery order: {}", id);
        orderService.delivery(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("Complete order")
    public Result complete(@PathVariable Long id) {
        log.info("admin complete order: {}", id);
        orderService.complete(id);
        return Result.success();
    }
}
