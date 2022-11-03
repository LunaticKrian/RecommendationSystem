package com.mislab.core.systemcore.service.impl;

import com.mislab.core.systemcore.mapper.BusinessMapper;
import com.mislab.core.systemcore.mapper.EnterpriseBusinessMapper;
import com.mislab.core.systemcore.pojo.vo.EnterpriseBusinessTaxVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 数据封装：
@Service
public class DataEncapsulationImpl {

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private EnterpriseBusinessMapper enterpriseBusinessMapper;

    // 封装 EnterpriseBusinessTaxVO：
    public EnterpriseBusinessTaxVO getEnterpriseBusinessTaxVOList(String enterpriseKey){
        // 通过Dao获取数据：
        EnterpriseBusinessTaxVO enterpriseBusinessInfoList = enterpriseBusinessMapper.getEnterpriseBusinessInfoList(enterpriseKey);

        return enterpriseBusinessInfoList;
    }
}
