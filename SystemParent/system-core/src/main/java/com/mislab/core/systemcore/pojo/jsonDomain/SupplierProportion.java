package com.mislab.core.systemcore.pojo.jsonDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author Ascendable
 * @since 2022/10/20
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="供应商资质信息", description="")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierProportion implements Serializable {

    @ApiModelProperty(value = "供应商资质类型")
    private Integer supId;

    @ApiModelProperty(value = "占比")
    private Double proportion;

}
