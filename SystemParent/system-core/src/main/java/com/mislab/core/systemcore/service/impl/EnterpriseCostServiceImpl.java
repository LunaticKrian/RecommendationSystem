package com.mislab.core.systemcore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mislab.core.systemcore.mapper.EnterpriseCostMapper;
import com.mislab.core.systemcore.mapper.EnterpriseMapper;
import com.mislab.core.systemcore.pojo.dto.CostDto;
import com.mislab.core.systemcore.pojo.dto.CostRelatedDto;
import com.mislab.core.systemcore.pojo.dto.EnterpriseOperationalMsgDto;
import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.service.EnterpriseCostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EnterpriseCostServiceImpl extends ServiceImpl<EnterpriseCostMapper, CostDto> implements EnterpriseCostService {

    @Autowired
    private EnterpriseCostMapper enterpriseCostMapper;

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Override
    public EnterpriseOperationalMsgDto findEnterpriseCostInfoByKey(String enterpriseKey) {
        EnterpriseOperationalMsgDto enterpriseOperationalMsgDto = new EnterpriseOperationalMsgDto();
        List<CostRelatedDto> costInfo = new ArrayList<>();

        // 根据enterpriseKey获取企业信息：
        QueryWrapper<Enterprise> queryWrapper = new QueryWrapper<Enterprise>().eq("enterprise_key", enterpriseKey);
        Enterprise enterprise = enterpriseMapper.selectOne(queryWrapper);

        // 拆分数据：字符串对象转换为List集合
        List<Integer> costIdList = JSONObject.parseArray(enterprise.getCostType(), Integer.class);

        // 获取所有的支出详细信息：
        for (Integer id: costIdList) {
            CostRelatedDto cost = enterpriseCostMapper.findCostById(id);
            costInfo.add(cost);
        }
        enterpriseOperationalMsgDto.setCostRelatedList(costInfo);

        return enterpriseOperationalMsgDto;
    }
}
