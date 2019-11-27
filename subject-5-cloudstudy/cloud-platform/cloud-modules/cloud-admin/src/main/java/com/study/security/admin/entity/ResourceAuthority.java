package com.study.security.admin.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@ApiModel(value = "角色权限")
@Data               // get,set
@NoArgsConstructor  // lombok无参构造器
@Table(name = "base_resource_authority")
public class ResourceAuthority {
    @Id
    private Integer id;

    @Column(name = "authority_id")
    private String authorityId;

    @Column(name = "authority_type")
    private String authorityType;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "parent_id")
    private String parentId;

    private String path;

    private String description;

    @Column(name = "crt_time")
    private Date crtTime;

    @Column(name = "crt_user")
    private String crtUser;

    @Column(name = "crt_name")
    private String crtName;

    @Column(name = "crt_host")
    private String crtHost;

    public ResourceAuthority(String authorityType, String resourceType) {
        this.authorityType = authorityType;
        this.resourceType = resourceType;
    }

}