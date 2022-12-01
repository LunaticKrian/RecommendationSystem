package com.mislab.core.systemcore.pojo.calculation.pojo;

import com.mislab.core.systemcore.pojo.entity.Enterprise;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("【计算使用】企业经营项目基本信息")
public class EnterpriseBusinessInfoDTO {
    @ApiModelProperty(value = "企业信息")
    private Enterprise enterprise;

    @ApiModelProperty(value = "经营业务详细信息列表")
    private List<EnterpriseBusinessTaxInfoDTO> enterpriseBusinessList;
}
