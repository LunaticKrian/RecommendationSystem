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
@ApiModel(value="股东信息", description="")
@Builder
@AllArgsConstructor
public class ShareholderInfo implements Serializable {

    @ApiModelProperty(value = "股东姓名")
    private String name;

    @ApiModelProperty(value = "持股比例")
    private String proportion;
}
