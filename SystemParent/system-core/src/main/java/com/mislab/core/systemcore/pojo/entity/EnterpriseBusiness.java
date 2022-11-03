package com.mislab.core.systemcore.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
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
@ApiModel(value="EnterpriseBusiness对象", description="")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseBusiness implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "企业key")
    private String enterpriseKey;

    @ApiModelProperty(value = "业务id")
    private Integer businessId;

    @ApiModelProperty(value = "占比")
    private Double businessRatio;

    @ApiModelProperty(value = "单项金额")
    private Double amount;

    @ApiModelProperty(value = "一般纳税人")
    private Double generalTaxpayerRatio;

    @ApiModelProperty(value = "小规模纳税人")
    private Double smallscaleTaxpayerRatio;

    @ApiModelProperty(value = "自然人")
    private Double naturalPersonRatio;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
