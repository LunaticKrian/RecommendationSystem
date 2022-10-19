package com.mislab.core.systemcore.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("企业对应成本信息")
public class EnterpriseCostVO {

    // 企业基本信息；
    private EnterpriseBasicMsgVo enterpriseBasicMsgVo;

    // 企业对应的成本花费信息：
    private List<CostInfoVO> costInfoVOList;

}
