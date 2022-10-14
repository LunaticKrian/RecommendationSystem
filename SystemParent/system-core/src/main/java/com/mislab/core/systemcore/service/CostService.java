package com.mislab.core.systemcore.service;

import com.mislab.common.result.R;
import com.mislab.core.systemcore.mapper.CostMapper;
import com.mislab.core.systemcore.mapper.EnterpriseMapper;
import com.mislab.core.systemcore.pojo.dto.CostDto;
import com.mislab.core.systemcore.pojo.entity.Cost;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
public interface CostService extends IService<Cost> {
    public R update(List<CostDto> costDtos, String epKey);

    public List<CostDto> getCosts(String enterpriseKey);


}
