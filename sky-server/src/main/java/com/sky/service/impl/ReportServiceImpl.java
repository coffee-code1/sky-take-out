package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @Override
    public void exportdate(HttpServletResponse httpServletResponse) {
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        BusinessDataVO businessData = getBusinessData(begin, end);

//        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        String fileName = URLEncoder.encode("运营数据报表.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
 //       httpServletResponse.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + fileName);

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("Template/运营数据报表模板.xlsx");
        if (inputStream == null) {
            throw new IllegalStateException("Excel模板不存在: Template/运营数据报表模板.xlsx");
        }

        try (InputStream templateStream = inputStream;
             XSSFWorkbook workbook = new XSSFWorkbook(templateStream);
             ServletOutputStream outputStream = httpServletResponse.getOutputStream()) {
            Sheet sheet = workbook.getSheetAt(0);
            sheet.getRow(1).getCell(1).setCellValue("时间：" + begin + " 至 " + end);
            sheet.getRow(3).getCell(2).setCellValue(businessData.getTurnover());
            sheet.getRow(3).getCell(4).setCellValue(businessData.getOrderCompletionRate());
            sheet.getRow(3).getCell(6).setCellValue(businessData.getNewUsers());
            sheet.getRow(4).getCell(2).setCellValue(businessData.getValidOrderCount());
            sheet.getRow(4).getCell(4).setCellValue(businessData.getUnitPrice());

            List<LocalDate> dates = getDateRange(begin, end);
            for (int i = 0; i < dates.size(); i++) {
                LocalDate date = dates.get(i);
                BusinessDataVO dailyBusinessData = getBusinessData(date, date);
                int rowIndex = 7 + i;

                sheet.getRow(rowIndex).getCell(1).setCellValue(date.toString());
                sheet.getRow(rowIndex).getCell(2).setCellValue(dailyBusinessData.getTurnover());
                sheet.getRow(rowIndex).getCell(3).setCellValue(dailyBusinessData.getValidOrderCount());
                sheet.getRow(rowIndex).getCell(4).setCellValue(dailyBusinessData.getOrderCompletionRate());
                sheet.getRow(rowIndex).getCell(5).setCellValue(dailyBusinessData.getUnitPrice());
                sheet.getRow(rowIndex).getCell(6).setCellValue(dailyBusinessData.getNewUsers());
            }

            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("导出运营数据报表失败", e);
        }
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

    private BusinessDataVO getBusinessData(LocalDate begin, LocalDate end) {
        Map<String, Object> completedOrderMap = new HashMap<>();
        completedOrderMap.put("begin", LocalDateTime.of(begin, LocalTime.MIN));
        completedOrderMap.put("end", LocalDateTime.of(end, LocalTime.MAX));
        completedOrderMap.put("status", Orders.COMPLETED);

        Map<String, Object> allOrderMap = new HashMap<>();
        allOrderMap.put("begin", LocalDateTime.of(begin, LocalTime.MIN));
        allOrderMap.put("end", LocalDateTime.of(end, LocalTime.MAX));

        Map<String, Object> newUserMap = new HashMap<>();
        newUserMap.put("begin", LocalDateTime.of(begin, LocalTime.MIN));
        newUserMap.put("end", LocalDateTime.of(end, LocalTime.MAX));

        Double turnover = orderMapper.getSum(completedOrderMap);
        Integer validOrderCount = orderMapper.countByMap(completedOrderMap);
        Integer totalOrderCount = orderMapper.countByMap(allOrderMap);
        Integer newUsers = userMapper.countByMap(newUserMap);

        double totalTurnover = turnover == null ? 0.0 : turnover;
        int totalValidOrderCount = validOrderCount == null ? 0 : validOrderCount;
        int totalOrder = totalOrderCount == null ? 0 : totalOrderCount;
        int totalNewUsers = newUsers == null ? 0 : newUsers;
        double orderCompletionRate = totalOrder == 0 ? 0.0 : (double) totalValidOrderCount / totalOrder;
        double unitPrice = totalValidOrderCount == 0 ? 0.0 : totalTurnover / totalValidOrderCount;

        return BusinessDataVO.builder()
                .turnover(totalTurnover)
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(totalNewUsers)
                .build();
    }
}
