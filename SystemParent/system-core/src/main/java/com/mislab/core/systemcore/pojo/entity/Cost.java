package com.mislab.core.systemcore.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@ApiModel(value="Cost对象", description="")
@Builder
public class Cost implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "成本费用类别")
    private String name;

    @ApiModelProperty(value = "成本占比")
    private Double costRatio;

    @ApiModelProperty(value = "供应商资质,包含供应商类别id和比例[{\"supId\" : 1,\"proportion\":0.5},{\"supId\" : 2,\"proportion\":0.3},{\"supId\" : 3,\"proportion\":0.2}]")
    private String supplierProportion;

    @ApiModelProperty(value = "行业id")
    private Integer industryId;


}
