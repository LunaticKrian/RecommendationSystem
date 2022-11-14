package com.mislab.core.systemcore.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mislab.core.systemcore.mapper.CostMapper;
import com.mislab.core.systemcore.pojo.entity.Cost;
import com.mislab.core.systemcore.service.CostService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangzy
 * @since 2022
 */
@Service
public class CostServiceImpl extends ServiceImpl<CostMapper, Cost> implements CostService {

}
