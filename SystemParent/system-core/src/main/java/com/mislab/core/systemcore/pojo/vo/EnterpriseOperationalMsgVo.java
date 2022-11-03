package com.mislab.core.systemcore.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ascendable
 * @since 2022/10/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("企业经营情况Vo对象")
public class EnterpriseOperationalMsgVo {

    @ApiModelProperty(value = "企业唯一识别码")
    private String enterpriseKey;

    @ApiModelProperty(value = "兼营销售纳税人0 否 1 是")
    private Integer salesTaxpayer;

    @ApiModelProperty(value = "年营业额")
    private Double annualTurnover;

    @ApiModelProperty(value = "年经营成本")
    private Double annualCost;

    @ApiModelProperty(value = "成本费用信息")
    private List<CostVo> costVoList;

    @ApiModelProperty(value = "收入相关信息")
    private List<RevenueVo> revenueVoList;

}
