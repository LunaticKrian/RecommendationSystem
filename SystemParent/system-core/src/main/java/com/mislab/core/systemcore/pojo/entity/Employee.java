package com.mislab.core.systemcore.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author krian
 * @since 2022-09-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Employee对象", description="")
public class Employee implements Serializable {

    public static  final Integer STATUS_NORMAL = 1;
    public static  final Integer STATUS_LOCKED = 0;
    public static final String USER_AVATAR = "";

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "员工编号")
    private String uid;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty(value = "电话号码")
    private String mobile;

    @ApiModelProperty(value = "用户头像")
    private String headImg;

    @ApiModelProperty(value = "状态（0：锁定 1：正常）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除(1:已删除，0:未删除)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;
}
