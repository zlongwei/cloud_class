package com.study.security.admin.entity;

import com.study.security.admin.constant.AdminCommonConstant;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@ApiModel(value = "角色")
@Data
@Table(name = "base_group")
public class Group {
    @Id
    private Integer id;

    private String code;

    private String name;

    @Column(name = "parent_id")
    private Integer parentId;

    private String path;

    private String type;

    @Column(name = "group_type")
    private Integer groupType = AdminCommonConstant.DEFAULT_GROUP_TYPE;

    private String description;

    @Column(name = "crt_time")
    private Date crtTime;

    @Column(name = "crt_user")
    private String crtUser;

    @Column(name = "crt_name")
    private String crtName;

    @Column(name = "crt_host")
    private String crtHost;

    @Column(name = "upd_time")
    private Date updTime;

    @Column(name = "upd_user")
    private String updUser;

    @Column(name = "upd_name")
    private String updName;

    @Column(name = "upd_host")
    private String updHost;

}