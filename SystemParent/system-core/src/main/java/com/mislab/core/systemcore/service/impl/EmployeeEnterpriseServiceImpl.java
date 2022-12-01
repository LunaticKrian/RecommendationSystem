package com.mislab.core.systemcore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mislab.common.result.R;
import com.mislab.core.systemcore.common.enums.EnterpriseStateEnum;
import com.mislab.core.systemcore.mapper.EmployeeEnterpriseMapper;
import com.mislab.core.systemcore.pojo.entity.EmployeeEnterprise;
import com.mislab.core.systemcore.service.EmployeeEnterpriseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-10-09
 */
@Service
public class EmployeeEnterpriseServiceImpl extends ServiceImpl<EmployeeEnterpriseMapper, EmployeeEnterprise> implements EmployeeEnterpriseService {

    /**
     * 统计员工已/待完成的项目数量
     * @param industryId
     * @param uid
     * @return
     * @author ascend
     */
    @Override
    public R getProjectNumber(Integer industryId, String uid) {
        Map<String,Object> res = new HashMap<>();
        //统计已完成的项目数量
        Integer finishedCount = this.count(new LambdaQueryWrapper<EmployeeEnterprise>()
                .eq(EmployeeEnterprise::getIndustryId, industryId)
                .eq(EmployeeEnterprise::getUid, uid)
                .eq(EmployeeEnterprise::getState, EnterpriseStateEnum.ALREADY_COMPLETE));
        //统计待完成的项目数量
        Integer unFinishedCount = this.count(new LambdaQueryWrapper<EmployeeEnterprise>()
                .eq(EmployeeEnterprise::getIndustryId, industryId)
                .eq(EmployeeEnterprise::getUid, uid)
                .eq(EmployeeEnterprise::getState, EnterpriseStateEnum.STILL_WRITING));

        res.put("finishedCount",finishedCount);
        res.put("unFinishedCount",unFinishedCount);
        return R.SUCCESS().data(res);
    }
}
