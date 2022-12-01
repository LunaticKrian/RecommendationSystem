package com.mislab.core.systemcore.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mislab.common.exception.Assert;
import com.mislab.common.result.R;
import com.mislab.common.result.ResponseEnum;
import com.mislab.common.utils.RegexValidateUtils;
import com.mislab.core.systemcore.pojo.entity.Employee;
import com.mislab.core.systemcore.pojo.vo.EmployeeMsgVo;
import com.mislab.core.systemcore.pojo.vo.EmployeeVo;
import com.mislab.core.systemcore.pojo.vo.LoginVo;
import com.mislab.core.systemcore.pojo.vo.RegisterVo;
import com.mislab.core.systemcore.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */

@Api(tags = "员工接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/core/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @ApiOperation("注册员工信息")
    @PostMapping("/register")
    public R save(@RequestBody RegisterVo registerVo){
        String uid = registerVo.getUid();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();

        Assert.notEmpty(uid, ResponseEnum.EMPLOYEE_NULL_ERROR);
        Assert.notEmpty(password, ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notEmpty(code, ResponseEnum.CODE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);

        employeeService.register(registerVo);
        return R.SUCCESS().message("注册成功，等待管理员审批！");
    }

    // TODO:批量导入员工信息（管理员端操作）

    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public R login(@RequestBody LoginVo loginVo, HttpServletRequest request) {
        String uid = loginVo.getUid();
        String password = loginVo.getPassword();
        Assert.notEmpty(uid, ResponseEnum.EMPLOYEE_NULL_ERROR);
        Assert.notEmpty(password, ResponseEnum.PASSWORD_NULL_ERROR);

        String ip = request.getRemoteAddr();
        EmployeeVo employeeVo = employeeService.login(loginVo, ip);

        return R.SUCCESS().data("employeeInfo", employeeVo);
    }

    @ApiOperation("获取用户基本信息")
    @GetMapping("getEmployeeMsg")
    public R getEmployeeMsg(@ApiParam("员工编号") String uid){
        Employee employee = employeeService.getOne(new LambdaQueryWrapper<Employee>()
                .eq(Employee::getUid, uid));
        //封装页面展示用户信息
        EmployeeMsgVo employeeMsgVo = new EmployeeMsgVo();
        BeanUtils.copyProperties(employee,employeeMsgVo);
        return R.SUCCESS().data("employeeMsg",employeeMsgVo);
    }
}

