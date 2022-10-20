package com.mislab.core.systemcore.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author Ascendable
 * @since 2022/10/20
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="人工相关信息", description="")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManualRelatedDto {

    @ApiModelProperty(value = "高管人数")
    private Integer ManagerNumber;

    @ApiModelProperty(value = "高管平均薪资")
    private Integer ManagerAvgSalary;

    @ApiModelProperty(value = "司机人数")
    private Integer DriverNumber;

    @ApiModelProperty(value = "司机人数")
    private Integer DriverAvgSalary;

}
