package com.mislab.core.systemcore.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@ApiModel(value="TaxRate对象", description="")
public class TaxRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "税率名称")
    private String name;

    private Integer businessId;

    private Integer costId;

    @ApiModelProperty(value = "纳税人资格  0 一般纳税人 1 小规模纳税人")
    private Integer taxpayerQualification;

    @ApiModelProperty(value = "发票种类")
    private Integer invoiceType;

    @ApiModelProperty(value = "是否兼营销售纳税人")
    private Integer salesTaxpayer;

    @ApiModelProperty(value = "税率")
    private Double taxRate;
}
