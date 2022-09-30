package com.mislab.core.systemcore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mislab.common.exception.Assert;
import com.mislab.common.result.R;
import com.mislab.common.result.ResponseEnum;
import com.mislab.common.utils.JwtUtils;
import com.mislab.common.utils.MD5;
import com.mislab.core.systemcore.mapper.EmployeeLoginRecordMapper;
import com.mislab.core.systemcore.pojo.entity.Employee;
import com.mislab.core.systemcore.mapper.EmployeeMapper;
import com.mislab.core.systemcore.pojo.entity.EmployeeLoginRecord;
import com.mislab.core.systemcore.pojo.vo.EmployeeVo;
import com.mislab.core.systemcore.pojo.vo.LoginVo;
import com.mislab.core.systemcore.pojo.vo.RegisterVo;
import com.mislab.core.systemcore.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeLoginRecordMapper employeeLoginRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EmployeeVo login(LoginVo loginVo, String ip) {
        String uid = loginVo.getUid();
        String password = loginVo.getPassword();

        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        Employee employeeInfo = baseMapper.selectOne(queryWrapper);

        Assert.notNull(employeeInfo, ResponseEnum.LOGIN_EMPLOYEE_ERROR);

        Assert.equals(MD5.encrypt(password), employeeInfo.getPassword(), ResponseEnum.LOGIN_PASSWORD_ERROR);
        Assert.equals(employeeInfo.getStatus(), Employee.STATUS_NORMAL, ResponseEnum.LOGIN_LOKED_ERROR);

        EmployeeLoginRecord employeeLoginRecord = new EmployeeLoginRecord();
        employeeLoginRecord.setUid(loginVo.getUid());
        employeeLoginRecord.setIp(ip);
        employeeLoginRecordMapper.insert(employeeLoginRecord);

        EmployeeVo employeeVo = new EmployeeVo();
        String token = JwtUtils.createToken(employeeInfo.getUid(), employeeInfo.getName());
        employeeVo.setToken(token);
        employeeVo.setHeadImg(employeeInfo.getHeadImg());
        employeeVo.setMobile(employeeInfo.getMobile());
        employeeVo.setName(employeeInfo.getName());
        employeeVo.setUid(employeeInfo.getUid());

        return employeeVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterVo registerVo) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", registerVo.getUid());
        Integer count = baseMapper.selectCount(queryWrapper);
        Assert.isTrue(count == 0, ResponseEnum.MOBILE_EXIST_ERROR);

        Employee employee = new Employee();
        employee.setUid(registerVo.getUid());
        employee.setName(registerVo.getUid());
        employee.setMobile(registerVo.getMobile());
        employee.setPassword(MD5.encrypt(registerVo.getPassword()));
        employee.setStatus(Employee.STATUS_LOCKED);
        employee.setHeadImg(Employee.USER_AVATAR);

        baseMapper.insert(employee);
    }
}
