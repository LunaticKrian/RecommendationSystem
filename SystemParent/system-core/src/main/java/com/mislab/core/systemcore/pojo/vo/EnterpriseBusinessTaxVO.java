package com.mislab.core.systemcore.pojo.vo;

import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.mislab.core.systemcore.pojo.entity.TaxRate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseBusinessTaxVO {
    // 业务名称：
    private String businessName;

    // 业务信息：
    private EnterpriseBusiness enterpriseBusiness;

    // 业务收入：
    private Double amount;

    // 业务税率：
    private TaxRate taxRate;
}
