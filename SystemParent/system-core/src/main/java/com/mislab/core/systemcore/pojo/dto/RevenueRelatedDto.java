package com.mislab.core.systemcore.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author Ascendable
 * @since 2022/10/20
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="收入相关信息", description="")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueRelatedDto {

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "占比")
    private Double businessRatio;

    @ApiModelProperty(value = "单项金额")
    private Double amount;

    @ApiModelProperty(value = "一般纳税人")
    private Double generalTaxpayerRatio;

    @ApiModelProperty(value = "小规模纳税人")
    private Double smallscaleTaxpayerRatio;

    @ApiModelProperty(value = "自然人")
    private Double naturalPersonRatio;

}
