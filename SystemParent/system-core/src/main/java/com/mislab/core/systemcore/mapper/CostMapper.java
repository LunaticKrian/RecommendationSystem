package com.mislab.core.systemcore.mapper;

import com.mislab.core.systemcore.pojo.dto.CostDto;
import com.mislab.core.systemcore.pojo.entity.Cost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */

@Mapper
public interface CostMapper extends BaseMapper<Cost> {
    //添加cost
    int insertCost(CostDto cost,String costJson);
    //修改cost
    int updateCost(CostDto costDto, int costId);
    //根据id获取cost（mp有自带）
}
