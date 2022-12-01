package com.mislab.core.systemcore.common.enums.calculation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 【计算使用】企业业务既定税率
 */
@Getter
@ToString
@AllArgsConstructor
public enum BusinessTaxEnum {
    VAT_RATE_9(0.09),
    VAT_RATE_3(0.03),
    VAT_RATE_0(0.00);



    private final Double taxRate;
}
