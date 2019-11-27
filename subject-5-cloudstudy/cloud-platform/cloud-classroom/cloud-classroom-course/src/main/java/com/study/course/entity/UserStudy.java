package com.study.course.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserStudy {

    private Long id;

    private Long courseId;

    private Integer userId;

    private Date startTime;

    private String courseName;

}