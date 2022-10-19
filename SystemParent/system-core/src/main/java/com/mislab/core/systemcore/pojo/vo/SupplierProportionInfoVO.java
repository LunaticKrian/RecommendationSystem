package com.mislab.core.systemcore.pojo.vo;

import com.mislab.core.systemcore.pojo.entity.Industry;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("供应商信息")
public class SupplierProportionInfoVO {
    // 供应商id：
    private int id;

    // 供应商类别：
    private String name;

    // 税率信息：
    private double taxRate;

    // 行业信息：
    private Industry industry;
}
