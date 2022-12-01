package com.mislab.core.systemcore.pojo.calculation.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("【计算使用】成本信息")
public class CostInfoDTO {
    @ApiModelProperty(value = "成本ID")
    private int id;

    @ApiModelProperty(value = "成本费用类别")
    private String costName;

    @ApiModelProperty(value = "成本占比")
    private Double costRatio;

    @ApiModelProperty(value = "成本花费")
    private double amount;

    @ApiModelProperty(value = "供应商资质")
    private List<SupplierProportionInfoDTO> list;
}
