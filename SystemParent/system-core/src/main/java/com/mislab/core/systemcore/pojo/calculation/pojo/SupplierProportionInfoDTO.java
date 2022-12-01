package com.mislab.core.systemcore.pojo.calculation.pojo;

import com.mislab.core.systemcore.pojo.entity.Industry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("【计算使用】供应商信息")
public class SupplierProportionInfoDTO {
    @ApiModelProperty(value = "供应商Id")
    private int id;

    @ApiModelProperty(value = "供应商类别")
    private String name;

    @ApiModelProperty(value = "供应商占比")
    private double proportion;

    @ApiModelProperty(value = "税率信息")
    private double taxRate;

    @ApiModelProperty(value = "行业信息")
    private Industry industry;

    @ApiModelProperty(value = "supId")
    private int supId;
}
