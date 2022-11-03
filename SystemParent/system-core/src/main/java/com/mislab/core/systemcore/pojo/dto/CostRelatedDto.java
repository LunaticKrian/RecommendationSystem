package com.mislab.core.systemcore.pojo.dto;

import com.mislab.core.systemcore.pojo.jsonDomain.SupplierProportion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author Ascendable
 * @since 2022/10/20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="成本相关信息", description="")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CostRelatedDto {

    @ApiModelProperty(value = "成本类别名称")
    private String name;

    @ApiModelProperty(value = "成本占比")
    private Double CostRatio;

    @ApiModelProperty(value = "供应商资质信息")
    private List<SupplierProportion> supplierProportions;

    @ApiModelProperty(value = "行业id")
    private Integer industryId;
}
