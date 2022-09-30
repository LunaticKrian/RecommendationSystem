package com.mislab.core.systemcore.service.impl;

import com.mislab.core.systemcore.pojo.entity.Business;
import com.mislab.core.systemcore.mapper.BusinessMapper;
import com.mislab.core.systemcore.service.BusinessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessMapper, Business> implements BusinessService {

}
