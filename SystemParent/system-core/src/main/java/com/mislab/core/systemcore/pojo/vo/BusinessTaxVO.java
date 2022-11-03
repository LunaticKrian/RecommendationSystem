package com.mislab.core.systemcore.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessTaxVO {
    private String businessName;

    private double VATRate;

    private double amount;
}
