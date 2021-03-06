package com.study.security.config;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * 云存储配置信息
 *
 * @author allen
 */
@Configuration
@ConfigurationProperties(prefix = "oss")
@Getter
@Setter
public class CloudStorageConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    //类型 1：七牛  2：阿里云  3：腾讯云
    @Range(min = 1, max = 3, message = "类型错误")
    private Integer type;

    //七牛绑定的域名
    private String qiniuDomain;
    //七牛路径前缀
    private String qiniuPrefix;
    //七牛ACCESS_KEY
    private String qiniuAccessKey;
    //七牛SECRET_KEY
    private String qiniuSecretKey;
    //七牛存储空间名
    private String qiniuBucketName;

    //阿里云绑定的域名
    private String aliyunDomain;
    //阿里云路径前缀
    private String aliyunPrefix;
    //阿里云EndPoint
    private String aliyunEndPoint;
    //阿里云AccessKeyId
    private String aliyunAccessKeyId;
    //阿里云AccessKeySecret
    private String aliyunAccessKeySecret;
    //阿里云BucketName
    private String aliyunBucketName;

    //腾讯云绑定的域名
    private String qcloudDomain;
    //腾讯云路径前缀
    private String qcloudPrefix;
    //腾讯云AppId
    private Integer qcloudAppId;
    //腾讯云SecretId
    private String qcloudSecretId;
    //腾讯云SecretKey
    private String qcloudSecretKey;
    //腾讯云BucketName
    private String qcloudBucketName;
    //腾讯云COS所属地区
    private String qcloudRegion;

}
