package com.mislab.core.systemcore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mislab.common.result.R;
import com.mislab.core.systemcore.mapper.BusinessIndustryMapper;
import com.mislab.core.systemcore.mapper.BusinessMapper;
import com.mislab.core.systemcore.pojo.entity.Business;
import com.mislab.core.systemcore.pojo.entity.BusinessIndustry;
import com.mislab.core.systemcore.pojo.entity.Industry;
import com.mislab.core.systemcore.mapper.IndustryMapper;
import com.mislab.core.systemcore.service.IndustryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
@Service
public class IndustryServiceImpl extends ServiceImpl<IndustryMapper, Industry> implements IndustryService {

    @Autowired
    private BusinessIndustryMapper businessIndustryMapper;

    @Autowired
    private BusinessMapper businessMapper;

    /**
     * 通过行业名称获取其对应业务
     * @param industryName
     * @return
     * @author ascend
     */
    @Override
    public R getBusinessByIndustry(String industryName) {
        //通过企业名称获取企业id
        Integer industryId = this.getOne(new LambdaQueryWrapper<Industry>().eq(Industry::getIndustryName, industryName)).getId();
        List<BusinessIndustry> businessAndIndustries = businessIndustryMapper
                .selectList(new LambdaQueryWrapper<BusinessIndustry>().eq(BusinessIndustry::getIndustryId, industryId));
        List<String> res = new ArrayList<>();
        for(BusinessIndustry businessIndustry : businessAndIndustries){
            String businessName = businessMapper.selectOne(new LambdaQueryWrapper<Business>()
                    .eq(Business::getId, businessIndustry.getBusinessId())).getName();
            res.add(businessName);
        }
        return R.SUCCESS().data("business_list", res);
    }
}
