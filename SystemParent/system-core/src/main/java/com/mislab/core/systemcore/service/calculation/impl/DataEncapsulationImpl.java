package com.mislab.core.systemcore.service.calculation.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mislab.core.systemcore.mapper.EnterpriseBusinessMapper;
import com.mislab.core.systemcore.mapper.EnterpriseCostMapper;
import com.mislab.core.systemcore.mapper.EnterpriseMapper;
import com.mislab.core.systemcore.mapper.SupplierQualificationMapper;
import com.mislab.core.systemcore.pojo.calculation.pojo.*;
import com.mislab.core.systemcore.pojo.entity.Cost;
import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.pojo.entity.SupplierQualification;
import com.mislab.core.systemcore.service.calculation.DataEncapsulation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DataEncapsulationImpl implements DataEncapsulation {
    @Autowired
    private EnterpriseMapper enterpriseMapper;
    @Autowired
    private EnterpriseBusinessMapper enterpriseBusinessMapper;

    @Autowired
    private EnterpriseCostMapper enterpriseCostMapper;

    @Autowired
    private SupplierQualificationMapper supplierQualificationMapper;

    /**
     * 封装企业经营项目信息
     *
     * @param enterpriseKey 企业唯一标识
     * @return EnterpriseBusinessInfoDTO 企业基本信息和经营项目信息
     */
    public EnterpriseBusinessInfoDTO getEnterpriseInfoDTO(String enterpriseKey) {
        EnterpriseBusinessInfoDTO enterpriseBusinessInfoDTO = new EnterpriseBusinessInfoDTO();
        Enterprise enterprise = enterpriseMapper.selectOne(new QueryWrapper<Enterprise>().eq("enterprise_key", enterpriseKey));
        enterpriseBusinessInfoDTO.setEnterprise(enterprise);

        Double turnover = enterprise.getAnnualTurnover();
        List<EnterpriseBusinessTaxInfoDTO> enterpriseBusinessTaxInfoDTOList = getEnterpriseBusinessTaxDTO(enterpriseKey);
        for (EnterpriseBusinessTaxInfoDTO item : enterpriseBusinessTaxInfoDTOList) {
            item.setAmount(turnover * item.getEnterpriseBusiness().getBusinessRatio());
        }
        enterpriseBusinessInfoDTO.setEnterpriseBusinessList(enterpriseBusinessTaxInfoDTOList);
        return enterpriseBusinessInfoDTO;
    }

    /**
     * 获取企业经营项目信息
     *
     * @param enterpriseKey 企业唯一标识
     * @return List企业经营信息
     */
    private List<EnterpriseBusinessTaxInfoDTO> getEnterpriseBusinessTaxDTO(String enterpriseKey) {
        List<EnterpriseBusinessTaxInfoDTO> enterpriseBusinessInfoList = enterpriseBusinessMapper.getEnterpriseBusinessInfoList(enterpriseKey);
        log.info(enterpriseBusinessInfoList == null ? "查询企业经营项目信息失败！" : enterpriseBusinessInfoList.toString());
        return enterpriseBusinessInfoList;
    }

    /**
     * 获取企业成本花销项目信息
     *
     * @param enterpriseKey 企业唯一标识
     * @return EnterpriseCostDTO 企业基本信息和成本花销项目信息
     */
    public EnterpriseCostDTO getEnterpriseCostDTO(String enterpriseKey) {
        EnterpriseCostDTO enterpriseCostDTO = new EnterpriseCostDTO();
        Enterprise enterprise = enterpriseMapper.selectOne(new QueryWrapper<Enterprise>().eq("enterprise_key", enterpriseKey));
        enterpriseCostDTO.setEnterprise(enterprise);
        // 获取开销总金额：
        Double annualCost = enterprise.getAnnualCost();

        // 拆分数据：字符串对象转换为List集合
        List<Integer> costIdList = JSONObject.parseArray(enterprise.getCostType(), Integer.class);
        List<CostInfoDTO> costInfoDTOS = new ArrayList<>();
        for (Integer id : costIdList) {
            Cost cost = enterpriseCostMapper.findCostInfoById(id);
            List<SupplierProportionInfoDTO> qualifications = new ArrayList<>();
            // 解析供应商信息：
            List<String> SupplierProportionStrings = JSONObject.parseArray(cost.getSupplierProportion(), String.class);
            for (String item : SupplierProportionStrings) {
                SupplierProportionInfoDTO supplier = JSONObject.parseObject(item, SupplierProportionInfoDTO.class);
                // 根据供应商Id查询数据：
                SupplierQualification qualification = supplierQualificationMapper.selectOne(new QueryWrapper<SupplierQualification>().eq("id", supplier.getSupId()));
                supplier.setName(qualification.getName());
                qualifications.add(supplier);
            }
            CostInfoDTO costInfoDTO = new CostInfoDTO();
            costInfoDTO.setCostName(cost.getName());
            costInfoDTO.setAmount(annualCost * cost.getCostRatio());
            costInfoDTO.setCostRatio(cost.getCostRatio());
            costInfoDTO.setList(qualifications);
            costInfoDTOS.add(costInfoDTO);
        }
        enterpriseCostDTO.setCostInfoDTOList(costInfoDTOS);
        return enterpriseCostDTO;
    }
}
