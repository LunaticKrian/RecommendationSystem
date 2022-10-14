package com.mislab.core.systemcore.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Ascendable
 * @since 2022/10/14
 */
@Getter
@ToString
@AllArgsConstructor
public enum TaxRateEnum {
    //小规模纳税人+专票
    SMALLSCALE_SPECIALTICKET(0.03),
    //小规模纳税人+普票
    SMALLSCALE_GENERALTICKET(0.00),
    //一般纳税人+运输服务
    COMMONSCALE_TRANSSERVICE(0.09),
    //一般纳税人+车辆销售
    COMMONSCALE_VEHICLESALES(0.13),
    //一般纳税人+其他业务
    COMMONSCALE_OTHERSBUSINESS(0.06);

    private final Double taxRate;
}
