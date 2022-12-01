package com.mislab.core.systemcore.pojo.calculation.pojo;

import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.pojo.vo.EnterpriseBasicMsgVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("【计算使用】企业对应成本信息")
public class EnterpriseCostDTO {
    @ApiModelProperty(value = "企业基本信息")
    private Enterprise enterprise;

    @ApiModelProperty(value = "企业对应的成本花费信息")
    private List<CostInfoDTO> costInfoDTOList;
}
