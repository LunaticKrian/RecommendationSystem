package com.mislab.core.systemcore.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("成本信息")
public class CostInfoVO {
    // id：
    private int id;

    // 成本费用类别：
    private String costName;

    // 成本花费：
    private double amount;

    // 供应商资质：
    private List<SupplierProportionInfoVO> list;
}
