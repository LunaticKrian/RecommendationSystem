package com.mislab.core.systemcore;

import com.mislab.core.systemcore.mapper.EnterpriseBusinessMapper;
import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.mislab.core.systemcore.pojo.entity.TaxRate;
import com.mislab.core.systemcore.pojo.vo.*;
import com.mislab.core.systemcore.service.impl.DataEncapsulationImpl;
import com.mislab.core.systemcore.service.impl.TaxRateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = SystemCoreApplication.class)
@RunWith(SpringRunner.class)
public class TestTaxCalculate {

    @Autowired
    private TaxRateServiceImpl taxRateService;

    @Autowired
    private EnterpriseBusinessMapper enterpriseBusinessMapper;

    @Autowired
    private DataEncapsulationImpl dataEncapsulation;


    @Test
    public void test() {
        EnterpriseBusinessTaxVO enterpriseBusinessTaxVOList = dataEncapsulation.getEnterpriseBusinessTaxVOList("6f716900a18e");
        System.out.println(enterpriseBusinessTaxVOList);
    }


    @Test
    public void testExcludesCorporateVAT() {
        EnterpriseInfoVO enterpriseInfoVO = new EnterpriseInfoVO();
        Enterprise enterprise = new Enterprise();

        enterprise.setSalesTaxpayer(0);
        enterprise.setTaxpayerQualification(0);
        enterprise.setInvoiceType(2);

        EnterpriseBusinessTaxVO businessTaxVO01 = new EnterpriseBusinessTaxVO("运输服务", 100.0, new TaxRate(), new EnterpriseBusiness());
        EnterpriseBusinessTaxVO businessTaxVO02 = new EnterpriseBusinessTaxVO("仓储服务", 100.0, new TaxRate(), new EnterpriseBusiness());
        EnterpriseBusinessTaxVO businessTaxVO03 = new EnterpriseBusinessTaxVO("搬运服务服务", 100.0, new TaxRate(), new EnterpriseBusiness());
        EnterpriseBusinessTaxVO businessTaxVO04 = new EnterpriseBusinessTaxVO("运输代理服务", 100.0, new TaxRate(), new EnterpriseBusiness());

        List<EnterpriseBusinessTaxVO> enterpriseBusinessTaxVOS = new ArrayList<>();

        enterpriseBusinessTaxVOS.add(businessTaxVO01);
        enterpriseBusinessTaxVOS.add(businessTaxVO02);
        enterpriseBusinessTaxVOS.add(businessTaxVO03);
        enterpriseBusinessTaxVOS.add(businessTaxVO04);

        enterpriseInfoVO.setEnterprise(enterprise);
        enterpriseInfoVO.setEnterpriseBusinessList(enterpriseBusinessTaxVOS);

        Map<Object, BusinessTaxVO> objectObjectMap = taxRateService.excludesCorporateVAT(enterpriseInfoVO);

        System.out.println(objectObjectMap);
    }


    @Test
    public void testVATInputTax() {
        EnterpriseCostVO enterpriseCostVO = new EnterpriseCostVO();

        CostInfoVO costInfoVO1 = new CostInfoVO();
        costInfoVO1.setCostName("车辆成本");
        costInfoVO1.setAmount(100);

        CostInfoVO costInfoVO2 = new CostInfoVO();
        costInfoVO2.setCostName("人工成本");
        costInfoVO2.setAmount(100);

        CostInfoVO costInfoVO3 = new CostInfoVO();
        costInfoVO3.setCostName("办公成本");
        costInfoVO3.setAmount(100);

        CostInfoVO costInfoVO4 = new CostInfoVO();
        costInfoVO4.setCostName("运输成本(油费）");
        costInfoVO4.setAmount(100);

        CostInfoVO costInfoVO5 = new CostInfoVO();
        costInfoVO5.setCostName("运输成本(路桥费)");
        costInfoVO5.setAmount(100);

        ArrayList<CostInfoVO> costInfoVoList = new ArrayList<>();

        costInfoVoList.add(costInfoVO1);
        costInfoVoList.add(costInfoVO2);
        costInfoVoList.add(costInfoVO3);
        costInfoVoList.add(costInfoVO4);
        costInfoVoList.add(costInfoVO5);

        SupplierProportionInfoVO supplierProportionInfoVO1 = new SupplierProportionInfoVO();
        supplierProportionInfoVO1.setName("一般纳税人(新车)");
        supplierProportionInfoVO1.setPercentage(0.3);
        supplierProportionInfoVO1.setTaxRate(0.13);

        SupplierProportionInfoVO supplierProportionInfoVO2 = new SupplierProportionInfoVO();
        supplierProportionInfoVO2.setName("一般纳税人(新车)");
        supplierProportionInfoVO2.setPercentage(0.3);
        supplierProportionInfoVO2.setTaxRate(0.13);

        SupplierProportionInfoVO supplierProportionInfoVO3 = new SupplierProportionInfoVO();
        supplierProportionInfoVO3.setName("一般纳税人(新车)");
        supplierProportionInfoVO3.setPercentage(0.3);
        supplierProportionInfoVO3.setTaxRate(0.13);

        SupplierProportionInfoVO supplierProportionInfoVO4 = new SupplierProportionInfoVO();
        supplierProportionInfoVO4.setName("一般纳税人(新车)");
        supplierProportionInfoVO4.setPercentage(0.3);
        supplierProportionInfoVO4.setTaxRate(0.13);

        SupplierProportionInfoVO supplierProportionInfoVO5 = new SupplierProportionInfoVO();
        supplierProportionInfoVO5.setName("一般纳税人(新车)");
        supplierProportionInfoVO5.setPercentage(0.3);
        supplierProportionInfoVO5.setTaxRate(0.13);

        ArrayList<SupplierProportionInfoVO> supplierProportionInfoVOList = new ArrayList<>();

        supplierProportionInfoVOList.add(supplierProportionInfoVO1);
        supplierProportionInfoVOList.add(supplierProportionInfoVO2);
        supplierProportionInfoVOList.add(supplierProportionInfoVO3);
        supplierProportionInfoVOList.add(supplierProportionInfoVO4);
        supplierProportionInfoVOList.add(supplierProportionInfoVO5);

        costInfoVO1.setList(supplierProportionInfoVOList);
        costInfoVO2.setList(supplierProportionInfoVOList);
        costInfoVO3.setList(supplierProportionInfoVOList);
        costInfoVO4.setList(supplierProportionInfoVOList);
        costInfoVO5.setList(supplierProportionInfoVOList);

        enterpriseCostVO.setCostInfoVOList(costInfoVoList);

        // 调用方法：
        Map<String, Double> stringDoubleMap = taxRateService.VATInputTax(enterpriseCostVO);

        // 输出结果：
        for (Map.Entry<String, Double> entry : stringDoubleMap.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            System.out.println(key + ":" + value);
        }
    }
}
