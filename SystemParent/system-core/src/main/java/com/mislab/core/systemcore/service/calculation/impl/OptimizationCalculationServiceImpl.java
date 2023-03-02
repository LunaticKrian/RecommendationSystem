package com.mislab.core.systemcore.service.calculation.impl;

import com.mislab.core.systemcore.pojo.calculation.pojo.*;
import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.service.calculation.OptimizationCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class OptimizationCalculationServiceImpl implements OptimizationCalculationService {


    /**
     * 计算所有一般纳税人公司的 含税营业额：
     * 计算公式：一般纳税人公司的含税营业额 = 各公司含税营业额之和 = 各公司服务的营业额 * 其所对应的“甲方资质”中的一般纳税人占比的和
     */
    public Map<String, Double> turnoverIncludingTax(EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO) {
        Map<String, Double> result = new HashMap<>();
        // 获取每个业务独立出来的公司含税营业额：
        List<EnterpriseBusinessTaxInfoDTO> enterpriseBusinessList = enterpriseBusinessInfoDTO.getEnterpriseBusinessList();
        for (EnterpriseBusinessTaxInfoDTO item : enterpriseBusinessList) {
            // 获取每一个业务中的“甲方资质”中的一般纳税人占比：
            Double generalTaxpayerRatio = item.getEnterpriseBusiness().getGeneralTaxpayerRatio();
            // 获取业务营业额：
            Double amount = item.getAmount();
            // 计算独立业务成为的公司含税营业额：
            Double v = amount * generalTaxpayerRatio;
            // 保存计算结果：
            result.put(item.getBusinessName(), v);
        }
        return result;
    }

    /**
     * 不含税营业额：
     * 计算公式：其含税营业额 / (1 + 增值税税率)
     */
    public Map<String, Object> turnoverExcludingTax(EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO) {
        Map<String, Object> result = new HashMap<>();
        // 获取各个公司的含税营业额：
        Map<String, Double> turnoverIncludingTaxes = turnoverIncludingTax(enterpriseBusinessInfoDTO);
        // 获取每个业务独立出来的公司含税营业额：
        List<EnterpriseBusinessTaxInfoDTO> enterpriseBusinessList = enterpriseBusinessInfoDTO.getEnterpriseBusinessList();
        for (EnterpriseBusinessTaxInfoDTO item : enterpriseBusinessList) {
            Map<String, Double> temp = new HashMap<>();
            // 获取每个独立公司的含税营业额：
            Double turnoverET = turnoverIncludingTaxes.get(item.getBusinessName());
            // 获取每个业务的增值税税率：
            Double rate = item.getTaxRate().getTaxRate();
            // 计算税率：
            Double v = turnoverET / (1 + rate);
            // 保存计算结果：
            temp.put("result", v);
            temp.put("rate", rate);
            result.put(item.getBusinessName(), temp);
        }
        return result;
    }

    /**
     * 计算所有一般纳税人公司的 增值税销项税额：
     * 计算公式：各纳税公司的不含税营业额 * 相对应增值税税率的和
     */
    public Double VATOutputTax(EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO) {
        double result = 0.0;
        // 获取各纳税公司的不含税营业额：
        Map<String, Object> turnoverExcludingTaxes = turnoverExcludingTax(enterpriseBusinessInfoDTO);
        // 获取每一个公司的不含税营业额：
        for (String item : turnoverExcludingTaxes.keySet()) {
            Map<String, Double> o = (Map<String, Double>) turnoverExcludingTaxes.get(item);
            // 获取税率：
            Double rate = o.get("rate");
            // 获取不含税营业额：
            Double res = o.get("result");
            // 计算结果：
            result += res * rate;
        }

        return result;
    }

    /**
     * 计算所有一般纳税人公司的 增值税进项税额：
     * 计算公式：(各进项含税营业额 * 供应商资质中一般纳税人和含专票的比例) / (1 + 其对应增值税税率) * 相应增值税税率
     */
    public Double VATInputTax(EnterpriseCostDTO enterpriseCostDTO) {
        double sum = 0;
        //获取各进项含税营业额：
        List<CostInfoDTO> costInfoDTOList = enterpriseCostDTO.getCostInfoDTOList();
        for (CostInfoDTO item : costInfoDTOList) {
            // 获取成本花费：
            double amount = item.getAmount();
            // 获取所有供应商资质：
            List<SupplierProportionInfoDTO> supplierProportionInfoDTOS = item.getList();
            // 获取供应商位一般纳税人信息和含专票：
            for (SupplierProportionInfoDTO sup : supplierProportionInfoDTOS) {  // 遍历供应商信息：
                if (sup.getSupId() == 1 || sup.getSupId() == 2 || sup.getSupId() == 4 || sup.getSupId() == 7) {
                    // 获得一般纳税人的比例：
                    double proportion = sup.getProportion();
                    // 对应增值税税率：
                    double taxRate = sup.getTaxRate();
                    // 计算：
                    double v = (amount * proportion) / (1 + taxRate) * taxRate;
                    log.info("({} * {}) / {} * {}", amount, proportion, (1 + taxRate), taxRate);
                    sum += v;
                }
            }
        }

        return sum;
    }

    /**
     * 增值税应纳税额
     * 计算公式：（增值税销项税额 - 增值税进项税额）：
     *
     * @param VATOutputTax 增值税销项税额
     * @param VATInputTax  增值税进项税额
     * @return double（增值税应纳税额）
     */
    public double VATPayable(double VATOutputTax, double VATInputTax) {
        return VATOutputTax - VATInputTax;
    }

    /**
     * 计算兼营销售占比：
     * 计算公式：所有一般纳税人公司的含税营业额之和 / 总的含税营业额。
     */
    public double proportionOfPartTimeSales(double subTurnoverIncludingTax, double allTurnoverIncludingTax) {
        return subTurnoverIncludingTax / allTurnoverIncludingTax;
    }

    /**
     * 一般纳税人 计算成本费用：
     * 计算公式：车辆成本 * 供应商资质中对应一般纳税人的比例
     * +（人工成本+办公成本）* 兼营销售占比
     * + 运输成本（油费）* 对应一般纳税人的比例
     * + 运输成本（路桥费）*对应一般纳税人的比例。
     */
    public double cost(EnterpriseCostDTO enterpriseCostDTO, double proportionOfPartTimeSales) {
        // 获取企业的指出项目：
        List<CostInfoDTO> costInfoDTOList = enterpriseCostDTO.getCostInfoDTOList();
        double allCost = 0.0;
        // 遍历每一个成本花销项：
        for (CostInfoDTO item : costInfoDTOList) {
            // 判断是否为车辆成本：
            if (Objects.equals(item.getCostName(), "车辆成本") || Objects.equals(item.getCostName(), "运输成本(油费）") || Objects.equals(item.getCostName(), "运输成本(路桥费)")) {
                //TODO:这里有问题，供应商资质中对应一般纳税人的比例默认设置为了1，这里如果后面需要更改的话，这需要根据数据库表中的信息查询占比
                // 但是这里是拆分公司，所以不明确占比多少！！！
                allCost += item.getAmount() * 1;
            } else if (Objects.equals(item.getCostName(), "人工成本") || Objects.equals(item.getCostName(), "办公成本")) {
                allCost += item.getAmount() * proportionOfPartTimeSales;
            }
        }
        return allCost;
    }

    /**
     * 小规模 计算成本费用：
     * 计算公式：（人工成本 + 办公成本）*（1-兼营销售占比）
     * <p>
     * @param enterpriseCostDTO
     * @param proportionOfPartTimeSales
     * @return
     */
    public double smallCost(EnterpriseCostDTO enterpriseCostDTO, double proportionOfPartTimeSales) {
        // 获取企业的指出项目：
        List<CostInfoDTO> costInfoDTOList = enterpriseCostDTO.getCostInfoDTOList();
        double allCost = 0.0;
        // 遍历每一个成本花销项：
        for (CostInfoDTO item : costInfoDTOList) {
            // 判断是否为车辆成本：
            if (Objects.equals(item.getCostName(), "人工成本") || Objects.equals(item.getCostName(), "办公成本")) {
                allCost += item.getAmount() * (1 - proportionOfPartTimeSales);
            }
        }
        return allCost;
    }

    /**
     * 计算应纳税所得额：
     * 计算公式：所有一般纳税人公司的不含税营业额-成本费用
     */
    public double taxableIncome(double turnoverExcludingTax, double cost) {
        return turnoverExcludingTax - cost;
    }

    /**
     * 计算企业所得税应纳税额：
     * 计算公式：应纳税所得额 * 企业所得税税率，
     * 其中企业所得税税率的规则见1.3节（2）中计算4。
     * 如，计算3中应纳税所得额为144.62，值高于100但低于300，所得税税率为12.5%*20%=5%，则企业所得税应纳税额=144.62*5%=7.23。
     */
    public double incomeTaxPayable(double taxableIncome) {
        // 先判断企业所得税税率：
        double taxRate = 0.05;
//        if (taxableIncome > 100 && taxableIncome < 300) {
//            taxRate = 0.05;
//        }
        // 计算应纳税额：
        return taxableIncome * taxRate;
    }

    /**
     * 计算附加税：
     * 计算公式：“增值税应纳税额” * 相应附加税税率
     * <p>
     * 附加税税率的种类有：
     *  1.一般企业的附加税税率为 12%；
     *  2.小型微利企业：年应纳税所得额在300万以下，人员数量300人以下，总资产5000万以下的企业，现行正常附加税减半征收，即原附加税 * 50%。
     */
    public double surTax(double VATPayable, Enterprise enterprise) {
        double taxRate = 0.12;
        if (VATPayable < 300) {
            taxRate *= 0.5;
        }
        return VATPayable * taxRate;
    }

    /**
     * 计算企业未分配利润（净利润）：
     * 计算公式：应纳税所得额 - 企业所得税应纳税额
     */
    public double netProfit(double taxableIncome,double incomeTaxPayable) {
        return taxableIncome - incomeTaxPayable;
    }

    /**
     * 计算股东个税：
     * 计算公式：股息红利所得税税率 * 企业未分配利润，其中“股息红利所得税税率”为20%
     */
    public double personalIncomeTax(double netProfit) {
        double taxRate = 0.2;
        return netProfit * taxRate;
    }

    /**
     * 计算所有小规模纳税人公司的含税营业额：
     * 计算公式：总的含税营业额 - 所有一般纳税人公司的含税营业额
     */
    public double turnoverIncludingTax(double allAmount, double normalTurnoverIncludingTax) {
        return allAmount - normalTurnoverIncludingTax;
    }

    //TODO: 小规模纳税人业务拆分，含税营业额 = 不含税营业额；增值税应纳税额 = 0；附加税 = 0；
    // 计算所有小规模纳税人公司的不含税营业额：=所有小规模纳税人公司的含税营业额/(1+增值税税率)，其中当前政策增值税税率为0%，但后面可能有会政策变动。
    // 计算增值税应纳税额：=总的不含税营业额*相应增值税税率，即目前政策为0。
    // 计算附加税：=总的增值税应纳税额*相应附加税税率，结果为0。
}
