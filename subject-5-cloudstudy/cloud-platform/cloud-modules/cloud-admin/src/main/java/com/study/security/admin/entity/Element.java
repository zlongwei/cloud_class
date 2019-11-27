package com.study.security.admin.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@ApiModel(value = "资源")
@Data
@Table(name = "base_element")
public class Element {
    @Id
    private Integer id;

    private String code;

    private String type;

    private String name;

    private String uri;

    @Column(name = "menu_id")
    private String menuId;

    @Column(name = "parent_id")
    private String parentId;

    private String path;

    private String method;

    private String description;

    @Column(name = "crt_time")
    private Date crtTime;

    @Column(name = "crt_user")
    private String crtUser;

    @Column(name = "crt_name")
    private String crtName;

    @Column(name = "crt_host")
    private String crtHost;

}