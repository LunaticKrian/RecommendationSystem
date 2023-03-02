package com.mislab.core.systemcore.controller;


import com.mislab.common.result.R;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseBusinessInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseCostDTO;
import com.mislab.core.systemcore.service.calculation.OptimizationCalculationService;
import com.mislab.core.systemcore.service.calculation.impl.DataEncapsulationImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "优化方案计算接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/core/better/calculation")
public class OptimizationCalculationController {

    @Autowired
    private OptimizationCalculationService optimizationCalculationService;

    @Autowired
    private DataEncapsulationImpl dataEncapsulation;

    @GetMapping("/calculate")
    public R calculate(@ApiParam("企业唯一标识") @RequestParam String enterpriseKey) {
        Map<String, Object> data = new HashMap<>();

        // 获取封装数据：
        EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO = dataEncapsulation.getEnterpriseInfoDTO(enterpriseKey);
        EnterpriseCostDTO costDTO = dataEncapsulation.getEnterpriseCostDTO(enterpriseKey);

        // 一般纳税人 含税营业额：
        Map<String, Double> map = optimizationCalculationService.turnoverIncludingTax(enterpriseBusinessInfoDTO);
        Double turnoverIncludingTax = 0.0;
        for (String item: map.keySet()) {
            turnoverIncludingTax += map.get(item);
        }
        data.put("一般纳税人含税营业额", turnoverIncludingTax);

        Map<String, Object> map1 = optimizationCalculationService.turnoverExcludingTax(enterpriseBusinessInfoDTO);
        double turnoverExcludingTax = 0.0;
        for (String item: map1.keySet()) {
            Map<String, Double> temp = (Map<String, Double>) map1.get(item);
            turnoverExcludingTax += temp.get("result");
        }
        data.put("一般纳税人不含税营业额", turnoverExcludingTax);

        Double VATOutputTax = optimizationCalculationService.VATOutputTax(enterpriseBusinessInfoDTO);
        data.put("一般纳税人增值税销项税额", VATOutputTax);

        Double VATInputTax = optimizationCalculationService.VATInputTax(costDTO);
        data.put("一般纳税人增值税进项税额", VATInputTax);

        double VATPayable = optimizationCalculationService.VATPayable(VATOutputTax, VATInputTax);
        data.put("一般纳税人增值税应纳税额", VATPayable);

        double proportionOfPartTimeSales = optimizationCalculationService.proportionOfPartTimeSales(turnoverIncludingTax, enterpriseBusinessInfoDTO.getEnterprise().getAnnualTurnover());
        data.put("兼营销售占比", proportionOfPartTimeSales);

        double cost = optimizationCalculationService.cost(costDTO, proportionOfPartTimeSales);
        data.put("一般纳税人成本费用", cost);

        double taxableIncome = optimizationCalculationService.taxableIncome(turnoverExcludingTax, cost);
        data.put("一般纳税人应纳税所得额", taxableIncome);

        double incomeTaxPayable = optimizationCalculationService.incomeTaxPayable(taxableIncome);
        data.put("一般纳税人所得税应纳税额", incomeTaxPayable);

        double surTax = optimizationCalculationService.surTax(VATPayable, enterpriseBusinessInfoDTO.getEnterprise());
        data.put("一般纳税人附加税", surTax);

        double netProfit = optimizationCalculationService.netProfit(taxableIncome, incomeTaxPayable);
        data.put("一般纳税人净利润", netProfit);

        double personalIncomeTax = optimizationCalculationService.personalIncomeTax(netProfit);
        data.put("一般纳税人股东个税", personalIncomeTax);

        // -------------------小规模纳税------------------

        double smallTurnoverIncludingTax = optimizationCalculationService.turnoverIncludingTax(enterpriseBusinessInfoDTO.getEnterprise().getAnnualTurnover(), turnoverIncludingTax);
        data.put("小规模纳税人含税营业额", smallTurnoverIncludingTax);

        double smallCost = optimizationCalculationService.smallCost(costDTO, proportionOfPartTimeSales);
        data.put("小规模纳税人成本费用", smallCost);

        double smallTaxableIncome = optimizationCalculationService.taxableIncome(smallTurnoverIncludingTax, smallCost);
        data.put("小规模纳税人应纳税所得额", smallTaxableIncome);

        double smallIncomeTaxPayable = optimizationCalculationService.incomeTaxPayable(smallTaxableIncome);
        data.put("小规模纳税人应纳税额", smallIncomeTaxPayable);

        double smallNetProfit = optimizationCalculationService.netProfit(smallTaxableIncome, smallIncomeTaxPayable);
        data.put("小规模纳税人净利润", smallNetProfit);

        double smallPersonalIncomeTax = optimizationCalculationService.personalIncomeTax(smallNetProfit);
        data.put("小规模纳税人股东个税", smallPersonalIncomeTax);

        // -------------------计算合计------------------
        double sumTax = VATPayable + incomeTaxPayable + personalIncomeTax + 0 + smallIncomeTaxPayable + smallPersonalIncomeTax;
        data.put("合计税金", sumTax);
        data.put("税负率", sumTax / enterpriseBusinessInfoDTO.getEnterprise().getAnnualTurnover());

        R success = R.SUCCESS();
        success.setData(data);
        return success;
    }
}
