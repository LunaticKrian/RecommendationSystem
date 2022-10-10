package com.mislab.core.systemcore.controller;


import com.mislab.common.result.R;
import com.mislab.core.systemcore.pojo.dto.EnterpriseBasicMsgDto;
import com.mislab.core.systemcore.service.EnterpriseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
}

