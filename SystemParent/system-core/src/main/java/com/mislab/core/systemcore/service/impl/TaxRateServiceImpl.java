package com.mislab.core.systemcore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mislab.common.utils.TimeUtils;
import com.mislab.core.systemcore.mapper.TaxRateMapper;
import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.mislab.core.systemcore.pojo.entity.TaxRate;
import com.mislab.core.systemcore.pojo.vo.*;
import com.mislab.core.systemcore.service.EnterpriseBusinessService;
import com.mislab.core.systemcore.service.EnterpriseService;
import com.mislab.core.systemcore.service.TaxRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
public class TaxRateServiceImpl extends ServiceImpl<TaxRateMapper, TaxRate> implements TaxRateService {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private EnterpriseBusinessService enterpriseBusinessService;

    // 计算“不含税营业额”
    public Map<Object, BusinessTaxVO> excludesCorporateVAT(EnterpriseInfoVO enterpriseInfoVO) {
        // 存放各项经营的不含税营业额：
        HashMap<Object, BusinessTaxVO> resultMap = new HashMap<>();

        // 获取所有经营业务信息：
        List<EnterpriseBusinessTaxVO> enterpriseBusinessList = enterpriseInfoVO.getEnterpriseBusinessList();

        // 计算“不含税营业额”：各服务营业额/(1+增值税税率)：
        // 表一获取的基本信息：
        // 兼营销纳税人：
        Integer salesTaxpayer = enterpriseInfoVO.getEnterprise().getSalesTaxpayer();
        // 纳税人资格：
        Integer taxpayerQualification = enterpriseInfoVO.getEnterprise().getTaxpayerQualification();
        // 发票类型：
        Integer invoiceType = enterpriseInfoVO.getEnterprise().getInvoiceType();

        // “兼营销售纳税人”选择为“否”，“纳税人资格”为一般纳税人，则“增值税税率”固定为9%：
        if (salesTaxpayer == 0 && taxpayerQualification == 0) {
            // 增值税税率
            double VATRate = 0.09;
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                double v = item.getAmount() / (1 + VATRate);
                BusinessTaxVO businessTaxVO = new BusinessTaxVO(item.getBusinessName(), VATRate, v);
                resultMap.put(item.getBusinessName(), businessTaxVO);
                log.info("------ 执行方案一，{} 计算出的总不含税营业额：{} ------", item.getBusinessName(), v);
            }
        } else if (salesTaxpayer == 1 && taxpayerQualification == 0) {
            // “是否兼营销售纳税人”选择为“是”且在表1中“纳税人资格”为一般纳税人
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                double v = item.getAmount() / (1 + item.getTaxRate().getTaxRate());
                BusinessTaxVO businessTaxVO = new BusinessTaxVO(item.getBusinessName(), item.getTaxRate().getTaxRate(), v);
                resultMap.put(item.getBusinessName(), businessTaxVO);
                log.info("------ 执行方案二，{} 计算出的总不含税营业额：{} ------", item.getBusinessName(), v);
            }
        } else if (salesTaxpayer == 1 && taxpayerQualification == 1 && invoiceType == 0) {
            // “纳税人资格”(taxpayerQualification)为小规模纳税人且“发票种类” (invoice_type) (为专票
            // 增值税税率
            double VATRate = 0.03;
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                double v = item.getAmount() / (1 + VATRate);
                BusinessTaxVO businessTaxVO = new BusinessTaxVO(item.getBusinessName(), VATRate, v);
                resultMap.put(item.getBusinessName(), businessTaxVO);
                log.info("------ 执行方案三，{} 计算出的总不含税营业额：{} ------", item.getBusinessName(), v);
            }
        } else if (salesTaxpayer == 1 && taxpayerQualification == 1 && invoiceType == 1) {
            // 若为普票，则税率为0%（未来这个数值可能会变动）
            double VATRate = 0.0;
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                double v = item.getAmount() / (1 + VATRate);
                BusinessTaxVO businessTaxVO = new BusinessTaxVO(item.getBusinessName(), VATRate, v);
                resultMap.put(item.getBusinessName(), businessTaxVO);
                log.info("------ 执行方案四，{} 计算出的总不含税营业额：{} ------", item.getBusinessName(), v);
            }
        } else if (salesTaxpayer == 1 && taxpayerQualification == 1 && invoiceType == 2) {
            // TODO: 若为专票+普票，则根据甲方资质为一般纳税人所占比例先计算该服务含专票的营业额（
            //  公式为：该服务营业额*所对应甲方资质中选择一般纳税人的比
            //  甲方资质选择的一般纳税人比例为50%，则含专票的营业额为500*50%=250），
            //  再根据专票增值税税率为3%，普票为0%计算出不含税营业额（计算公式：含专票营业额/(1+3%)+(该服务营业额-含专票营业额)/(1+0%)

            // 循环遍历当前企业的每一个业务信息：
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                double VATRate_0 = 0.03;
                double VATRate_1 = 0.0;

                // 获取企业的业务信息
                EnterpriseBusiness businessInfo = item.getEnterpriseBusiness();
                // 拿取每一个甲方资质占比：
                Double generalTaxpayerRatio = businessInfo.getGeneralTaxpayerRatio();           // 一般纳税人占比
                // Double smallscaleTaxpayerRatio = businessInfo.getSmallscaleTaxpayerRatio();  // 小规模纳税人占比
                // TODO: 分类计算
                Double SpecialTicketPartAmount = item.getAmount() * generalTaxpayerRatio;

                // 计算不含税营业：
                double v0 = SpecialTicketPartAmount / (1 + VATRate_0);
                BusinessTaxVO businessTaxVO_00 = new BusinessTaxVO(item.getBusinessName() + "一般纳税人", VATRate_0, v0);
                resultMap.put(item.getBusinessName(), businessTaxVO_00);
                log.info("------ 执行方案五，{} 计算出的总不含税营业额：{} ------", item.getBusinessName() + "一般纳税人", v0);

                double v1 = (item.getAmount() - SpecialTicketPartAmount) / (1 + VATRate_1);
                BusinessTaxVO businessTaxVO_01 = new BusinessTaxVO(item.getBusinessName() + "小规模纳税人", VATRate_1, v1);
                resultMap.put(item.getBusinessName(), businessTaxVO_01);
                log.info("------ 执行方案五，{} 计算出的总不含税营业额：{} ------", item.getBusinessName() + "小规模纳税人", v1);
            }
        } else {
            log.info("计算条件有误，请检查后端计算算法，或是前端提交参数是否完整！！！--- {}", TimeUtils.getLocalTime());
        }
        return resultMap;
    }

    // 增值税销项税额:（各服务不含税营业额*其相对应的增值税税率）
    public Map<String, Double> VATOutputTax(Map<Object, Object> VATMap) {
        // 存放每一个经营项目的增值税销项税额：
        Map<String, Double> result = new HashMap<>();

        for (Map.Entry<Object, Object> entry : VATMap.entrySet()) {
            // 获取经营业务名称：
            String mapKey = (String) entry.getKey();
            // 获取封装对象：
            BusinessTaxVO mapValue = (BusinessTaxVO) entry.getValue();

            // 计算对应的增值税销项税额：
            double v = mapValue.getAmount() * mapValue.getVATRate();
            result.put(mapKey, v);

            log.info("{} 计算得出增值税销项税额：{}", mapKey, v);
        }
        return result;
    }

    // 计算“增值税进项税额”:
    public Map<String, Double> VATInputTax(EnterpriseCostVO enterpriseCostVO) {
        // 创建一个Map存放计算信息：
        HashMap<String, Double> resultDetail = new HashMap<>();

        // 遍历，获取所有的成本信息：
        List<CostInfoVO> costInfoList = enterpriseCostVO.getCostInfoVOList();

        // 计算所有成本项目总和：
        double sum = 0.0;
        for (CostInfoVO item : costInfoList) {
            sum += CalculateCost(item);
            resultDetail.put(item.getCostName(), sum);
        }
        return resultDetail;
    }

    // TODO: 计算成本：
    private double CalculateCost(CostInfoVO item) {
        // 获取所有的供应商资质：
        List<SupplierProportionInfoVO> supplierProportionInfoVOS = item.getList();
        // 获取当前成本金额：
        double amount = item.getAmount();
        // 总增值税进项税额：
        double result = 0.0;
        // 计算供应商资质下对应的增值税进项税额：
        for (SupplierProportionInfoVO proportionInfo : supplierProportionInfoVOS) {
            // 当前纳税人所占比例：
            double percentage = proportionInfo.getPercentage();
            // 获取当前纳税人的税率信息：
            double taxRate = proportionInfo.getTaxRate();
            // 计算：(各进项含税营业额*供应商资质中纳税人的比例)/(1+对应增值税税率)*对应增值税税率
            result += (amount * percentage) / (1 + taxRate) * taxRate;
        }
        return result;
    }

    // 增值税应纳税额（增值税销项税额”-“增值税进项税额）：
    public Map<String, Double> VATPayable(Map<String, Double> VATOutputTax, Map<String, Double> VATInputTax) {
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Double> entry : VATOutputTax.entrySet()) {
            String mapKey = entry.getKey();
            double output = entry.getValue();
            double input = VATInputTax.get(mapKey);
            double v = output - input;
            result.put(mapKey, v);
            log.info("{} 增值税应纳税额计算得出： {}", mapKey, v);
        }
        return result;
    }

    // 计算附加税（增值税应纳税额 * 相应附加税税率）：

    /**
     * 附加税税率的种类有：
     * （1）一般企业的附加税税率为12%；
     * （2）小型微利企业：年应纳税所得额在300万以下，人员数量300人以下，总资产5000万以下的企业，现行正常附加税减半征收，即附加税*50%。
     */
    public Map<Object, Double> surtax(Map<String, Double> VATPayable, Enterprise enterprise) {
        double tax = 0.0;

        HashMap<Object, Double> result = new HashMap<>();

        // 判断企业类型：
        Integer enterpriseType = enterprise.getEnterpriseType();
        if (enterpriseType == 1) {
            tax = 0.12;
        } else if (enterpriseType == 2) {
            tax = 0.06;
        } else {
            tax = 1;
            log.info("企业类型异常，附加税税率设置为1！！！");
        }

        for (Map.Entry<String, Double> entry : VATPayable.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue() * tax;

            result.put(key, value);
        }
        return result;
    }

    // 企业所得税应纳税额:
    public double taxableIncome(EnterpriseInfoVO enterpriseInfoVO) {
        // 1.计算不含税营业额：
        double sumIn = 0;
        Map<Object, BusinessTaxVO> corporateVAT = excludesCorporateVAT(enterpriseInfoVO);
        for (BusinessTaxVO value : corporateVAT.values()) {
            sumIn += value.getAmount();
        }

        // 2.计算支出合计金额：
        double sumOut = 0;
        List<EnterpriseBusinessTaxVO> list = enterpriseInfoVO.getEnterpriseBusinessList();
        for (EnterpriseBusinessTaxVO item : list) {
            sumOut += item.getAmount();
        }

        // 3.应纳税所得额：不含税营业额 - 成本费用:
        return sumIn - sumOut;
    }

    // 企业所得税税率：
    // 若“应纳税所得额”在100万（含）以下;“应纳税所得额”在100万（含）以下，减按12.5%计入应纳税所得额，按20%征收所得税税率，
    // 企业所得税应纳税额”=“应纳税所得额”*12.5%*20%
    public double incomeTaxIndeed(double taxableIncome, int enterpriseType) {
        double tax_1 = 1;
        double tax_2 = 1;
        if (taxableIncome <= 1000000 && taxableIncome > 0) {
            tax_1 = 0.125;
            tax_2 = 0.2;
        } else if (taxableIncome > 1000000 && taxableIncome <= 3000000) {
            tax_1 = 0.25;
            tax_2 = 0.2;
        } else if (taxableIncome > 3000000) {
            tax_1 = 0.25;
            if (enterpriseType == 2 || enterpriseType == 3) {
                tax_1 = 0.15;
            }
        }
        return taxableIncome * tax_1 * tax_2;
    }

    // 计算股东分红所得:

    // “企业未分配利润”：=企业所得税的“应纳税所得额”-“企业所得税应纳税额”
    public double undistributedProfit(double taxableIncome, double incomeTaxIndeed) {
        return taxableIncome - incomeTaxIndeed;
    }

    // 股东个税，“股东个税”：=“股息红利所得税税率”*“企业未分配利润”
    public double shareholderPersonalTax(double undistributedProfit) {
        double tax = 0.2;
        return undistributedProfit * tax;
    }
}
