package com.mislab.core.systemcore.controller;


import com.mislab.common.result.R;
import com.mislab.core.systemcore.pojo.calculation.pojo.BusinessTaxInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseBusinessInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseCostDTO;
import com.mislab.core.systemcore.service.calculation.impl.CalculationServiceImpl;
import com.mislab.core.systemcore.service.calculation.impl.DataEncapsulationImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "计算接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/core/calculation")
public class CalculationController {

    @Autowired
    private CalculationServiceImpl calculationService;

    @Autowired
    private DataEncapsulationImpl dataEncapsulation;

    @GetMapping("/calculate")
    public R calculate(@ApiParam("企业唯一标识") @RequestParam String enterpriseKey) {
        Map<String, Object> hashMap = new HashMap<>();

        log.info("企业唯一标识：{}", enterpriseKey);

        // 获取封装数据：
        EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO = dataEncapsulation.getEnterpriseInfoDTO(enterpriseKey);
        Map<Object, BusinessTaxInfoDTO> map = calculationService.excludesCorporateVAT(enterpriseBusinessInfoDTO);

        double sum = 0;
        Collection<BusinessTaxInfoDTO> values = map.values();
        for (BusinessTaxInfoDTO item : values) {
            sum += item.getAmount();
        }
        // 不含营业税额：
        hashMap.put("notIncludedBusiTax", sum);

        Map<String, Double> vatOutputTax = calculationService.excludesVATOutputTax(map);
        double sumVatOutputTax = 0;
        Collection<Double> values1 = vatOutputTax.values();
        for (Double i : values1) {
            sumVatOutputTax += i;
        }

        // 增值税销项税额：
        hashMap.put("outputVATAmount", sumVatOutputTax);

        EnterpriseCostDTO costDTO = dataEncapsulation.getEnterpriseCostDTO(enterpriseKey);
        Map<String, Double> vatInputTax = calculationService.excludesVATInputTax(costDTO);
        System.out.println(vatInputTax.toString());
        double sum01 = 0.0;
        Collection<Double> values01 = vatInputTax.values();
        for (double i:values01
        ) {
            sum01 += i;
        }

        // 增值税应纳税额：
        double v = calculationService.excludesVATPayable(sumVatOutputTax, sum01);
        hashMap.put("VATLiability", v);

        // 附加税：
        double v1 = calculationService.excludesSurTax(v, enterpriseBusinessInfoDTO.getEnterprise());
        hashMap.put("surtax", v1);

        // 企业所得税应纳税额：
        double v2 = calculationService.excludesTaxableIncome(enterpriseBusinessInfoDTO);
        hashMap.put("corporateIncomeTaxLiability", v2);

        // 企业所得税税率额：
        double v3 = calculationService.excludesIncomeTaxIndeed(v2, enterpriseBusinessInfoDTO.getEnterprise().getEnterpriseType());
        hashMap.put("theAmountOfCorporateIncomeTaxRate", v3);

        // 计算股东分红所得：
        double v4 = calculationService.excludesUndistributedProfit(v2, v3);
        hashMap.put("dividendsReceivedByShareholders", v4);

        // 股东个税
        double v5 = calculationService.excludesShareholderPersonalTax(v4);
        hashMap.put("shareholderTax", v5);

        R result = R.SUCCESS();
        result.setData(hashMap);
        return result;
    }
}
