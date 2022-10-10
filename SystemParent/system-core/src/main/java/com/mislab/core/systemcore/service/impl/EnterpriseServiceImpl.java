package com.mislab.core.systemcore.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mislab.common.result.R;
import com.mislab.core.systemcore.mapper.BusinessIndustryMapper;
import com.mislab.core.systemcore.mapper.BusinessMapper;
import com.mislab.core.systemcore.mapper.EnterpriseBusinessMapper;
import com.mislab.core.systemcore.mapper.EnterpriseMapper;
import com.mislab.core.systemcore.pojo.dto.EnterpriseBasicMsgDto;
import com.mislab.core.systemcore.pojo.entity.Business;
import com.mislab.core.systemcore.pojo.entity.EmployeeEnterprise;
import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.mislab.core.systemcore.service.EmployeeEnterpriseService;
import com.mislab.core.systemcore.service.EnterpriseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
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

    /**
     * 保存企业信息
     *
     * @param enterpriseBasicMsgDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveEnterpriseMsg(EnterpriseBasicMsgDto enterpriseBasicMsgDto) {
        String enterprise_key = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        //新增企业基本信息，对外投资信息以及股东信息需要转化为json
        Enterprise enterprise = Enterprise.builder()
                .shareholderInfo(JSON.toJSONString(enterpriseBasicMsgDto.getShareholderInfo()))
                .investee(JSON.toJSONString(enterpriseBasicMsgDto.getInvestee())).build();
        //复制相同的属性值到enterprise对象中
        BeanUtils.copyProperties(enterpriseBasicMsgDto, enterprise);
        enterprise.setEnterpriseKey(enterprise_key);
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
        for (String business : business_list) {
            //获取与业务名称对应的业务id
            Integer business_id = businessMapper.selectOne(new LambdaQueryWrapper<Business>().eq(Business::getName, business)).getId();
            //新增企业与业务的绑定关系到enterprise_business表中
            EnterpriseBusiness enterpriseBusiness = EnterpriseBusiness.builder()
                    .enterpriseKey(enterprise_key)
                    .businessId(business_id).build();
            enterpriseBusinessMapper.insert(enterpriseBusiness);
        }

        return R.SUCCESS().data("enterprise_key", enterprise_key);
    }

    /**
     * 修改企业基本信息
     * @param enterpriseBasicMsgDto
     * @return
     */
    @Override
    public R updateEnterpriseMsg(EnterpriseBasicMsgDto enterpriseBasicMsgDto) {
        String enterpriseKey = enterpriseBasicMsgDto.getEnterpriseKey();
        //直接从前端传入企业key,如果已存在于数据库中则为修改，反之为新增
        Enterprise enterprise = this.getOne(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getEnterpriseKey, enterpriseKey));
        enterprise.setShareholderInfo(JSON.toJSONString(enterpriseBasicMsgDto.getShareholderInfo()));
        enterprise.setInvestee(JSON.toJSONString(enterpriseBasicMsgDto.getInvestee()));
        BeanUtils.copyProperties(enterpriseBasicMsgDto, enterprise);

        //修改企业与业务的绑定关系到enterprise_business表中
        List<String> business_list = enterpriseBasicMsgDto.getBusiness_list();
        //删除以往的绑定关系
        enterpriseBusinessMapper.delete(new LambdaQueryWrapper<EnterpriseBusiness>()
                .eq(EnterpriseBusiness::getEnterpriseKey, enterpriseKey));
        for (String business : business_list) {
            //获取与业务名称对应的业务id
            Integer business_id = businessMapper.selectOne(new LambdaQueryWrapper<Business>().eq(Business::getName, business)).getId();
            //新增企业与业务的绑定关系到enterprise_business表中
            EnterpriseBusiness enterpriseBusiness = EnterpriseBusiness.builder()
                    .enterpriseKey(enterpriseKey)
                    .businessId(business_id).build();
            enterpriseBusinessMapper.insert(enterpriseBusiness);
        }
        this.update(enterprise,new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getEnterpriseKey,enterpriseKey));
        return R.SUCCESS().data("enterprise_key", enterpriseKey);
    }

}
