package com.mislab.core.systemcore.controller;


import com.mislab.common.result.R;
import com.mislab.core.systemcore.service.EmployeeEnterpriseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author krian
 * @since 2022-10-09
 */
@Api(tags = "企业与员工的关联信息")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/core/employeeEnterprise")
public class EmployeeEnterpriseController {

    @Autowired
    private EmployeeEnterpriseService employeeEnterpriseService;

    @ApiOperation("统计员工已/待完成的项目数量")
    @GetMapping("/getProjectNumber/{industryId}/{uid}")
    public R getProjectNumber(@ApiParam("行业id") @PathVariable(value = "industryId") Integer industryId,
                              @ApiParam("员工编号") @PathVariable(value = "uid") String uid){
        return employeeEnterpriseService.getProjectNumber(industryId,uid);
    }

}

