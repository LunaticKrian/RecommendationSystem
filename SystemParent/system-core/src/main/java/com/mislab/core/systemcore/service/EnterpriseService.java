package com.mislab.core.systemcore.service;

import com.mislab.core.systemcore.pojo.dto.EnterpriseBasicMsgDto;
import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
public interface EnterpriseService extends IService<Enterprise> {

    /**
     * 保存企业基本信息
     * @param enterpriseBasicMsgDto
     * @return
     */
    boolean saveEnterpriseMsg(EnterpriseBasicMsgDto enterpriseBasicMsgDto);
}
