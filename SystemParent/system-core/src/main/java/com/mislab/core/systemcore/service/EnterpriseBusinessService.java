package com.mislab.core.systemcore.service;

import com.mislab.common.result.R;
import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mislab.core.systemcore.pojo.dto.Enterprise2VO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
public interface EnterpriseBusinessService extends IService<EnterpriseBusiness> {
    /**
     * 根据enterpriseKey修改数据
     */
    public R updateByEnterpriseKey(List<EnterpriseBusiness> enterpriseBusiness);

    public List<EnterpriseBusiness> getByEnterpriseKey(String enterpriseKey);

    /**
     * 根据enterpris修改年营业额和是否兼营销售纳税人,年经营成本
     * @param enterpriseKey
     * @param ST
     * @param AT
     * @param AC
     * @return
     */
    int setSTATAC(String enterpriseKey,int ST,double AT,double AC);

    Enterprise2VO getDateIITable(String enterpriseKey);
}
