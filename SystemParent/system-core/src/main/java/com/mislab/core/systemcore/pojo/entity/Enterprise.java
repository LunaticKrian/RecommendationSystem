package com.mislab.core.systemcore.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Enterprise对象", description="")
@Builder
public class Enterprise implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "企业唯一识别码")
    private String enterpriseKey;

    @ApiModelProperty(value = "企业名")
    private String enterpriseName;

    @ApiModelProperty(value = "企业类型,若企业为高新技术企业和西部大开发企业有税率减免(0 普通 25% 1 高新 15% 2 西部大开发 15%)")
    private Integer enterpriseType;

    @ApiModelProperty(value = "纳税人识别号")
    private String taxpayernum;

    @ApiModelProperty(value = "成立时间")
    private LocalDate establishTime;

    @ApiModelProperty(value = "注册资本")
    private String registeredCapital;

    @ApiModelProperty(value = "人员数量")
    private Integer personNum;

    @ApiModelProperty(value = "总资产 : 单位(万)")
    private Double totalAsstes;

    @ApiModelProperty(value = "法定代表人")
    private String legalPerson;

    @ApiModelProperty(value = "是否对外投资")
    private Integer investmentAbroad;

    @ApiModelProperty(value = "行业")
    private Integer industryId;

    @ApiModelProperty(value = "兼营销售纳税人0 否 1 是")
    private Integer salesTaxpayer;

    @ApiModelProperty(value = "年营业额")
    private Double annualTurnover;

    @ApiModelProperty(value = "年经营成本")
    private Double annualCost;

    @ApiModelProperty(value = "成本费用类别	[1,2,3]	")
    private String costType;

    @ApiModelProperty(value = "[{\"name\" : \"股东1\"，proportion :\"50\"}]")
    private String shareholderInfo;

    @ApiModelProperty(value = "0 一般纳税人 1 小规模纳税人")
    private Integer taxpayerQualification;

    @ApiModelProperty(value = "0 专票 1 普票 2 普票加专票 ")
    private Integer invoiceType;

    @ApiModelProperty(value = "[{\"name\" : \"神雕侠侣\",\"proportion\" : \"10\",\"mainProject\" : \"黯然销魂掌\"},{\"name\" : \"射雕英雄\",\"proportion\" : \"5\",\"mainProject\" : \"降龙十八掌\"}]")
    private String investee;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
