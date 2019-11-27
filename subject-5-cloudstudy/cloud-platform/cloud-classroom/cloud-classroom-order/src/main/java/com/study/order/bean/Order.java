package com.study.order.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Order {

    private Long id;

    private Date orderTime;

    private String phone;

    private Integer state;

    private Double total;

    private Integer userId;

    private Long courseId;

    private String courseName;

}