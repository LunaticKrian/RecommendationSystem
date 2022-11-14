package com.mislab.core.systemcore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mislab.core.systemcore.mapper.EnterpriseBusinessMapper;
import com.mislab.core.systemcore.mapper.EnterpriseMapper;
import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.pojo.vo.EnterpriseBusinessTaxVO;
import com.mislab.core.systemcore.pojo.vo.EnterpriseInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DataEncapsulationImpl {

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private EnterpriseBusinessMapper enterpriseBusinessMapper;


    public EnterpriseInfoVO getEnterpriseInfoVO(String enterpriseKey) {
        EnterpriseInfoVO enterpriseInfoVO = new EnterpriseInfoVO();

        Enterprise enterprise = enterpriseMapper.selectOne(new QueryWrapper<Enterprise>().eq("enterprise_key", enterpriseKey));
        enterpriseInfoVO.setEnterprise(enterprise);

        // 获取企业收入总金额：
        Double turnover = enterprise.getAnnualTurnover();

        List<EnterpriseBusinessTaxVO> enterpriseBusinessTaxVOList = getEnterpriseBusinessTaxVO(enterpriseKey);
        for (EnterpriseBusinessTaxVO item: enterpriseBusinessTaxVOList) {
            item.setAmount(turnover * item.getEnterpriseBusiness().getBusinessRatio());
        }

        enterpriseInfoVO.setEnterpriseBusinessList(enterpriseBusinessTaxVOList);
        return enterpriseInfoVO;
    }

    private List<EnterpriseBusinessTaxVO> getEnterpriseBusinessTaxVO(String enterpriseKey) {
        List<EnterpriseBusinessTaxVO> enterpriseBusinessInfoList = enterpriseBusinessMapper.getEnterpriseBusinessInfoList(enterpriseKey);
        log.info(enterpriseBusinessInfoList == null ? "查询企业经营项目信息失败！" : enterpriseBusinessInfoList.toString());
        return enterpriseBusinessInfoList;
    }
}
