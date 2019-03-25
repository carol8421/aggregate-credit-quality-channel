package com.aggregate.framework.gzt.entity;

import com.aggregate.framework.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tbl_upstream")
public class Upstream extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 用户公钥
     */
    @TableField("public_key")
    private String publicKey;

    /**
     * 用户client_id
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 用户client_secret
     */
    @TableField("client_secret")
    private String clientSecret;

}
