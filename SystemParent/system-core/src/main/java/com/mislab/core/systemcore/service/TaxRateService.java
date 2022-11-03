package com.mislab.core.systemcore.service;

import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.pojo.entity.TaxRate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mislab.core.systemcore.pojo.vo.BusinessTaxVO;
import com.mislab.core.systemcore.pojo.vo.EnterpriseCostVO;
import com.mislab.core.systemcore.pojo.vo.EnterpriseInfoVO;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
public interface TaxRateService extends IService<TaxRate> {

    public Map<Object, BusinessTaxVO> excludesCorporateVAT(EnterpriseInfoVO enterpriseInfoVO);

    public Map<String, Double> VATOutputTax(Map<Object, Object> VATMap);

    public Map<String, Double> VATInputTax(EnterpriseCostVO enterpriseCostVO);

    public Map<String, Double> VATPayable(Map<String, Double> VATOutputTax, Map<String, Double> VATInputTax);

    public Map<Object, Double> surtax(Map<String, Double> VATPayable, Enterprise enterprise);

    public double taxableIncome(EnterpriseInfoVO enterpriseInfoVO);

    public double incomeTaxIndeed(double taxableIncome, int enterpriseType);

    public double undistributedProfit(double taxableIncome, double incomeTaxIndeed);

    public double shareholderPersonalTax(double undistributedProfit);
}
