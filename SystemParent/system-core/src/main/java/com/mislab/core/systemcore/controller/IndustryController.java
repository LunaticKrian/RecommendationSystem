package com.mislab.core.systemcore.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mislab.common.result.R;
import com.mislab.core.systemcore.pojo.entity.Business;
import com.mislab.core.systemcore.pojo.entity.BusinessIndustry;
import com.mislab.core.systemcore.service.BusinessIndustryService;
import com.mislab.core.systemcore.service.BusinessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
    private BusinessIndustryService businessIndustryService;

    @Autowired
    private BusinessService businessService;

    @ApiOperation("获取行业对应的业务")
    @GetMapping("/getBusinessByIndustry")
    public R getBusinessByIndustry(@ApiParam("企业id") Integer industryId){
        log.info("行业id为:{}",industryId);
        if (industryId == null){
            return R.ERROR();
        }
        List<BusinessIndustry> businessIndustries = businessIndustryService.list(new LambdaQueryWrapper<BusinessIndustry>().eq(BusinessIndustry::getIndustryId, industryId));
        List<Integer> businessIds = businessIndustries.stream()
                .map(BusinessIndustry::getBusinessId)
                .collect(Collectors.toList());
        List<String> res = businessService.listByIds(businessIds).stream()
                .map(Business::getName)
                .collect(Collectors.toList());
        return R.SUCCESS().data("businessList",res);
    }
}

