package com.mislab.core.systemcore;

import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseCostDTO;
import com.mislab.core.systemcore.pojo.dto.EnterpriseOperationalMsgDto;
import com.mislab.core.systemcore.pojo.calculation.pojo.BusinessTaxInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseBusinessInfoDTO;
import com.mislab.core.systemcore.service.EnterpriseCostService;
import com.mislab.core.systemcore.service.calculation.impl.DataEncapsulationImpl;
import com.mislab.core.systemcore.service.calculation.impl.CalculationServiceImpl;
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
    private DataEncapsulationImpl dataEncapsulation;

    @Autowired
    private EnterpriseCostService enterpriseCostService;

    @Test
    public void getData(){
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
        for (double i:values01
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
}
