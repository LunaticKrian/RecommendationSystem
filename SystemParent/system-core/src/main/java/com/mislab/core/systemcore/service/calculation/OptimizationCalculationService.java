package com.mislab.core.systemcore.service.calculation;

import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseBusinessInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseCostDTO;
import com.mislab.core.systemcore.pojo.entity.Enterprise;

import java.util.Map;

public interface OptimizationCalculationService {
    public Map<String, Double> turnoverIncludingTax(EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO);

    public Map<String, Object> turnoverExcludingTax(EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO);

    public Double VATOutputTax(EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO);

    public Double VATInputTax(EnterpriseCostDTO enterpriseCostDTO);

    public double VATPayable(double VATOutputTax, double VATInputTax);

    public double proportionOfPartTimeSales(double subTurnoverIncludingTax, double allTurnoverIncludingTax);

    public double cost(EnterpriseCostDTO enterpriseCostDTO, double proportionOfPartTimeSales);

    public double smallCost(EnterpriseCostDTO enterpriseCostDTO, double proportionOfPartTimeSales);

    public double taxableIncome(double turnoverExcludingTax, double cost);


    public double incomeTaxPayable(double taxableIncome);


    public double surTax(double VATPayable, Enterprise enterprise);

    public double netProfit(double taxableIncome,double incomeTaxPayable);

    public double personalIncomeTax(double netProfit);

    public double turnoverIncludingTax(double allAmount, double normalTurnoverIncludingTax);
}
