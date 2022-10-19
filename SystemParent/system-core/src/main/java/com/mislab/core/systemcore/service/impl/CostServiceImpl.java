package com.mislab.core.systemcore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mislab.common.result.R;
import com.mislab.core.systemcore.mapper.EnterpriseMapper;
import com.mislab.core.systemcore.pojo.dto.CostDto;
import com.mislab.core.systemcore.pojo.entity.Cost;
import com.mislab.core.systemcore.mapper.CostMapper;
import com.mislab.core.systemcore.pojo.entity.Employee;
import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.mislab.core.systemcore.pojo.jsonDomain.SupplierProportion;
import com.mislab.core.systemcore.service.CostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangzy
 * @since 2022
 */
@Service
public class CostServiceImpl extends ServiceImpl<CostMapper, Cost> implements CostService {
    @Autowired
    EnterpriseMapper ENTERPRISE_MAPPER;

    @Autowired
    CostMapper COST_MAPPER;


    @Override
    public R update(List<CostDto> costs, String epKey) {
        Enterprise enterprise = ENTERPRISE_MAPPER.selectOne(new LambdaQueryWrapper<Enterprise>()
                .eq(Enterprise::getEnterpriseKey, epKey));
        if (StringUtils.isEmpty(enterprise.getCostType())){
            //enterprise内没有costType
            int[] costType = new int[5];
            int i =0;
            for (CostDto costDto:costs){
                String costJson = JSON.toJSONString(costDto.getSupplierProportions());
                COST_MAPPER.insertCost(costDto,costDto.getCostName(),costJson);
                costType[i] = costDto.getId();
                i++;
            }
            String costTypeString = String.valueOf(costType);
            ENTERPRISE_MAPPER.updateCostType(epKey,costTypeString);
        }else {
            //enterprise中有costType，对已有的进行修改
            String costType = ENTERPRISE_MAPPER.selectCostTypeByKey(epKey);
            //取出已有的costType中的编号
            int [] arr = new int[5];
            //char转化为int
            String[] strArray = costType.split(",");
            int i = 0;
            for(String s : strArray){
                //去的掉字符串数组中的特殊字符，获取每个元素的值
                if(i == 0){
                    s = s.substring(1,s.length());
                }
                if(i == strArray.length-1){
                    s = s.replace("\n","").trim();
                    s = s.substring(0,s.length()-1);
                }
                s = s.replace("\n","").trim();
                //将字符串数组中的元素转换成int类型，复制给int数组的元素
                arr[i] = Integer.valueOf(s);
                i++;
            }

            for (int j = 0; arr[j] != 0; j++) {
                System.out.println(arr[j]);
                String costJson = JSON.toJSONString(costs.get(j).getSupplierProportions());
                COST_MAPPER.updateCost(costs.get(j).getCostName(),costJson,arr[j]);
            }
        }
        return R.SUCCESS();
    }

    @Override
    public List<CostDto> getCosts(String epKey) {
        if (StringUtils.isEmpty(ENTERPRISE_MAPPER.selectCostTypeByKey(epKey))){
            //enterprise内没有costType
            return null;
        }else {
            //enterprise中有costType，对已有的进行修改
            String costType = ENTERPRISE_MAPPER.selectCostTypeByKey(epKey);
            //取出已有的costType中的编号
            int [] arr = new int[5];
            //char转化为int
            String[] strArray = costType.split(",");
            int i = 0;
            for(String s : strArray){
                //去的掉字符串数组中的特殊字符，获取每个元素的值
                if(i == 0){
                    s = s.substring(1,s.length());
                }
                if(i == strArray.length-1){
                    s = s.replace("\n","").trim();
                    s = s.substring(0,s.length()-1);
                }
                s = s.replace("\n","").trim();
                //将字符串数组中的元素转换成int类型，复制给int数组的元素
                arr[i] = Integer.valueOf(s);
                i++;
            }
            List<CostDto> costs = new ArrayList<>();
            for ( i = 0; i < arr.length; i++) {
                //costs.add(COST_MAPPER.selectById(arr[i]));
                if (arr[i] == 0){
                    break;
                }
                Cost cost = COST_MAPPER.selectById(arr[i]);
                /*JSONObject costJson =  (JSONObject)JSON.toJSON(cost.getSupplierProportion());
                JSONArray costJsonJSONArray = costJson.getJSONArray("SupplierProportion");
                List<SupplierProportion> supplierProportions = JSONArray.parseArray(costJsonJSONArray
                        .toJSONString(),SupplierProportion.class);*/
                List<SupplierProportion> supplierProportions = JSONArray.parseArray(cost.getSupplierProportion())
                        .toJavaList(SupplierProportion.class);
                CostDto costDto = new CostDto();
                costDto.setCostName(cost.getName());
                costs.add(costDto);
                //List<SupplierProportion> supplierProportions = (List<SupplierProportion>));
                costs.get(i).setSupplierProportions(supplierProportions);
                costs.get(i).setCostName(cost.getName());
            }
            return costs;
        }

    }

}
