package com.mislab.core.systemcore.mapper;

import com.alibaba.fastjson.JSONArray;
import com.mislab.core.systemcore.pojo.entity.Enterprise;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mislab.core.systemcore.pojo.dto.Enterprise2VO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */

@Mapper
public interface EnterpriseMapper extends BaseMapper<Enterprise> {

    //根据enterpriseKey放入costType
    int updateCostType(String enterpriseKey, JSONArray costType);

    String selectCostTypeByKey(String enterpriseKey);

    /**
     * 根据enterpriseKey向enterprise加入年营业额和是否兼营销售纳税人,年经营成本
     */
    int updateSAAEnterprise(String enterpriseKey,int ST,double AT,double AC);

    Enterprise2VO selectDateIITable(String enterpriseKey);

}
