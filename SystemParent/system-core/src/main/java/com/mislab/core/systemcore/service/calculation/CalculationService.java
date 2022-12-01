package com.mislab.core.systemcore.service.calculation;

import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.pojo.entity.TaxRate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mislab.core.systemcore.pojo.calculation.pojo.BusinessTaxInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseCostDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseBusinessInfoDTO;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
public interface CalculationService extends IService<TaxRate> {

    Map<Object, BusinessTaxInfoDTO> excludesCorporateVAT(EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO);

    Map<String, Double> excludesVATOutputTax(Map<Object, BusinessTaxInfoDTO> VATMap);

    Map<String, Double> excludesVATInputTax(EnterpriseCostDTO enterpriseCostDTO);

    double excludesVATPayable(double VATOutputTax, double VATInputTax);

    double excludesSurTax(double VATPayable, Enterprise enterprise);

    double excludesTaxableIncome(EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO);

    double excludesIncomeTaxIndeed(double taxableIncome, int enterpriseType);

    double excludesUndistributedProfit(double taxableIncome, double incomeTaxIndeed);

    double excludesShareholderPersonalTax(double undistributedProfit);
}
