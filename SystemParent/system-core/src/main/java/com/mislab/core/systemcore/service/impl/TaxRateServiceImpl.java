package com.mislab.core.systemcore.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mislab.core.systemcore.mapper.TaxRateMapper;
import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.mislab.core.systemcore.pojo.entity.TaxRate;
import com.mislab.core.systemcore.pojo.vo.EnterpriseBusinessTaxVO;
import com.mislab.core.systemcore.pojo.vo.EnterpriseInfoVO;
import com.mislab.core.systemcore.service.EnterpriseBusinessService;
import com.mislab.core.systemcore.service.EnterpriseService;
import com.mislab.core.systemcore.service.TaxRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // TODO：计算企业增值税：
    public Map<Object, Object> ExcludesCorporateVAT(EnterpriseInfoVO enterpriseInfoVO) {
        // 总不含税营业额：
        double TotalTurnoverExcludingTax = 0.0;
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
                TotalTurnoverExcludingTax += item.getAmount() / (1 + VATRate);
            }
            log.info("------ 执行方案一，计算出的总不含税营业额：{} ------", TotalTurnoverExcludingTax);

        } else if (salesTaxpayer == 1 && taxpayerQualification == 0) {
            // “是否兼营销售纳税人”选择为“是”且在表1中“纳税人资格”为一般纳税人
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                TotalTurnoverExcludingTax += item.getAmount() / (1 + item.getTaxRate().getTaxRate());
            }
            log.info("------ 执行方案二，计算出的总不含税营业额：{} ------", TotalTurnoverExcludingTax);

        } else if (salesTaxpayer == 1 && taxpayerQualification == 1 && invoiceType == 0) {
            // “纳税人资格”(taxpayerQualification)为小规模纳税人且“发票种类” (invoice_type) (为专票
            // 增值税税率
            double VATRate = 0.03;
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                TotalTurnoverExcludingTax += item.getAmount() / (1 + VATRate);
            }
            log.info("------ 执行方案三，计算出的总不含税营业额：{} ------", TotalTurnoverExcludingTax);

        } else if (salesTaxpayer == 1 && taxpayerQualification == 1 && invoiceType == 1) {
            // 若为普票，则税率为0%（未来这个数值可能会变动）
            double VATRate = 0.0;
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                TotalTurnoverExcludingTax += item.getAmount() / (1 + VATRate);
            }
            log.info("------ 执行方案四，计算出的总不含税营业额：{} ------", TotalTurnoverExcludingTax);
        } else if (salesTaxpayer == 1 && taxpayerQualification == 1 && invoiceType == 2) {
            // TODO: 1、若为专票+普票，则根据甲方资质为一般纳税人所占比例先计算该服务含专票的营业额（
            //  公式为：该服务营业额*所对应甲方资质中选择一般纳税人的比
            //  甲方资质选择的一般纳税人比例为50%，则含专票的营业额为500*50%=250），
            //  再根据专票增值税税率为3%，普票为0%计算出不含税营业额（计算公式：含专票营业额/(1+3%)+(该服务营业额-含专票营业额)/(1+0%)

            // 循环遍历当前企业的每一个业务信息：
            for (EnterpriseBusinessTaxVO item : enterpriseBusinessList) {
                // 获取企业的业务信息
                EnterpriseBusiness businessInfo = item.getEnterpriseBusiness();
                // 拿取每一个甲方资质占比：
                Double generalTaxpayerRatio = businessInfo.getGeneralTaxpayerRatio();// 一般纳税人占比
                Double smallscaleTaxpayerRatio = businessInfo.getSmallscaleTaxpayerRatio();  // 小规模纳税人占比
                // TODO: 分类计算
            }


        }

        resultMap.put("不含税营业额", TotalTurnoverExcludingTax);
        log.info("------ 最终计算结果不含税营业额：{} ------", TotalTurnoverExcludingTax);
        return resultMap;
    }


}
