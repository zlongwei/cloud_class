package com.study.order.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class UserStudy {

    private Long id;

    private Long courseId;

    private Integer userId;

    private Date startTime;

}