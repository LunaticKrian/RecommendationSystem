package com.mislab.core.systemcore.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("员工信息对象")
public class EmployeeVo {
    @ApiModelProperty(value = "员工编号")
    private String uid;

    @ApiModelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty(value = "电话号码")
    private String mobile;

    @ApiModelProperty(value = "用户头像")
    private String headImg;

    @ApiModelProperty(value = "JWT访问令牌")
    private String token;

}
