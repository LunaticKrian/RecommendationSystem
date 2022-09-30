package com.mislab.core.systemcore.service;

import com.mislab.core.systemcore.pojo.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mislab.core.systemcore.pojo.vo.EmployeeVo;
import com.mislab.core.systemcore.pojo.vo.LoginVo;
import com.mislab.core.systemcore.pojo.vo.RegisterVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
public interface EmployeeService extends IService<Employee> {

    EmployeeVo login(LoginVo loginVo, String ip);

    void register(RegisterVo registerVo);

}
