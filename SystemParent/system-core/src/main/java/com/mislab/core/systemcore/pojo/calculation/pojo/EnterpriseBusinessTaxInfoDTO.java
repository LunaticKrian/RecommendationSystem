package com.mislab.core.systemcore.pojo.calculation.pojo;

import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.mislab.core.systemcore.pojo.entity.TaxRate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("【计算使用】企业经营项目信息")
public class EnterpriseBusinessTaxInfoDTO {
    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "当前业务营业额")
    private Double amount;

    @ApiModelProperty(value = "当前业务税率")
    private TaxRate taxRate;

    @ApiModelProperty(value = "业务信息")
    private EnterpriseBusiness enterpriseBusiness;
}
