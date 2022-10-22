package com.mislab.core.systemcore.pojo.vo;

import com.mislab.core.systemcore.pojo.jsonDomain.SupplierProportion;
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
@ApiModel("成本相关信息")
@Builder
public class CostVo {

    @ApiModelProperty(value = "成本类别名称")
    private String name;

    @ApiModelProperty(value = "成本占比")
    private Double CostRatio;

    @ApiModelProperty(value = "单项金额")
    private Double amount;

    @ApiModelProperty(value = "供应商资质信息")
    private List<SupplierProportion> supplierProportions;

}
