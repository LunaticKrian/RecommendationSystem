package com.mislab.core.systemcore.pojo.vo;

import com.mislab.core.systemcore.pojo.entity.Enterprise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseInfoVO {
    // 企业信息：
    private Enterprise enterprise;

    // 经营业务详细信息列表：
    private List<EnterpriseBusinessTaxVO> enterpriseBusinessList;
}
