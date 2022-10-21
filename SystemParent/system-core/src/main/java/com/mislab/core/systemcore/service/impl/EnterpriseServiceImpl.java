package com.mislab.core.systemcore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mislab.common.result.R;
import com.mislab.core.systemcore.common.enums.TaxRateEnum;
import com.mislab.core.systemcore.mapper.BusinessMapper;
import com.mislab.core.systemcore.mapper.CostMapper;
import com.mislab.core.systemcore.mapper.EnterpriseBusinessMapper;
import com.mislab.core.systemcore.mapper.EnterpriseMapper;
import com.mislab.core.systemcore.pojo.dto.CostRelatedDto;
import com.mislab.core.systemcore.pojo.dto.EnterpriseBasicMsgDto;
import com.mislab.core.systemcore.pojo.dto.EnterpriseOperationalMsgDto;
import com.mislab.core.systemcore.pojo.dto.RevenueRelatedDto;
import com.mislab.core.systemcore.pojo.entity.*;
import com.mislab.core.systemcore.pojo.jsonDomain.Investee;
import com.mislab.core.systemcore.pojo.jsonDomain.ShareholderInfo;
import com.mislab.core.systemcore.pojo.vo.EnterpriseBasicMsgVo;
import com.mislab.core.systemcore.service.EmployeeEnterpriseService;
import com.mislab.core.systemcore.service.EnterpriseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
    private EmployeeEnterpriseService employeeEnterpriseService;

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private CostMapper costMapper;

    /**
     * 保存企业信息
     *
     * @param enterpriseBasicMsgDto
     * @return
     * @author ascend
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
     * @author ascend
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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

    /**
     * 获取第一页面的企业基本信息
     * @param enterpriseKey
     * @return
     * @author ascend
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R getEnterpriseMsgOfFirst(String enterpriseKey) {
        //声明企业基本信息数据返回对象
        EnterpriseBasicMsgVo enterpriseBasicMsgVo = new EnterpriseBasicMsgVo();
        //通过企业唯一标识码获取数据库中的企业信息
        Enterprise enterprise = enterpriseMapper.selectOne(new LambdaQueryWrapper<Enterprise>()
                .eq(Enterprise::getEnterpriseKey, enterpriseKey));
        //通过企业业务关联表获取企业对应的业务
        List<EnterpriseBusiness> enterpriseBusinesses = enterpriseBusinessMapper.selectList(new LambdaQueryWrapper<EnterpriseBusiness>()
                .eq(EnterpriseBusiness::getEnterpriseKey, enterpriseKey));
        List<String> business_list = new ArrayList<>();
        for(EnterpriseBusiness enterpriseBusiness : enterpriseBusinesses){
            //获取行业对应的一个业务名称
            String businessName = businessMapper.selectOne(new LambdaQueryWrapper<Business>()
                    .eq(Business::getId, enterpriseBusiness.getBusinessId())).getName();
            business_list.add(businessName);
        }
        enterpriseBasicMsgVo.setBusiness_list(business_list);

        //转化股东信息以及对外投资信息  将JSON字符串数组转对象集合
        enterpriseBasicMsgVo.setShareholderInfo(JSON.parseArray(enterprise.getShareholderInfo()).toJavaList(ShareholderInfo.class));
        enterpriseBasicMsgVo.setInvestee(JSON.parseArray(enterprise.getInvestee()).toJavaList(Investee.class));

        //计算税率
        List<TaxRateEnum> taxList = new ArrayList<>();
        if(enterprise.getTaxpayerQualification() == 1){
            //小规模纳税人
            Integer invoiceType = enterprise.getInvoiceType();
            if (invoiceType == 0){
                //专票
                taxList.add(TaxRateEnum.SMALLSCALE_SPECIALTICKET);
            }else if(invoiceType == 1){
                //普票
                taxList.add(TaxRateEnum.SMALLSCALE_GENERALTICKET);
            }else if(invoiceType == 2){
                //专票+普票
                taxList.add(TaxRateEnum.SMALLSCALE_SPECIALTICKET);
                taxList.add(TaxRateEnum.SMALLSCALE_GENERALTICKET);
            }
        }else{
            //一般纳税人
            for (String businessName : business_list){
                //这里目前只能固定写
                if (businessName.equals("运输服务")){
                    taxList.add(TaxRateEnum.COMMONSCALE_TRANSSERVICE);
                }else if (businessName.equals("车辆销售")){
                    taxList.add(TaxRateEnum.COMMONSCALE_VEHICLESALES);
                }else {
                    //其他主营业务,税率为6
                    taxList.add(TaxRateEnum.COMMONSCALE_OTHERSBUSINESS);
                }
            }
        }
        List<Double> taxRateList = new ArrayList<>();
        for(TaxRateEnum taxRate : taxList){
            taxRateList.add(taxRate.getTaxRate());
        }
        enterpriseBasicMsgVo.setTaxRate(taxRateList);

        //复制企业的其他信息到enterpriseBasicMsgVo对象
        BeanUtils.copyProperties(enterprise,enterpriseBasicMsgVo);
        return R.SUCCESS().data("enterpriseBasicMsgVo",enterpriseBasicMsgVo);
    }

    /**
     * 新增/修改企业经营情况
     * @param enterpriseOperationalMsgDto
     * @return
     * @author ascend
     */
    @Override
    @Transactional
    public R saveEnterpriseOperationalMsg(EnterpriseOperationalMsgDto enterpriseOperationalMsgDto) {
        //获取企业信息
        Enterprise enterprise = this.getOne(new LambdaQueryWrapper<Enterprise>()
                .eq(Enterprise::getEnterpriseKey, enterpriseOperationalMsgDto.getEnterpriseKey()));
        //设置是否兼营销售纳税人
        enterprise.setSalesTaxpayer(enterprise.getSalesTaxpayer());
        //设置年营业额
        enterprise.setAnnualTurnover(enterpriseOperationalMsgDto.getAnnualTurnover());
        //设置年经营成本
        enterprise.setAnnualCost(enterpriseOperationalMsgDto.getAnnualCost());
        //存放企业-业务数据到enterprise_business表中
        //存放之前应该先删除以前的数据
        List<RevenueRelatedDto> revenueRelateList = enterpriseOperationalMsgDto.getRevenueRelateList();
        for (RevenueRelatedDto  revenueRelate : revenueRelateList){
            //获取业务对象
            Business business = businessMapper.selectOne(new LambdaQueryWrapper<Business>()
                    .eq(Business::getName, revenueRelate.getBusinessName()));
            //存放业务-企业关联信息,这里不用先删除后新增，直接执行update操作即可
            EnterpriseBusiness enterpriseBusiness = EnterpriseBusiness.builder()
                    .enterpriseKey(enterpriseOperationalMsgDto.getEnterpriseKey())
                    .businessId(business.getId())
                    .businessRatio(revenueRelate.getBusinessRatio())
                    .generalTaxpayerRatio(revenueRelate.getGeneralTaxpayerRatio())
                    .smallscaleTaxpayerRatio(revenueRelate.getSmallscaleTaxpayerRatio())
                    .naturalPersonRatio(revenueRelate.getNaturalPersonRatio()).build();
            //执行更新操作
            enterpriseBusinessMapper.update(enterpriseBusiness,new LambdaUpdateWrapper<EnterpriseBusiness>()
                    .eq(EnterpriseBusiness::getEnterpriseKey,enterpriseOperationalMsgDto.getEnterpriseKey())
                    .eq(EnterpriseBusiness::getBusinessId,business.getId()));
        }

        //存放成本相关信息到cost表中,如果之前有企业已经有cost_type消息，那么应该先删除之前的，防止后期数据库数据量过多
        String costType = enterprise.getCostType();
        if (!StringUtils.isEmpty(costType)){
            List<Integer> costTypeList = JSONArray.parseArray(costType).toJavaList(Integer.class);
            costMapper.deleteBatchIds(costTypeList);
        }
        //执行cost新增操作
        List<CostRelatedDto> costRelatedList = enterpriseOperationalMsgDto.getCostRelatedList();
        //存放cost_id到enterprise表中
        List<Integer> costTypeRes = new ArrayList<>();
        for (CostRelatedDto costRelated : costRelatedList){
            Cost cost = Cost.builder()
                    .name(costRelated.getName())
                    .costRatio(costRelated.getCostRatio())
                    .supplierProportion(JSON.toJSONString(costRelated.getSupplierProportions()))
                    .industryId(costRelated.getIndustryId()).build();
            costMapper.insert(cost);
            costTypeRes.add(cost.getId());
        }
        enterprise.setCostType(JSON.toJSONString(costTypeRes));
        //保存enterprise数据
        this.save(enterprise);
        return R.SUCCESS().message("保存企业信息成功");
    }


    /**
     * 获取第一页面的企业基本信息
     * @param enterpriseKey
     * @return
     * @author ascend
     */
    @Override
    @Transactional
    public R getEnterpriseMsgOfSecond(String enterpriseKey) {
        //获取enterprise对象
        Enterprise enterprise = this.getOne(new LambdaQueryWrapper<Enterprise>()
                .eq(Enterprise::getEnterpriseKey, enterpriseKey));
        //
        return R.SUCCESS();
    }

}
