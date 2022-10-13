package com.mislab.core.systemcore.controller;


import com.mislab.common.result.R;
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

        R r2 = costService.update(enterprise2VO.getCosts(),enterprise2VO.getEnterpriseKey());
        enterpriseBusinessService.setSTATAC(enterprise2VO.getEnterpriseKey(),salesTaxpayer,annualTurnover,annualCost);
        //将获取到的enterpriseKey放入enterpriseBuiness
        for (EnterpriseBusiness enterpriseBusiness:EPBs){
            enterpriseBusiness.setEnterpriseKey(enterpriseKey);
        }
        R r1 = enterpriseBusinessService.updateByEnterpriseKey(EPBs);
        //返回的r1与r2永远是success无需判断
        return R.SUCCESS();
    }

    /**
     * 打开页面返回的页面数据
     * @param enterpriseKey
     * @return
     */
    @GetMapping("getByKey")
    public R getData(String enterpriseKey){
        List<EnterpriseBusiness> EPBs = enterpriseBusinessService.getByEnterpriseKey(enterpriseKey);
        List<Cost> costs = costService.getCosts(enterpriseKey);
        Enterprise2VO enterprise2VO = enterpriseBusinessService.getDateIITable(enterpriseKey);
        return R.SUCCESS().data("enterpriseBusiness",EPBs)
                .data("costs",costs)
                .data("date",enterprise2VO);
    }
}

