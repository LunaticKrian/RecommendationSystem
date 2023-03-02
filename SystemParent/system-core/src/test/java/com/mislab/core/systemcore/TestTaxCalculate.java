package com.mislab.core.systemcore;

import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseCostDTO;
import com.mislab.core.systemcore.pojo.dto.EnterpriseOperationalMsgDto;
import com.mislab.core.systemcore.pojo.calculation.pojo.BusinessTaxInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseBusinessInfoDTO;
import com.mislab.core.systemcore.service.EnterpriseCostService;
import com.mislab.core.systemcore.service.calculation.impl.DataEncapsulationImpl;
import com.mislab.core.systemcore.service.calculation.impl.CalculationServiceImpl;
import com.mislab.core.systemcore.service.calculation.impl.OptimizationCalculationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = SystemCoreApplication.class)
@RunWith(SpringRunner.class)
public class TestTaxCalculate {

    @Autowired
    private CalculationServiceImpl calculationService;

    @Autowired
    private OptimizationCalculationServiceImpl optimizationCalculationService;

    @Autowired
    private DataEncapsulationImpl dataEncapsulation;

    @Autowired
    private EnterpriseCostService enterpriseCostService;

    @Test
    public void getData() {
        EnterpriseCostDTO costDTO = dataEncapsulation.getEnterpriseCostDTO("sddsads");
        System.out.println(costDTO.toString());
    }


    @Test
    public void testCalculate() {
        // 获取封装数据：
        EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO = dataEncapsulation.getEnterpriseInfoDTO("sddsads");
        Map<Object, BusinessTaxInfoDTO> map = calculationService.excludesCorporateVAT(enterpriseBusinessInfoDTO);

        // -------------不含营业税额计算测试---------------
        double sum = 0;
        Collection<BusinessTaxInfoDTO> values = map.values();
        for (BusinessTaxInfoDTO item : values) {
            sum += item.getAmount();
        }
        log.info("计算结果：{}", sum);

        // -------------增值税销项税额计算测试---------------
        Map<String, Double> vatOutputTax = calculationService.excludesVATOutputTax(map);
        double sumVatOutputTax = 0;
        Collection<Double> values1 = vatOutputTax.values();
        for (Double i : values1) {
            sumVatOutputTax += i;
        }
        log.info("增值税销项税额计算结果：{}", sumVatOutputTax);


        EnterpriseCostDTO costDTO = dataEncapsulation.getEnterpriseCostDTO("sddsads");
        Map<String, Double> vatInputTax = calculationService.excludesVATInputTax(costDTO);
        System.out.println(vatInputTax.toString());
        double sum01 = 0.0;
        Collection<Double> values01 = vatInputTax.values();
        for (double i : values01
        ) {
            sum01 += i;

        }

        System.out.println("计算结果：" + sum01);

        System.out.println("--------------------------");

        double v = calculationService.excludesVATPayable(sumVatOutputTax, sum01);
        System.out.println("最后计算结果" + v);

        System.out.println("---------------------");

        double v1 = calculationService.excludesSurTax(v, enterpriseBusinessInfoDTO.getEnterprise());
        System.out.println(v1);

        System.out.println("----------------------");

        double v2 = calculationService.excludesTaxableIncome(enterpriseBusinessInfoDTO);
        System.out.println(v2);

        System.out.println("----------------------");

        double v3 = calculationService.excludesIncomeTaxIndeed(v2, enterpriseBusinessInfoDTO.getEnterprise().getEnterpriseType());
        System.out.println(v3);

        System.out.println("----------------------");

        double v4 = calculationService.excludesUndistributedProfit(v2, v3);
        System.out.println(v4);

        double v5 = calculationService.excludesShareholderPersonalTax(v4);
        System.out.println(v5);
    }

