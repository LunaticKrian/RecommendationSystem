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
public enum EnterpriseStateEnum {

    //企业信息已不存在
    ALREADY_DELETE(0),
    //企业信息正在填写中，未完成
    STILL_WRITING(1),
    //企业信息已提交，已完成
    ALREADY_COMPLETE(2);

    private final Integer code;
}
