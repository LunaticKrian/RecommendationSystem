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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("企业基本信息Dto对象")
public class EnterpriseBasicMsgDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "员工id")
    private String uid;

    @ApiModelProperty(value = "行业id")
    private Integer industryId;

    @ApiModelProperty(value = "企业标识码")
    private String enterpriseKey;

    @ApiModelProperty(value = "企业名")
    private String enterpriseName;

    @ApiModelProperty(value = "纳税人识别号")
    private String taxpayernum;

    @ApiModelProperty(value = "成立时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate establishTime;

    @ApiModelProperty(value = "注册资本")
    private String registeredCapital;

    @ApiModelProperty(value = "法定代表人")
    private String legalPerson;

    @ApiModelProperty(value = "人员数量")
    private Integer personNum;

    @ApiModelProperty(value = "总资产 : 单位(万)")
    private Double totalAsstes;

    @ApiModelProperty(value = "企业类型,若企业为高新技术企业和西部大开发企业有税率减免(1 普通 25% 2 高新 15% 3 西部大开发 15%)")
    private Integer enterpriseType;

    @ApiModelProperty(value = "是否对外投资")
    private Integer investmentAbroad;

    @ApiModelProperty(value = "主营业务[\"运输服务\"]")
    private List<String> business_list;

    @ApiModelProperty(value = "[{\"name\" : \"股东1\"，proportion :\"50\"}]")
    private List<ShareholderInfo> shareholderInfo;

    @ApiModelProperty(value = "[{\"name\" : \"神雕侠侣\",\"proportion\" : \"10\",\"mainProject\" : \"黯然销魂掌\"},{\"name\" : \"射雕英雄\",\"proportion\" : \"5\",\"mainProject\" : \"降龙十八掌\"}]")
    private List<Investee> investee;

    @ApiModelProperty(value = "0 一般纳税人 1 小规模纳税人")
    private Integer taxpayerQualification;

    @ApiModelProperty(value = "0 专票 1 普票 2 普票加专票 ")
    private Integer invoiceType;

}
