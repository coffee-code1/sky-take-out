package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = getDateRange(begin, end);
        List<Double> turnoverList = new ArrayList<>();

        for (LocalDate date : dates) {
            Map<String, Object> map = buildDateStatusMap(date, Orders.COMPLETED);
            Double turnover = orderMapper.getSum(map);
            turnoverList.add(turnover == null ? 0.0 : turnover);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dates, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = getDateRange(begin, end);
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate date : dates) {
            Map<String, Object> totalMap = new HashMap<>();
            totalMap.put("end", LocalDateTime.of(date, LocalTime.MAX));
            Integer totalUserCount = userMapper.countByMap(totalMap);
            totalUserList.add(totalUserCount == null ? 0 : totalUserCount);

            Map<String, Object> newUserMap = new HashMap<>();
            newUserMap.put("begin", LocalDateTime.of(date, LocalTime.MIN));
            newUserMap.put("end", LocalDateTime.of(date, LocalTime.MAX));
            Integer newUserCount = userMapper.countByMap(newUserMap);
            newUserList.add(newUserCount == null ? 0 : newUserCount);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dates, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = getDateRange(begin, end);
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        int totalOrderCount = 0;
        int validOrderCount = 0;

        for (LocalDate date : dates) {
            Map<String, Object> totalMap = buildDateStatusMap(date, null);
            Integer orderCount = orderMapper.countByMap(totalMap);
            int dayOrderCount = orderCount == null ? 0 : orderCount;
            orderCountList.add(dayOrderCount);
            totalOrderCount += dayOrderCount;

            Map<String, Object> validMap = buildDateStatusMap(date, Orders.COMPLETED);
            Integer validCount = orderMapper.countByMap(validMap);
            int dayValidCount = validCount == null ? 0 : validCount;
            validOrderCountList.add(dayValidCount);
            validOrderCount += dayValidCount;
        }

        double orderCompletionRate = totalOrderCount == 0 ? 0.0 : (double) validOrderCount / totalOrderCount;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dates, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", LocalDateTime.of(begin, LocalTime.MIN));
        map.put("end", LocalDateTime.of(end, LocalTime.MAX));
        map.put("status", Orders.COMPLETED);

        List<GoodsSalesDTO> salesTop10 = orderDetailMapper.getSalesTop10(map);
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();

        if (salesTop10 != null) {
            for (GoodsSalesDTO goodsSalesDTO : salesTop10) {
                nameList.add(goodsSalesDTO.getName());
                numberList.add(goodsSalesDTO.getNumber());
            }
        }

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }

    private List<LocalDate> getDateRange(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate current = begin;
        while (!current.isAfter(end)) {
            dates.add(current);
            current = current.plusDays(1);
        }
        return dates;
    }

    private Map<String, Object> buildDateStatusMap(LocalDate date, Integer status) {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", LocalDateTime.of(date, LocalTime.MIN));
        map.put("end", LocalDateTime.of(date, LocalTime.MAX));
        map.put("status", status);
        return map;
    }
}
