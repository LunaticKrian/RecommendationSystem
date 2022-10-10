package com.mislab.core.systemcore.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mislab.core.systemcore.mapper.BusinessIndustryMapper;
import com.mislab.core.systemcore.mapper.BusinessMapper;
import com.mislab.core.systemcore.mapper.EnterpriseBusinessMapper;
import com.mislab.core.systemcore.mapper.EnterpriseMapper;
import com.mislab.core.systemcore.pojo.dto.EnterpriseBasicMsgDto;
import com.mislab.core.systemcore.pojo.entity.*;
import com.mislab.core.systemcore.service.EmployeeEnterpriseService;
import com.mislab.core.systemcore.service.EnterpriseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements EnterpriseService {

    @Autowired
    private EnterpriseBusinessMapper enterpriseBusinessMapper;

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private BusinessIndustryMapper businessIndustryMapper;

    @Autowired
    private EmployeeEnterpriseService employeeEnterpriseService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveEnterpriseMsg(EnterpriseBasicMsgDto enterpriseBasicMsgDto) {
        //String enterprise_key = UUID.randomUUID().toString().replace("-", "").substring(0,10);
        //直接从前端传入企业key,如果已存在于数据库中则为修改，反之为新增
        String enterprise_key = enterpriseBasicMsgDto.getEnterpriseKey();
        Enterprise one = this.getOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getEnterpriseKey, enterprise_key));
        //企业基本信息，对外投资信息以及股东信息需要转化为json
        Enterprise enterprise = Enterprise.builder()
                .enterpriseKey(enterprise_key)
                .shareholderInfo(JSON.toJSONString(enterpriseBasicMsgDto.getShareholderInfo()))
                .investee(JSON.toJSONString(enterpriseBasicMsgDto.getInvestee()))
                .business_list(JSON.toJSONString(enterpriseBasicMsgDto.getBusiness_list())).build();
        //复制相同的属性值到enterprise对象中
        BeanUtils.copyProperties(enterpriseBasicMsgDto,enterprise);
        //判断数据库中是否已经有该企业信息
        if (one == null){
            //没有则新增
            this.save(enterprise);
            //新增员工与企业的绑定关系
            String uid = enterpriseBasicMsgDto.getUid();
            EmployeeEnterprise employeeEnterprise = EmployeeEnterprise.builder()
                    .uid(uid)
                    .state(1)
                    .enterpriseKey(enterprise_key).build();
            employeeEnterpriseService.save(employeeEnterprise);
            //拆分主营业务,保存在数据库中
            List<String> business_list = enterpriseBasicMsgDto.getBusiness_list();
            for(String business : business_list){
                Integer business_id = getBusinessId(business);
                //新增行业与业务的绑定关系到business_industry表中
                BusinessIndustry businessIndustryOne = businessIndustryMapper.selectOne(new LambdaQueryWrapper<BusinessIndustry>()
                        .eq(BusinessIndustry::getBusinessId, business_id)
                        .eq(BusinessIndustry::getIndustryId, enterpriseBasicMsgDto.getIndustryId()));
                //判断该绑定关系是否已经存在于数据库中，如果不存在则新增，存在则不管
                if(businessIndustryOne == null){
                    BusinessIndustry businessIndustry = BusinessIndustry.builder()
                            .industryId(enterpriseBasicMsgDto.getIndustryId())
                            .businessId(business_id).build();
                    businessIndustryMapper.insert(businessIndustry);
                }
                //新增企业与业务的绑定关系到enterprise_business表中
                EnterpriseBusiness enterpriseBusiness = EnterpriseBusiness.builder()
                        .enterpriseKey(enterprise_key)
                        .businessId(business_id).build();
                enterpriseBusinessMapper.insert(enterpriseBusiness);
            }
        }else{
            //有则修改
            this.update(enterprise,new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getEnterpriseKey,enterprise_key));
            //修改企业与业务的绑定关系到enterprise_business表中
            List<String> business_list = enterpriseBasicMsgDto.getBusiness_list();
            //删除以往的绑定关系
            enterpriseBusinessMapper.delete(new LambdaQueryWrapper<EnterpriseBusiness>()
                    .eq(EnterpriseBusiness::getEnterpriseKey,enterprise_key));

            for(String business : business_list){
                LambdaQueryWrapper<Business> wrapper = new LambdaQueryWrapper<Business>().eq(Business::getName, business);
                Business businessOne = businessMapper.selectOne(wrapper);
                Integer business_id = null;
                //判断该业务是否已经存在于数据库中，如果不存在则新增，存在则不管
                if(businessOne == null){
                    Business insert_business = new Business();
                    insert_business.setName(business);
                    businessMapper.insert(insert_business);
                    businessOne = businessMapper.selectOne(new LambdaQueryWrapper<Business>()
                            .eq(Business::getName,business));
                }
                business_id = businessOne.getId();
                EnterpriseBusiness enterpriseBusiness = EnterpriseBusiness.builder()
                        .enterpriseKey(enterprise_key)
                        .businessId(business_id).build();
                enterpriseBusinessMapper.insert(enterpriseBusiness);
            }
        }
        return true;
    }

    private Integer getBusinessId(String business) {
        //保存业务在数据库business表中
        LambdaQueryWrapper<Business> wrapper = new LambdaQueryWrapper<Business>().eq(Business::getName, business);
        Business businessOne = businessMapper.selectOne(wrapper);
        Integer business_id = null;
        //判断该业务是否已经存在于数据库中，如果不存在则新增，存在则不管
        if(businessOne == null){
            Business insert_business = new Business();
            insert_business.setName(business);
            businessMapper.insert(insert_business);
            business_id = businessMapper.selectOne(new LambdaQueryWrapper<Business>()
                    .eq(Business::getName, business)).getId();
        }else{
            business_id = businessOne.getId();
        }
        return business_id;
    }
}
