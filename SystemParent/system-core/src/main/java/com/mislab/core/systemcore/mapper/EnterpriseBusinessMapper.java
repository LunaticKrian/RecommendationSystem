package com.mislab.core.systemcore.mapper;

import com.mislab.core.systemcore.pojo.dto.EnterpriseBusinessDTO;
import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mislab.core.systemcore.pojo.calculation.pojo.EnterpriseBusinessTaxInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */

@Mapper
public interface EnterpriseBusinessMapper extends BaseMapper<EnterpriseBusiness> {

    /**
     * 根据enterpriseKey获取相关信息
     */
    List<EnterpriseBusiness> getByEnterpriseKey(String EnterpriseKey);

    /**
     * 根据enterpriseKey放入数据
     */
    int setByEnterpriseKey(EnterpriseBusiness enterpriseBusiness, String enterpriseKey);

    /**
     * 修改已有的enterprise数据
     */
    int updateByEnterpriseKey(EnterpriseBusinessDTO enterpriseBusinessDTO);

    /**
     * 封装 企业信息和企业经营项目信息以及相关税率
     *
     * @param enterpriseKey
     * @return
     */
    List<EnterpriseBusinessTaxInfoDTO> getEnterpriseBusinessInfoList(String enterpriseKey);


}
