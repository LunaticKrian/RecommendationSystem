package com.mislab.core.systemcore.pojo.jsonDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="对外投资信息", description="")
@Builder
@AllArgsConstructor
public class Investee implements Serializable {

    @ApiModelProperty(value = "企业名称")
    private String name;

    @ApiModelProperty(value = "投资比例")
    private String proportion;

    @ApiModelProperty(value = "主营项目")
    private String mainProject;

}
