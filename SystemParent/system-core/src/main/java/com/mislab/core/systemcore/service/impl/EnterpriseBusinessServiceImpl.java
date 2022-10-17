package com.mislab.core.systemcore.service.impl;

import com.mislab.common.result.R;
import com.mislab.core.systemcore.mapper.EnterpriseMapper;
import com.mislab.core.systemcore.pojo.dto.EnterpriseBusinessDTO;
import com.mislab.core.systemcore.pojo.entity.EnterpriseBusiness;
import com.mislab.core.systemcore.mapper.EnterpriseBusinessMapper;
import com.mislab.core.systemcore.pojo.dto.Enterprise2VO;
import com.mislab.core.systemcore.service.EnterpriseBusinessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class EnterpriseBusinessServiceImpl extends ServiceImpl<EnterpriseBusinessMapper, EnterpriseBusiness> implements EnterpriseBusinessService {

    @Autowired
    EnterpriseBusinessMapper enterpriseBusinessMapper;

    @Autowired
    EnterpriseMapper enterpriseMapper;
    /**
     * 在第一个页面已将建好enterpriseBusiness，只需修改
     * @param EPBs
     * @return
     */
    @Override
    public R updateByEnterpriseKey(List<EnterpriseBusinessDTO> EPBs,String enterpriseKey) {
        for (EnterpriseBusinessDTO enterpriseBusiness: EPBs) {
            enterpriseBusiness.setEnterpriseKey(enterpriseKey);
            enterpriseBusinessMapper.updateByEnterpriseKey(enterpriseBusiness);
        }
        return R.SUCCESS();
    }

    @Override
    public List<EnterpriseBusiness> getByEnterpriseKey(String enterpriseKey) {
        List<EnterpriseBusiness> EPBs = enterpriseBusinessMapper.getByEnterpriseKey(enterpriseKey);
        return EPBs;
    }



    @Override
    public int setSTATAC(String enterpriseKey,int ST,double AT, double AC) {
        enterpriseMapper.updateSAAEnterprise(enterpriseKey,ST,AT,AC);
        return 1;
    }

    @Override
    public Enterprise2VO getDateIITable(String enterpriseKey) {
        Enterprise2VO enterprise2VO = enterpriseMapper.selectDateIITable(enterpriseKey);
        return enterprise2VO;
    }

}
