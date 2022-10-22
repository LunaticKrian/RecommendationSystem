package com.mislab.core.systemcore.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ascendable
 * @since 2022/10/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("企业项目VO")
public class EnterpriseProjectVo implements Serializable {

    @ApiModelProperty(value = "企业标识码")
    private String enterpriseKey;

    @ApiModelProperty(value = "企业名称")
    private String enterpriseName;

    @ApiModelProperty(value = "行业名称")
    private String industryName;

    @ApiModelProperty(value = "0:逻辑删除 1:正在进行(保存但未提交) 2:已完成")
    private Integer state;

    @ApiModelProperty(value = "完成/保存时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注")
    private String note;


}
