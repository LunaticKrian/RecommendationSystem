package com.mislab.core.systemcore.pojo.dto;

import com.mislab.core.systemcore.pojo.entity.Cost;
import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.mislab.core.systemcore.pojo.jsonDomain.SupplierProportion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel("第二张表的所需数据")
public class Enterprise2VO {
    //int salesTaxpayer, double annualTurnover, double annualCost, String enterpriseKey, List<EnterpriseBusiness> EPBs, List<Cost> costs
    @ApiModelProperty(value = "兼营销售纳税人0 否 1 是")
    private Integer salesTaxpayer;

    @ApiModelProperty(value = "年营业额")
    private Double annualTurnover;

    @ApiModelProperty(value = "年经营成本")
    private Double annualCost;

    @ApiModelProperty(value = "业务营业额")
    List<EnterpriseBusiness> EPBs;

    @ApiModelProperty(value = "业务营业额")
    List<Cost> costs;

    @ApiModelProperty(value = "costJson")
    List<SupplierProportion> supplierProportions;

    @ApiModelProperty(value = "enterpriseKey")
    String enterpriseKey;
}
