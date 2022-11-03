package com.mislab.core.systemcore.service;

import com.mislab.common.result.R;
import com.mislab.core.systemcore.pojo.entity.EmployeeEnterprise;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author krian
 * @since 2022-10-09
 */
public interface EmployeeEnterpriseService extends IService<EmployeeEnterprise> {

    R getProjectNumber(Integer industryId, String uid);
}
