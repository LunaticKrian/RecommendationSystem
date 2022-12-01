package com.mislab.core.systemcore.pojo.calculation.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "【计算使用】企业经营项目信息")
public class BusinessTaxInfoDTO {
    @ApiModelProperty(value = "企业经营项目名称")
    private String businessName;

    @ApiModelProperty(value = "税率")
    private double VATRate;

    @ApiModelProperty(value = "营业额")
    private double amount;
}
