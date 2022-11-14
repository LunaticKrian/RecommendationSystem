package com.mislab.core.systemcore.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mislab.common.exception.Assert;
import com.mislab.common.result.R;
import com.mislab.common.result.ResponseEnum;
import com.mislab.core.systemcore.common.enums.EnterpriseStateEnum;
import com.mislab.core.systemcore.pojo.dto.EnterpriseBasicMsgDto;
import com.mislab.core.systemcore.pojo.dto.EnterpriseForSearchDto;
import com.mislab.core.systemcore.pojo.dto.EnterpriseOperationalMsgDto;
import com.mislab.core.systemcore.pojo.entity.*;
import com.mislab.core.systemcore.pojo.vo.EnterpriseProjectVo;
import com.mislab.core.systemcore.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
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

    @Autowired
    private EnterpriseBusinessService enterpriseBusinessService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private IndustryService industryService;

    @ApiOperation("保存/修改企业基本信息")
    @PostMapping("/saveEnterpriseMsg")
    public R saveEnterpriseMsg(@RequestBody EnterpriseBasicMsgDto enterpriseBasicMsgDto) {
        String enterpriseKey = enterpriseBasicMsgDto.getEnterpriseKey();
        if (StringUtils.isEmpty(enterpriseKey)) {
            //新增企业基本信息
            return enterpriseService.saveEnterpriseMsg(enterpriseBasicMsgDto);
        } else {
            //修改企业基本信息
            return enterpriseService.updateEnterpriseMsg(enterpriseBasicMsgDto);
        }
    }

    @ApiOperation("获取第一页面的企业基本信息")
    @GetMapping("/getEnterpriseMsgOfFirst")
    public R getEnterpriseMsgOfFirst(@ApiParam("企业唯一标识码") String enterpriseKey, @ApiParam("员工编号") String uid) {
        //获取企业与员工的关联关系
        EmployeeEnterprise employeeEnterprise = employeeEnterpriseService.getOne(new LambdaQueryWrapper<EmployeeEnterprise>()
                .eq(EmployeeEnterprise::getEnterpriseKey, enterpriseKey)
                .eq(EmployeeEnterprise::getUid, uid));
        //如果没有关系，返回异常
        Assert.notNull(enterpriseKey, ResponseEnum.ENTERPRISE_NOMATCH_EMPLOYEE);
        //如果存在关系，但该企业信息已经不存在（被逻辑删除）
        Assert.notEquals(employeeEnterprise.getState(),EnterpriseStateEnum.ALREADY_DELETE,ResponseEnum.ENTERPRISE_NOTFOUND);

        return enterpriseService.getEnterpriseMsgOfFirst(enterpriseKey);
    }

    @ApiOperation("保存/修改企业经营情况的信息")
    @PostMapping("/saveEnterpriseOperationalMsg")
    public R saveEnterpriseOperationalMsg(@RequestBody EnterpriseOperationalMsgDto enterpriseOperationalMsgDto) {
        log.info("企业唯一标识为"+enterpriseOperationalMsgDto.getEnterpriseKey());
        Enterprise enterprise = enterpriseService.getOne(new LambdaQueryWrapper<Enterprise>()
                .eq(Enterprise::getEnterpriseKey, enterpriseOperationalMsgDto.getEnterpriseKey()));
        //如果enterpriseKey为null，返回企业信息未找到异常
        Assert.notNull(enterprise.getEnterpriseKey(), ResponseEnum.ENTERPRISE_NOTFOUND);
        return enterpriseService.saveEnterpriseOperationalMsg(enterpriseOperationalMsgDto);
    }

    @ApiOperation("获取第二页面的企业经营情况信息")
    @GetMapping("/getEnterpriseMsgOfSecond")
    public R getEnterpriseMsgOfSecond(@ApiParam("企业唯一标识码") String enterpriseKey, @ApiParam("员工编号") String uid) {

        EmployeeEnterprise employeeEnterprise = employeeEnterpriseService.getOne(new LambdaQueryWrapper<EmployeeEnterprise>()
                .eq(EmployeeEnterprise::getEnterpriseKey, enterpriseKey)
                .eq(EmployeeEnterprise::getUid, uid));
        Assert.notNull(enterpriseKey, ResponseEnum.ENTERPRISE_NOMATCH_EMPLOYEE);
        Assert.notEquals(employeeEnterprise.getState(), EnterpriseStateEnum.ALREADY_DELETE,ResponseEnum.ENTERPRISE_NOTFOUND);

        return enterpriseService.getEnterpriseMsgOfSecond(enterpriseKey);
    }

    @ApiOperation("获取员工管理的不同状态的企业")
    @GetMapping("/getEnterpriseListByState")
    public R getEnterpriseListByState(@ApiParam("行业id") Integer industryId,
                                      @ApiParam("员工编号") String uid,
                                      @RequestParam(value = "state",required = false) @ApiParam("状态码") Integer state){
        return enterpriseService.getEnterpriseList(industryId,uid,state);
    }


    @ApiOperation("获取企业对应的业务名称")
    @GetMapping("getBusinessByEnterpriseKey")
    public R getBusinessByEnterpriseKey(@ApiParam("企业唯一标识码") String enterpriseKey){
        Assert.notEmpty(enterpriseKey,ResponseEnum.ENTERPRISEKEY_ISEMPTY);
        Enterprise enterprise = enterpriseService.getOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getEnterpriseKey, enterpriseKey));
        Assert.notNull(enterprise,ResponseEnum.ENTERPRISE_NOTFOUND);

        List<Integer> businessIds = enterpriseBusinessService.list(new LambdaQueryWrapper<EnterpriseBusiness>().eq(EnterpriseBusiness::getEnterpriseKey, enterpriseKey))
                .stream().map(EnterpriseBusiness::getBusinessId)
                .collect(Collectors.toList());

        List<String> res = businessService.listByIds(businessIds)
                .stream().map(Business::getName)
                .collect(Collectors.toList());

        return R.SUCCESS().data("businessList",res);
    }

    @ApiOperation("通过相关查询条件查询企业信息")
    @PostMapping("searchForEnterpriseList")
    public R searchForEnterpriseList(@RequestBody EnterpriseForSearchDto enterpriseForSearchDto){
        return enterpriseService.searchForEnterpriseList(enterpriseForSearchDto);
    }

    @ApiOperation("删除企业标识符对应的企业")
    @DeleteMapping("deleteByEnterpriseKey/{uid}/{enterpriseKey}")
    public R deleteByEnterpriseKey(@ApiParam("员工id") @PathVariable("uid") String uid,
                                   @ApiParam("企业唯一标识符") @PathVariable("enterpriseKey") String enterpriseKey){
        LambdaQueryWrapper<EmployeeEnterprise> queryWrapper = new LambdaQueryWrapper<EmployeeEnterprise>()
                .eq(EmployeeEnterprise::getEnterpriseKey, enterpriseKey)
                .eq(EmployeeEnterprise::getUid, uid);
        //获取企业与员工的关联关系
        EmployeeEnterprise employeeEnterprise = employeeEnterpriseService.getOne(queryWrapper);
        //如果没有关系，没有权限操作，返回异常
        Assert.notNull(enterpriseKey, ResponseEnum.ENTERPRISE_NOMATCH_EMPLOYEE);
        //执行删除逻辑
        employeeEnterprise.setState(EnterpriseStateEnum.ALREADY_DELETE.getCode());
        employeeEnterpriseService.update(employeeEnterprise,queryWrapper);

        return R.SUCCESS().message("删除成功");
    }
}



