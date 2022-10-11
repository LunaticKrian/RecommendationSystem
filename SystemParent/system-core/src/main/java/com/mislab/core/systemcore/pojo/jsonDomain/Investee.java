package com.mislab.core.systemcore.pojo.jsonDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="对外投资信息", description="")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Investee implements Serializable {

    @ApiModelProperty(value = "企业名称")
    private String name;

    @ApiModelProperty(value = "投资比例")
    private String proportion;

    @ApiModelProperty(value = "主营项目")
    private String mainProject;

}
