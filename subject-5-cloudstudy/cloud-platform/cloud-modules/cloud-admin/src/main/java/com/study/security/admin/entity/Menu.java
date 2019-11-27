package com.study.security.admin.entity;

import com.study.security.admin.constant.AdminCommonConstant;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@ApiModel(value = "菜单信息")
@Data
@Table(name = "base_menu")
public class Menu {
    @Id
    private Integer id;

    private String code;

    private String title;

    @Column(name = "parent_id")
    private Integer parentId = AdminCommonConstant.ROOT;

    private String href;

    private String icon;

    private String type;

    private String description;

    private String path;

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