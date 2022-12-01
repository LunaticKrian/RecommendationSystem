package com.mislab.core.systemcore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mislab.core.systemcore.pojo.dto.CostDto;
import com.mislab.core.systemcore.pojo.dto.EnterpriseOperationalMsgDto;

public interface EnterpriseCostService extends IService<CostDto> {

    /**
     * 封装企业花费信息
     * @param enterpriseKey 企业唯一标识
     * @return EnterpriseOperationalMsgDto
     */
    EnterpriseOperationalMsgDto findEnterpriseCostInfoByKey(String enterpriseKey);
}
