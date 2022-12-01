package com.mislab.core.systemcore.common.enums.calculation;

public class CommentConstant {

    // 兼营销纳税人 Concurrent Sales Taxpayers
    public final static Integer NOT_CONCURRENT_SALES_TAXPAYERS = 0;
    public final static Integer IS_CONCURRENT_SALES_TAXPAYERS = 1;

    // 纳税人资格 Taxpayer Eligibility
    // 一般纳税人：
    public final static Integer Taxpayer_Eligibility_GENERAL_TAXPAYERS = 0;
    // 小规模纳税人：
    public final static Integer Taxpayer_Eligibility_SMALL_SCALE_TAXPAYERS = 1;

    // 发票类型 Invoice Type
    // 专票：
    public final static Integer INVOICE_TYPE_SPECIAL_TICKETS = 0;
    // 普票：
    public final static Integer INVOICE_TYPE_GENERAL_TICKETS = 1;
    // 专票 + 普票：
    public final static Integer INVOICE_TYPE_MULTI_TICKETS = 2;

    // 企业类型：
    // 一般企业的附加税税率
    public final static Integer TAX_FOR_GENERAL_ENTERPRISES = 1;
    public final static Double TAX_RATE_FOR_GENERAL_ENTERPRISES = 0.12;
    // 小型微利企业
    public final static Integer TAX_FOR_MICRO_PROFIT_ENTERPRISES = 2;
    public final static Double TAX_RATE_FOR_MICRO_PROFIT_ENTERPRISES = TAX_RATE_FOR_GENERAL_ENTERPRISES * 0.5;


}
