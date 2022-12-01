package com.mislab.core.systemcore.service.calculation;

import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseBusinessInfoDTO;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseCostDTO;

public interface DataEncapsulation {
    /**
     * 封装企业经营项目信息
     *
     * @param enterpriseKey 企业唯一标识
     * @return EnterpriseBusinessInfoDTO 企业基本信息和经营项目信息
     */
    public EnterpriseBusinessInfoDTO getEnterpriseInfoDTO(String enterpriseKey);

    /**
     * 获取企业成本花销项目信息
     *
     * @param enterpriseKey 企业唯一标识
     * @return EnterpriseCostDTO 企业基本信息和成本花销项目信息
     */
    public EnterpriseCostDTO getEnterpriseCostDTO(String enterpriseKey);
}
