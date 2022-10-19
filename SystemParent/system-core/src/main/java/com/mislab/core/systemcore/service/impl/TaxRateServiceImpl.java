package com.mislab.core.systemcore.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mislab.common.utils.TimeUtils;
import com.mislab.core.systemcore.mapper.TaxRateMapper;
import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.mislab.core.systemcore.pojo.entity.TaxRate;
import com.mislab.core.systemcore.pojo.vo.*;
import com.mislab.core.systemcore.service.EnterpriseBusinessService;
import com.mislab.core.systemcore.service.EnterpriseService;
import com.mislab.core.systemcore.service.TaxRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // TODO：1、计算企业增值税：
    // TODO:2、计算“增值税销项税额”
    // 关联计算：
    public Map<Object, Object> ExcludesCorporateVAT(EnterpriseInfoVO enterpriseInfoVO) {
        // 总不含税营业额：
        double TotalTurnoverExcludingTax = 0.0;
        // 增值税销项税额:
        double TotalVATOutputTax = 0.0;

        HashMap<Object, Object> resultMap = new HashMap<>();

        // 拿取总金额：
        Double annualTurnover = enterpriseInfoVO.getEnterprise().getAnnualTurnover();
        List<EnterpriseBusinessTaxVO> enterpriseBusinessList = enterpriseInfoVO.getEnterpriseBusinessList();

        // 遍历列表计算所有的每一项收入的金额：
        for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
            Double businessRatio = item.getEnterpriseBusiness().getBusinessRatio();
            item.setAmount(annualTurnover * businessRatio);

            log.info("------ {} 计算出的单项营业额：{} ------", item.getBusinessName(), annualTurnover * businessRatio);
        }

        // 计算“不含税营业额”：各服务营业额/(1+增值税税率)之和：
        // 兼营销纳税人：
        Integer salesTaxpayer = enterpriseInfoVO.getEnterprise().getSalesTaxpayer();
        // 纳税人资格：
        Integer taxpayerQualification = enterpriseInfoVO.getEnterprise().getTaxpayerQualification();
        // 发票类型：
        Integer invoiceType = enterpriseInfoVO.getEnterprise().getInvoiceType();

        // “是否兼营销售纳税人”选择为“否”，“纳税人资格”为一般纳税人，则“增值税税率”固定为9%：
        if (salesTaxpayer == 0 && taxpayerQualification == 0) {
            // 增值税税率
            double VATRate = 0.09;
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                double v = item.getAmount() / (1 + VATRate);
                TotalTurnoverExcludingTax += v;
                TotalVATOutputTax += (v * VATRate);

            }
            log.info("------ 执行方案一，计算出的总不含税营业额：{} ------", TotalTurnoverExcludingTax);

        } else if (salesTaxpayer == 1 && taxpayerQualification == 0) {
            // “是否兼营销售纳税人”选择为“是”且在表1中“纳税人资格”为一般纳税人
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                double v = item.getAmount() / (1 + item.getTaxRate().getTaxRate());
                TotalTurnoverExcludingTax += v;
                TotalVATOutputTax += (v * item.getTaxRate().getTaxRate());
            }
            log.info("------ 执行方案二，计算出的总不含税营业额：{} ------", TotalTurnoverExcludingTax);

        } else if (salesTaxpayer == 1 && taxpayerQualification == 1 && invoiceType == 0) {
            // “纳税人资格”(taxpayerQualification)为小规模纳税人且“发票种类” (invoice_type) (为专票
            // 增值税税率
            double VATRate = 0.03;
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                double v = item.getAmount() / (1 + VATRate);
                TotalTurnoverExcludingTax += v;
                TotalVATOutputTax += (v * VATRate);
            }
            log.info("------ 执行方案三，计算出的总不含税营业额：{} ------", TotalTurnoverExcludingTax);

        } else if (salesTaxpayer == 1 && taxpayerQualification == 1 && invoiceType == 1) {
            // 若为普票，则税率为0%（未来这个数值可能会变动）
            double VATRate = 0.0;
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                double v = item.getAmount() / (1 + VATRate);
                TotalTurnoverExcludingTax += v;
                TotalVATOutputTax += (v * VATRate);
            }
            log.info("------ 执行方案四，计算出的总不含税营业额：{} ------", TotalTurnoverExcludingTax);
        } else if (salesTaxpayer == 1 && taxpayerQualification == 1 && invoiceType == 2) {
            // TODO: 1、若为专票+普票，则根据甲方资质为一般纳税人所占比例先计算该服务含专票的营业额（
            //  公式为：该服务营业额*所对应甲方资质中选择一般纳税人的比
            //  甲方资质选择的一般纳税人比例为50%，则含专票的营业额为500*50%=250），
            //  再根据专票增值税税率为3%，普票为0%计算出不含税营业额（计算公式：含专票营业额/(1+3%)+(该服务营业额-含专票营业额)/(1+0%)

            // 循环遍历当前企业的每一个业务信息：
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {

                double VATRate = 0.03;

                // 获取企业的业务信息
                EnterpriseBusiness businessInfo = item.getEnterpriseBusiness();
                // 拿取每一个甲方资质占比：
                Double generalTaxpayerRatio = businessInfo.getGeneralTaxpayerRatio();// 一般纳税人占比
                Double smallscaleTaxpayerRatio = businessInfo.getSmallscaleTaxpayerRatio();  // 小规模纳税人占比
                // TODO: 分类计算
                Double SpecialTicketPartAmount = item.getAmount() * generalTaxpayerRatio;
                // 计算不含税营业：
                double v = SpecialTicketPartAmount * (1 + VATRate) + (item.getAmount() - SpecialTicketPartAmount);
                TotalTurnoverExcludingTax += v;
                TotalVATOutputTax += (SpecialTicketPartAmount * (1 + VATRate) * VATRate) + (item.getAmount() - SpecialTicketPartAmount) * 0;
            }
        } else {
            log.info("计算条件有误，请检查后端计算算法，或是前端提交参数是否完整！！！--- {}", TimeUtils.getLocalTime());
        }

        resultMap.put("不含税营业额", TotalTurnoverExcludingTax);
        resultMap.put("增值税销项税额", TotalVATOutputTax);
        log.info("------ 最终计算结果不含税营业额：{} ------", TotalTurnoverExcludingTax);
        log.info("------ 最终计算结果增值税销项税额：{} ------", TotalVATOutputTax);
        return resultMap;
    }


    // TODO:3、计算“增值税进项税额”:
    public Map<Object, Object> VATInputTax(EnterpriseCostVO enterpriseCostVO) {
        // 遍历，获取所有的成本信息：
        List<CostInfoVO> costInfoList = enterpriseCostVO.getCostInfoVOList();
        for (CostInfoVO item : costInfoList) {
            switch (item.getId()) {
                case 1:
                    double result = CalculateVehicleCost(item);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }
        return null;
    }

    // TODO: 计算车辆成本：
    private double CalculateVehicleCost(CostInfoVO item) {
        // 获取所有的供应商资质：
        List<SupplierProportionInfoVO> supplierProportionInfoVOS = item.getList();
        for (SupplierProportionInfoVO ProportionInfo : supplierProportionInfoVOS) {
            switch (ProportionInfo.getId()) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                default:
                    // 报出异常
            }
        }
        return 0.0;
    }

    // TODO：计算人工成本：

    // TODO：计算办公成本：

    // TODO：计算运输（油费）成本：

    // TODO：计算运输（路桥费）成本

}
