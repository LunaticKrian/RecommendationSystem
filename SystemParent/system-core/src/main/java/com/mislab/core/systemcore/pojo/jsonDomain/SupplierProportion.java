package com.mislab.core.systemcore.pojo.jsonDomain;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CostJson对象", description="")
public class SupplierProportion {

    private int supId;

    private int proportion;
}
