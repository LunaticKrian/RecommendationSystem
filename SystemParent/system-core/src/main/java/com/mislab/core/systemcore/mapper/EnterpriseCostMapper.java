package com.mislab.core.systemcore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mislab.core.systemcore.pojo.calculation.pojo.CostInfoDTO;
import com.mislab.core.systemcore.pojo.dto.CostDto;
import com.mislab.core.systemcore.pojo.dto.CostRelatedDto;
import com.mislab.core.systemcore.pojo.entity.Cost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EnterpriseCostMapper extends BaseMapper<CostDto> {
    /**
     * 根据Id，查询企业支出项目
     */
    CostRelatedDto findCostById(@Param("id") int id);

    /**
     * 根据id，查询企业成本项目
     * @param id 成本Id
     * @return Cost
     */
    Cost findCostInfoById(@Param("id") int id);
}
