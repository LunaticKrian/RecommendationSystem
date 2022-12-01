package com.mislab.core.systemcore.service.calculation.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mislab.common.utils.TimeUtils;
import com.mislab.core.systemcore.mapper.TaxRateMapper;
import com.mislab.core.systemcore.pojo.calculation.pojo.BusinessTaxInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseBusinessInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseBusinessTaxInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.CostInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.SupplierProportionInfoDTO;
import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.mislab.core.systemcore.pojo.entity.TaxRate;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseCostDTO;
import com.mislab.core.systemcore.service.calculation.CalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mislab.core.systemcore.common.enums.calculation.BusinessTaxEnum.*;
import static com.mislab.core.systemcore.common.enums.calculation.CommentConstant.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
@Slf4j
@Service

public class CalculationServiceImpl extends ServiceImpl<TaxRateMapper, TaxRate> implements CalculationService {

    /**
     * 计算不含税营业额
     * 计算公式1：各服务营业额/(1+增值税税率)
     * 计算公式2：当前服务营业额 * 所对应甲方资质中选择一般纳税人的比；含专票营业额/(1+3%)+(该服务营业额-含专票营业额)/(1+0%)
     *
     * @param enterpriseBusinessInfoDTO 企业经营项目基本信息
     * @return Map(存放各项经营的不含税营业额)
     */
    @SuppressWarnings("all")
    public Map<Object, BusinessTaxInfoDTO> excludesCorporateVAT(EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO) {
        HashMap<Object, BusinessTaxInfoDTO> resultMap = new HashMap<>();

        List<EnterpriseBusinessTaxInfoDTO> enterpriseBusinessList = enterpriseBusinessInfoDTO.getEnterpriseBusinessList();
        Integer concurrentSalesTaxpayer = enterpriseBusinessInfoDTO.getEnterprise().getSalesTaxpayer();
        Integer taxpayerQualification = enterpriseBusinessInfoDTO.getEnterprise().getTaxpayerQualification();
        // 发票类型：
        Integer invoiceType = enterpriseBusinessInfoDTO.getEnterprise().getInvoiceType();

        // (1) 不是兼营销售纳税人；纳税人资格：一般纳税人；增值税税率固：固定0.09：
        if (Objects.equals(concurrentSalesTaxpayer, NOT_CONCURRENT_SALES_TAXPAYERS)
                && Objects.equals(taxpayerQualification, Taxpayer_Eligibility_GENERAL_TAXPAYERS)) {
            double VATRate = VAT_RATE_9.getTaxRate();
            for (EnterpriseBusinessTaxInfoDTO item : enterpriseBusinessList) {
                // 计算公式：
                double v = item.getAmount() / (1 + VATRate);
                BusinessTaxInfoDTO businessTaxInfoDTO = new BusinessTaxInfoDTO(item.getBusinessName(), VATRate, v);
                resultMap.put(item.getBusinessName(), businessTaxInfoDTO);

                log.info("【计算不含税营业额】------ 执行方案一，{} 计算出的总不含税营业额：{} ------", item.getBusinessName(), v);
            }

            // (2) 是兼营销售纳税人；纳税人资格：一般纳税人
        } else if (Objects.equals(concurrentSalesTaxpayer, IS_CONCURRENT_SALES_TAXPAYERS)
                && Objects.equals(taxpayerQualification, Taxpayer_Eligibility_GENERAL_TAXPAYERS)) {
            for (EnterpriseBusinessTaxInfoDTO item : enterpriseBusinessList) {
                // 计算公式：
                double v = item.getAmount() / (1 + item.getTaxRate().getTaxRate());
                BusinessTaxInfoDTO businessTaxInfoDTO = new BusinessTaxInfoDTO(item.getBusinessName(), item.getTaxRate().getTaxRate(), v);
                resultMap.put(item.getBusinessName(), businessTaxInfoDTO);

                log.info("【计算不含税营业额】------ 执行方案二，{} 计算出的总不含税营业额：{} ------", item.getBusinessName(), v);
            }

            // (3) 纳税人资格：小规模纳税人（不论兼营销售纳税人）；发票种类：专票
        } else if ((Objects.equals(concurrentSalesTaxpayer, IS_CONCURRENT_SALES_TAXPAYERS)
                || (Objects.equals(concurrentSalesTaxpayer, NOT_CONCURRENT_SALES_TAXPAYERS)))
                && Objects.equals(taxpayerQualification, Taxpayer_Eligibility_SMALL_SCALE_TAXPAYERS)
                && Objects.equals(invoiceType, INVOICE_TYPE_SPECIAL_TICKETS)) {
            double VATRate = VAT_RATE_3.getTaxRate();
            for (EnterpriseBusinessTaxInfoDTO item : enterpriseBusinessList) {
                double v = item.getAmount() / (1 + VATRate);
                BusinessTaxInfoDTO businessTaxInfoDTO = new BusinessTaxInfoDTO(item.getBusinessName(), VATRate, v);
                resultMap.put(item.getBusinessName(), businessTaxInfoDTO);

                log.info("【计算不含税营业额】------ 执行方案三，{} 计算出的总不含税营业额：{} ------", item.getBusinessName(), v);
            }

            // (4) 纳税人资格：小规模纳税人（不论兼营销售纳税人）；发票种类：普票
        } else if ((Objects.equals(concurrentSalesTaxpayer, IS_CONCURRENT_SALES_TAXPAYERS)
                || (Objects.equals(concurrentSalesTaxpayer, NOT_CONCURRENT_SALES_TAXPAYERS)))
                && Objects.equals(taxpayerQualification, Taxpayer_Eligibility_SMALL_SCALE_TAXPAYERS)
                && Objects.equals(invoiceType, INVOICE_TYPE_GENERAL_TICKETS)) {
            double VATRate = VAT_RATE_0.getTaxRate();
            for (EnterpriseBusinessTaxInfoDTO item : enterpriseBusinessList) {
                // 计算公式：
                double v = item.getAmount() / (1 + VATRate);
                BusinessTaxInfoDTO businessTaxInfoDTO = new BusinessTaxInfoDTO(item.getBusinessName(), VATRate, v);
                resultMap.put(item.getBusinessName(), businessTaxInfoDTO);

                log.info("【计算不含税营业额】------ 执行方案四，{} 计算出的总不含税营业额：{} ------", item.getBusinessName(), v);
            }

            // (5) 纳税人资格：小规模纳税人（不论兼营销售纳税人）；发票种类：专票 + 普票（则根据甲方资质为一般纳税人所占比例先计算该服务含专票的营业额）
            //     甲方资质选择的一般纳税人比例为50%
            //     专票增值税税率为3%，普票为0%计算出不含税营业额
        } else if ((Objects.equals(concurrentSalesTaxpayer, IS_CONCURRENT_SALES_TAXPAYERS)
                || (Objects.equals(concurrentSalesTaxpayer, NOT_CONCURRENT_SALES_TAXPAYERS)))
                && Objects.equals(taxpayerQualification, Taxpayer_Eligibility_SMALL_SCALE_TAXPAYERS)
                && Objects.equals(invoiceType, INVOICE_TYPE_MULTI_TICKETS)) {
            // 循环遍历当前企业的每一个业务信息：
            for (EnterpriseBusinessTaxInfoDTO item : enterpriseBusinessList) {
                double VATRate_0 = VAT_RATE_3.getTaxRate();
                double VATRate_1 = VAT_RATE_0.getTaxRate();
                // 获取企业的业务信息
                EnterpriseBusiness businessInfo = item.getEnterpriseBusiness();
                // 拿取每一个甲方资质占比：
                Double generalTaxpayerRatio = businessInfo.getGeneralTaxpayerRatio();           // 一般纳税人占比
                // Double smallscaleTaxpayerRatio = businessInfo.getSmallscaleTaxpayerRatio();  // 小规模纳税人占比
                Double SpecialTicketPartAmount = item.getAmount() * generalTaxpayerRatio;

                double v0 = SpecialTicketPartAmount / (1 + VATRate_0);
                BusinessTaxInfoDTO businessTaxInfoDTO_00 = new BusinessTaxInfoDTO(item.getBusinessName() + "一般纳税人", VATRate_0, v0);
                resultMap.put(item.getBusinessName(), businessTaxInfoDTO_00);
                log.info("【计算不含税营业额】------ 执行方案五，{} 计算出的总不含税营业额：{} ------", item.getBusinessName() + "一般纳税人", v0);

                double v1 = (item.getAmount() - SpecialTicketPartAmount) / (1 + VATRate_1);
                BusinessTaxInfoDTO businessTaxInfoDTO_01 = new BusinessTaxInfoDTO(item.getBusinessName() + "小规模纳税人", VATRate_1, v1);
                resultMap.put(item.getBusinessName(), businessTaxInfoDTO_01);
                log.info("【计算不含税营业额】------ 执行方案五，{} 计算出的总不含税营业额：{} ------", item.getBusinessName() + "小规模纳税人", v1);
            }
        } else {
            log.info("【计算不含税营业额】------计算条件有误，请检查后端计算算法，或是前端提交参数是否完整！！！--- {}", TimeUtils.getLocalTime());
        }
        return resultMap;
    }

