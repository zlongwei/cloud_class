package com.study.order.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Course {

    private Long id;

    private String name;

    private String image;

    private Double price;

    private String synopsis;

    private String description;

    private String classification;

    private Long teacherId;

    private Integer status;

    private Date startTime;

}