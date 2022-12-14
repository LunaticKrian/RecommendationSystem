package com.mislab.core.systemcore.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
@ApiModel(value="EnterpriseBusiness对象", description="")
public class EnterpriseBusinessDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "企业key")
    private String enterpriseKey;

    @ApiModelProperty(value = "占比")
    private Double businessRatio;

    @ApiModelProperty(value = "一般纳税人")
    private Double generalTaxpayerRatio;

    @ApiModelProperty(value = "小规模纳税人")
    private Double smallscaleTaxpayerRatio;

    @ApiModelProperty(value = "自然人")
    private Double naturalPersonRatio;

}