    /**
     * 增值税销项税额
     * 计算公式：（各服务不含税营业额*其相对应的增值税税率）
     *
     * @param VATMap 计算不含税营业额返回的企业经营项目集合
     * @return Map（返回每一项经营业务的增值税销项税额）
     */
    public Map<String, Double> excludesVATOutputTax(Map<Object, BusinessTaxInfoDTO> VATMap) {
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<Object, BusinessTaxInfoDTO> entry : VATMap.entrySet()) {
            String mapKey = (String) entry.getKey();
            BusinessTaxInfoDTO mapValue = entry.getValue();
            // 计算公式：
            double v = mapValue.getAmount() * mapValue.getVATRate();
            result.put(mapKey, v);

            log.info("【增值税销项税额】------{} 计算得出增值税销项税额：{}------ ", mapKey, v);
        }
        return result;
    }

    /**
     * 计算成本的增值税进项税额
     * 需要根据不同的供应商计算结果
     *
     * @param enterpriseCostDTO 企业开销数据封装项
     * @return Map（每一项成本的增值税进项税额）
     */
    public Map<String, Double> excludesVATInputTax(EnterpriseCostDTO enterpriseCostDTO) {
        HashMap<String, Double> result = new HashMap<>();
        List<CostInfoDTO> costInfoList = enterpriseCostDTO.getCostInfoDTOList();
        for (CostInfoDTO item : costInfoList) {
            double v = calculateCost(item);
            result.put(item.getCostName(), v);
        }
        return result;
    }

    /**
     * 根据供应商计算当前成本的增值税进项税额
     *
     * @param item 当前成本的信息（包含供应商）
     * @return 增值税进项税额
     */
    private double calculateCost(CostInfoDTO item) {
        // 获取所有的供应商资质：
        List<SupplierProportionInfoDTO> supplierProportionInfoDTOS = item.getList();
        // 获取当前成本金额：
        double amount = item.getAmount();
        // 总增值税进项税额：
        double result = 0.0;
        // 计算供应商资质下对应的增值税进项税额：
        for (SupplierProportionInfoDTO proportionInfo : supplierProportionInfoDTOS) {
            // 当前纳税人所占比例：
            double percentage = proportionInfo.getProportion();
            // 获取当前纳税人的税率信息：
            double taxRate = proportionInfo.getTaxRate();
            // 计算：(各进项含税营业额*供应商资质中纳税人的比例)/(1+对应增值税税率)*对应增值税税率
            result += (amount * percentage) / (1 + taxRate) * taxRate;
        }
        return result;
    }

    /**
     * 增值税应纳税额
     * 计算公式：（增值税销项税额 - 增值税进项税额）：
     *
     * @param VATOutputTax 增值税销项税额
     * @param VATInputTax  增值税进项税额
     * @return double（增值税应纳税额）
     */
    public double excludesVATPayable(double VATOutputTax, double VATInputTax) {
        log.info("【计算增值税应纳税额】------ 增值税应纳税额计算得出： {} ------ ", VATOutputTax - VATInputTax);
        return VATOutputTax - VATInputTax;
    }

    /**
     * 公式：计算附加税（增值税应纳税额 * 相应附加税税率）
     * 附加税税率的种类有：
     * （1）一般企业的附加税税率为12%；
     * （2）小型微利企业：年应纳税所得额在300万以下，人员数量300人以下，总资产5000万以下的企业，现行正常附加税减半征收，即附加税*50%。
     */
    public double excludesSurTax(double VATPayable, Enterprise enterprise) {
        double tax;
        // 判断企业类型：
        Integer enterpriseType = enterprise.getEnterpriseType();
        if (Objects.equals(enterpriseType, TAX_FOR_GENERAL_ENTERPRISES)) {
            tax = TAX_RATE_FOR_GENERAL_ENTERPRISES;
        } else if (Objects.equals(enterpriseType, TAX_FOR_MICRO_PROFIT_ENTERPRISES)) {
            tax = TAX_RATE_FOR_MICRO_PROFIT_ENTERPRISES;
        } else {
            log.info("企业类型异常，附加税税率设置为1！！！");
            tax = 1;
        }
        return VATPayable * tax;
    }

    /**
     * 企业所得税应纳税额
     *
     * @param enterpriseBusinessInfoDTO 企业业务信息
     * @return double 企业所得税应纳税额
     */
    public double excludesTaxableIncome(EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO) {
        // 1.计算不含税营业额：
        double sumIn = 0;
        Map<Object, BusinessTaxInfoDTO> corporateVAT = excludesCorporateVAT(enterpriseBusinessInfoDTO);
        for (BusinessTaxInfoDTO value : corporateVAT.values()) {
            sumIn += value.getAmount();
        }
        // 2.计算支出合计金额：
        Double annualCost = enterpriseBusinessInfoDTO.getEnterprise().getAnnualCost();
        // 3.应纳税所得额：不含税营业额 - 成本费用:
        return sumIn - annualCost;
    }

    /**
     * 企业所得税税率额
     * 公式：企业所得税应纳税额”=“应纳税所得额”*12.5%*20%
     * 若“应纳税所得额”在100万（含）以下;“应纳税所得额”在100万（含）以下，减按12.5%计入应纳税所得额，按20%征收所得税税率，
     *
     * @param taxableIncome 应纳税所得额
     * @param enterpriseType 企业类型
     * @return double
     */
    public double excludesIncomeTaxIndeed(double taxableIncome, int enterpriseType) {
        double tax_1 = 1;
        double tax_2 = 1;
        if (taxableIncome <= 100 && taxableIncome > 0) {
            tax_1 = 0.125;
            tax_2 = 0.2;
        } else if (taxableIncome > 100 && taxableIncome <= 300) {
            tax_1 = 0.25;
            tax_2 = 0.2;
        } else if (taxableIncome > 300) {
            tax_1 = 0.25;
            if (enterpriseType == 2 || enterpriseType == 3) {
                tax_1 = 0.15;
            }
        }
        return taxableIncome * tax_1 * tax_2;
    }

    /**
     * 计算股东分红所得
     * “企业未分配利润”：=企业所得税的“应纳税所得额”-“企业所得税应纳税额”
     *
     * @param taxableIncome 企业所得税应纳税额
     * @param incomeTaxIndeed 企业所得税税率额
     * @return double
     */
    public double excludesUndistributedProfit(double taxableIncome, double incomeTaxIndeed) {
        return taxableIncome - incomeTaxIndeed;
    }

    /**
     * 股东个税
     * 公式：“股东个税”：=“股息红利所得税税率”*“企业未分配利润”
     *
     * @param undistributedProfit 计算股东分红所得
     * @return double
     */
    public double excludesShareholderPersonalTax(double undistributedProfit) {
        double tax = 0.2;
        return undistributedProfit * tax;
    }
}
