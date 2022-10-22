package com.mislab.core.systemcore.pojo.vo;

import com.mislab.core.systemcore.pojo.dto.RevenueRelatedDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ascendable
 * @since 2022/10/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("收入相关信息")
@Builder
public class RevenueVo extends RevenueRelatedDto {

}
