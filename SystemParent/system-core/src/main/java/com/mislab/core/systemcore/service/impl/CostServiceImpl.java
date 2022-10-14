package com.mislab.core.systemcore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mislab.common.result.R;
import com.mislab.core.systemcore.mapper.EnterpriseMapper;
import com.mislab.core.systemcore.pojo.dto.CostDto;
import com.mislab.core.systemcore.pojo.entity.Cost;
import com.mislab.core.systemcore.mapper.CostMapper;
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
        if (StringUtils.isEmpty(ENTERPRISE_MAPPER.selectCostTypeByKey(epKey))){
            //enterprise内没有costType
            int[] costType = new int[5];
            int i =0;
            for (CostDto costDto:costs){
                String costJosn = JSON.toJSONString(costDto.getSupplierProportions());
                costType[i] =COST_MAPPER.insertCost(costDto,costJosn);
            }
            JSONArray JsonCost = (JSONArray) JSONArray.toJSON(costType);
            ENTERPRISE_MAPPER.updateCostType(epKey,JsonCost);
        }else {
            //enterprise中有costType，对已有的进行修改
            String costType = ENTERPRISE_MAPPER.selectCostTypeByKey(epKey);
            //取出已有的costType中的编号
            char[] arr = costType.toCharArray();    // char数组
            for (int i = 0; i < arr.length; i++) {
                COST_MAPPER.updateCost(costs.get(0),arr[i]);
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
            //enterprise中有costType,取出
            String costType = ENTERPRISE_MAPPER.selectCostTypeByKey(epKey);
            //取出已有的costType中的编号
            char[] arr = costType.toCharArray();    // char数组
            List<CostDto> costs = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                //costs.add(COST_MAPPER.selectById(arr[i]));
                Cost cost = COST_MAPPER.selectById(arr[i]);
                JSONObject costJson =  (JSONObject)JSON.toJSON(cost.getSupplierProportion());
                JSONArray costJsonJSONArray = costJson.getJSONArray("SupplierProportion");
                List<SupplierProportion> supplierProportions = JSONArray.parseArray(costJsonJSONArray.toJSONString(),SupplierProportion.class);
                //List<SupplierProportion> supplierProportions = (List<SupplierProportion>));
                costs.get(i).setSupplierProportions(supplierProportions);
                costs.get(i).setCostName(cost.getName());
            }
            return costs;
        }

    }

}
