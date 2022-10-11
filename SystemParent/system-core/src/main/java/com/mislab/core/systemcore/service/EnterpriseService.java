package com.mislab.core.systemcore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mislab.common.result.R;
import com.mislab.core.systemcore.pojo.dto.EnterpriseBasicMsgDto;
import com.mislab.core.systemcore.pojo.entity.Enterprise;

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
    R saveEnterpriseMsg(EnterpriseBasicMsgDto enterpriseBasicMsgDto);

    R updateEnterpriseMsg(EnterpriseBasicMsgDto enterpriseBasicMsgDto);

    R getEnterpriseMsgOfFirst(String enterpriseKey);
}
