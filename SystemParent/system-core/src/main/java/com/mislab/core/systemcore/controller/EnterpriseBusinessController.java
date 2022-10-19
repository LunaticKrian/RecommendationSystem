package com.mislab.core.systemcore.controller;


import com.mislab.common.result.R;
import com.mislab.core.systemcore.mapper.EnterpriseBusinessMapper;
import com.mislab.core.systemcore.pojo.dto.CostDto;
import com.mislab.core.systemcore.pojo.dto.EnterpriseBusinessDTO;
import com.mislab.core.systemcore.pojo.entity.Cost;
import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.mislab.core.systemcore.pojo.dto.Enterprise2VO;
import com.mislab.core.systemcore.service.BusinessService;
import com.mislab.core.systemcore.service.CostService;
import com.mislab.core.systemcore.service.EnterpriseBusinessService;
import com.mislab.core.systemcore.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 * @author krian
 * @since 2022-09-30
 */
@RestController
@RequestMapping("/enterprise_operation")
public class EnterpriseBusinessController {

    @Autowired
    EnterpriseBusinessService enterpriseBusinessService;

    @Autowired
    CostService costService;

    @Autowired
    EnterpriseService enterpriseService;

    @Autowired
    BusinessService businessService;

    /**
     * 获取接口存放enterprise business与costs数据
     */
    @PostMapping("save")
    public R saveData(@RequestBody Enterprise2VO enterprise2VO){

        R r2 = costService.update(enterprise2VO.getCostDto(),enterprise2VO.getEnterpriseKey());
        enterpriseBusinessService.setSTATAC(enterprise2VO.getEnterpriseKey()
                ,enterprise2VO.getSalesTaxpayer()
                ,enterprise2VO.getAnnualTurnover()
                ,enterprise2VO.getAnnualCost());
        R r1 = enterpriseBusinessService.updateByEnterpriseKey(enterprise2VO.getEPBs(),enterprise2VO.getEnterpriseKey());
        //返回的r1与r2永远是success无需判断
        return R.SUCCESS();
    }

    /**
     * 打开页面返回的页面数据
     * @param enterpriseKey
     * @return
     */
    @GetMapping("getByKey")
    public R getData( String enterpriseKey){
        List<EnterpriseBusiness> EPBs = enterpriseBusinessService.getByEnterpriseKey(enterpriseKey);
        List<CostDto> costs = costService.getCosts(enterpriseKey);
        Enterprise2VO enterprise2VO = enterpriseBusinessService.getDateIITable(enterpriseKey);
        return R.SUCCESS().data("enterpriseBusiness",EPBs)
                .data("costs",costs)
                .data("date",enterprise2VO);
    }
}