    @Test
    public void testOptimizationCalculation() {
        // 获取封装数据：
        EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO = dataEncapsulation.getEnterpriseInfoDTO("sddsads");
        EnterpriseCostDTO costDTO = dataEncapsulation.getEnterpriseCostDTO("sddsads");

        Map<String, Double> map = optimizationCalculationService.turnoverIncludingTax(enterpriseBusinessInfoDTO);
        Double sum = 0.0;
        for (String item: map.keySet()) {
            sum += map.get(item);
        }
        System.out.println("所有一般纳税人公司的含税营业额：" + sum);

        System.out.println("----------------------");

        System.out.println("计算各公司不含税营业额");
        Map<String, Object> map1 = optimizationCalculationService.turnoverExcludingTax(enterpriseBusinessInfoDTO);
        double v3 = 0.0;
        for (String item: map1.keySet()) {
            Map<String, Double> temp = (Map<String, Double>) map1.get(item);
            System.out.println(item + "\t" + temp);
            v3 += temp.get("result");
        }

        System.out.println("----------------------");

        Double outputTax = optimizationCalculationService.VATOutputTax(enterpriseBusinessInfoDTO);
        System.out.println("一般纳税人公司的增值税销项税额：" + outputTax);

        System.out.println("----------------------");

        Double vatInputTax = optimizationCalculationService.VATInputTax(costDTO);

        System.out.println("计算所有一般纳税人公司的 增值税进项税额：" + vatInputTax);

        System.out.println("----------------------");

        double v = optimizationCalculationService.VATPayable(outputTax, vatInputTax);
        System.out.println("增值税应纳税额：" + v);

        System.out.println("----------------------");

        // 计算计算兼营销售占比：
        double v1 = optimizationCalculationService.proportionOfPartTimeSales(sum, enterpriseBusinessInfoDTO.getEnterprise().getAnnualTurnover());
        System.out.println("计算计算兼营销售占比：" + v1);

        System.out.println("----------------------");

        double cost = optimizationCalculationService.cost(costDTO, v1);
        System.out.println("计算成本费用：" + cost);

        System.out.println("----------------------");

        double v2 = optimizationCalculationService.taxableIncome(v3, cost);
        System.out.println("计算应纳税所得额：" + v2);

        System.out.println("----------------------");

        double v4 = optimizationCalculationService.incomeTaxPayable(v2);
        System.out.println("计算企业所得税应纳税额：" + v4);

        System.out.println("----------------------");

        double v5 = optimizationCalculationService.surTax(v, enterpriseBusinessInfoDTO.getEnterprise());
        System.out.println("计算附加税：" + v5);

        System.out.println("----------------------");

        double v6 = optimizationCalculationService.netProfit(v2, v4);
        System.out.println("净利润：" + v6);

        System.out.println("----------------------");

        double v7 = optimizationCalculationService.personalIncomeTax(v6);
        System.out.println("股东个税：" + v7);

        System.out.println("----------------------");

        double v8 = optimizationCalculationService.turnoverIncludingTax(enterpriseBusinessInfoDTO.getEnterprise().getAnnualTurnover(), sum);
        System.out.println("小规模纳税人公司的含税营业额：" + v8);

        System.out.println("----------------------");

        System.out.println("---------小规模纳税人-------------");
        double v9 = optimizationCalculationService.smallCost(costDTO, v1);
        System.out.println("计算成本费用：" + v9);
        System.out.println("小规模 应纳税所得额：" + optimizationCalculationService.taxableIncome(v8, v9));
        System.out.println("小规模 应纳税额：" + optimizationCalculationService.incomeTaxPayable(optimizationCalculationService.taxableIncome(v8, v9)));
        System.out.println("小规模 净利润：" + optimizationCalculationService.netProfit(optimizationCalculationService.taxableIncome(v8, v9), optimizationCalculationService.incomeTaxPayable(optimizationCalculationService.taxableIncome(v8, v9))));
        System.out.println("小规模 股东个税：" + optimizationCalculationService.personalIncomeTax(optimizationCalculationService.netProfit(optimizationCalculationService.taxableIncome(v8, v9), optimizationCalculationService.incomeTaxPayable(optimizationCalculationService.taxableIncome(v8, v9)))));
    }
}
