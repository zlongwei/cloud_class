package com.study.course.client;

import com.study.course.entity.Course;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by zhongxin on 2017/9/17.
 */
@Deprecated
@FeignClient(value = "cloud-classroom-essearch")
public interface CloudClassroomEsClient {

    @GetMapping(value = "/user/names")
    Map<Integer,String> getUserNames();

    @PostMapping("/cloud/classroom/es/course/save")
    String addEsCourse(Course courseDocument);

    @DeleteMapping("/cloud/classroom/es/course/delete/{id}")
    String deleteEsCourse(@PathVariable("id") String id);
}



