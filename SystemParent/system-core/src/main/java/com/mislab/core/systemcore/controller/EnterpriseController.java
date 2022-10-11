package com.mislab.core.systemcore.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mislab.common.result.R;
import com.mislab.core.systemcore.pojo.dto.EnterpriseBasicMsgDto;
import com.mislab.core.systemcore.pojo.entity.EmployeeEnterprise;
import com.mislab.core.systemcore.service.EmployeeEnterpriseService;
import com.mislab.core.systemcore.service.EnterpriseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
@Api(tags = "企业基本信息接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/core/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private EmployeeEnterpriseService employeeEnterpriseService;

    @ApiOperation("保存/修改企业基本信息")
    @PostMapping("/saveEnterpriseMsg")
    public R saveEnterpriseMsg(@RequestBody EnterpriseBasicMsgDto enterpriseBasicMsgDto){
        String enterpriseKey = enterpriseBasicMsgDto.getEnterpriseKey();
        if(StringUtils.isEmpty(enterpriseKey)){
            //新增企业基本信息
            return enterpriseService.saveEnterpriseMsg(enterpriseBasicMsgDto);
        }else{
            //修改企业基本信息
            return enterpriseService.updateEnterpriseMsg(enterpriseBasicMsgDto);
        }
    }

    @ApiOperation("获取企业基本信息")
    @GetMapping("/getEnterpriseMsgOfFirst")
    public R getEnterpriseMsgOfFirst(@ApiParam("企业唯一标识码") String enterpriseKey,@ApiParam("员工编号") String uid){
        //获取企业与员工的关联关系
        EmployeeEnterprise employeeEnterprise = employeeEnterpriseService.getOne(new LambdaQueryWrapper<EmployeeEnterprise>()
                .eq(EmployeeEnterprise::getEnterpriseKey, enterpriseKey)
                .eq(EmployeeEnterprise::getUid, uid));
        //如果没有关系，返回异常
        if(employeeEnterprise == null){
            return R.ERROR().message("该企业与员工没有对应关系");
        }else {
            Integer state = employeeEnterprise.getState();
            if (state == 0) return R.ERROR().message("该企业信息已经不存在");
        }
        return R.SUCCESS();
    }
}

