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

    // 收入详细信息列表：(根据企业表示enterprise_key进行查询写入，表关联)
    private List<EnterpriseBusinessTaxVO> enterpriseBusinessList;
}
