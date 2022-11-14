package com.mislab.core.systemcore.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mislab.core.systemcore.pojo.jsonDomain.Investee;
import com.mislab.core.systemcore.pojo.jsonDomain.ShareholderInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Ascendable
 * @since 2022/11/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("企业查询条件Dto对象")
public class EnterpriseForSearchDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "企业标识码")
    private String enterpriseKey;

    @ApiModelProperty(value = "企业名")
    private String enterpriseName;

    @ApiModelProperty(value = "员工id")
    private String uid;

    @ApiModelProperty(value = "行业名称")
    private String industryName;

    @ApiModelProperty(value = "项目状态")
    private Integer state;

    @ApiModelProperty(value = "创建时间之开始时间")
    private LocalDate createStartTime;

    @ApiModelProperty(value = "创建时间之结束时间")
    private LocalDate createEndTime;

    @ApiModelProperty(value = "完成时间之开始时间")
    private LocalDate finishedStartTime;

    @ApiModelProperty(value = "完成时间之结束时间")
    private LocalDate finishedEndTime;

}
