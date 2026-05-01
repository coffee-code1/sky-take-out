package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@ApiModel("员工的接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation("员工登录的接口")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("员工推出的接口")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增员工的接口")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("添加员工{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success("添加成功");
    }

    @GetMapping("/page")
    @ApiOperation("员工查询接口")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("查询员工,参数{}",employeePageQueryDTO);
        PageResult pageResult=employeeService.page(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("status/{status}")
    @ApiOperation("员工状态修改")
    public Result StartandStop(@PathVariable Integer status,Long id){
        log.info("修改状态的员工id为：{}",id);
        employeeService.StartandStop(status,id);
        return Result.success();
    }

    @GetMapping("{id}")
    @ApiOperation("根据员工id查询")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("查询的员工id为:{}",id);
        return Result.success(employeeService.getById(id));
    }

    @PutMapping
    @ApiOperation("修改员工的信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("修改员工的信息为：{}",employeeDTO);
        Employee emp=new Employee();
        BeanUtils.copyProperties(employeeDTO,emp);
        employeeService.update(emp);
        return Result.success();
    }
}
