package com.mislab.core.systemcore.service;

import com.mislab.common.result.R;
import com.mislab.core.systemcore.pojo.entity.Industry;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
public interface IndustryService extends IService<Industry> {

    R getBusinessByIndustry(String industryName);
}
