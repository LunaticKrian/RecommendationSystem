package com.mislab.core.systemcore.controller;


import com.mislab.common.result.R;
import com.mislab.core.systemcore.service.IndustryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
@Api(tags = "行业相关接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/core/industry")
public class IndustryController {

    @Autowired
    private IndustryService industryService;

    @ApiOperation("获取行业对应的业务")
    @GetMapping("/getBusinessByIndustry")
    public R getBusinessByIndustry(@ApiParam("企业名称") String IndustryName){
        System.err.println(IndustryName);
        if (StringUtils.isEmpty(IndustryName)){
            return R.ERROR();
        }
        return industryService.getBusinessByIndustry(IndustryName);
    }
}

