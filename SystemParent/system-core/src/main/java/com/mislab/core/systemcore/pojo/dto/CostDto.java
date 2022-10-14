package com.mislab.core.systemcore.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mislab.core.systemcore.pojo.jsonDomain.SupplierProportion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CostDto对象", description="")
public class CostDto implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "成本费用类别")
    private String costName;

    @ApiModelProperty(value = "成本费用占比")
    private Double businessRatio;

    @ApiModelProperty(value = "供应商资质")
    private List<SupplierProportion> supplierProportions;


}
