package com.study.course.controller;

import com.study.course.entity.Course;
import com.study.course.bean.RespBean;
import com.study.course.entity.UserStudy;
import com.study.course.common.JwtUtil;
import com.study.course.service.CourseService;
import com.study.course.service.UserStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 课程
 */
@RestController
@RequestMapping("/cloud/classroom/user_study")
public class UserStudyController {
    @Autowired
    UserStudyService userStudyService;

    @Autowired
    CourseService courseService;

    @RequestMapping(method = RequestMethod.POST)
    public RespBean addUserStudy(@RequestBody UserStudy userStudy) {
        String userId = JwtUtil.getUserId();
        if (userStudyService.addUserStudy(userStudy) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserStudy getUserStudy(@PathVariable String id) {
        return userStudyService.getUserStudy(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<UserStudy> getAllUserStudies() {
        return userStudyService.getAllUserStudy();
    }

    @RequestMapping(value = "/page/list", method = RequestMethod.GET)
    public Map<String, Object> getUserStudiesByPage(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size, String name) {
        name = Optional.ofNullable(name).orElse("");
        Long count = userStudyService.getCountByKey(name);
        List<UserStudy> userStudies = new ArrayList<>();
        userStudies = userStudyService.getUserStudiesByPage(page, size, name);
        Map<String, Object> map = new HashMap<>();
        map.put("UserStudies", userStudies);
        map.put("count", count);
        return map;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public RespBean updateUserStudy(UserStudy userStudy) {
        if (userStudyService.updateUserStudy(userStudy) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public RespBean deleteUserStudy(@PathVariable String id) {
        if (userStudyService.deleteUserStudy(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }


    @RequestMapping(value = "/current/user/list", method = RequestMethod.GET)
    public List<UserStudy> getAllUserStudiesOfCurrentUser() {
        List<UserStudy> userStudies = userStudyService.getAllUserStudiesOfCurrentUser();
        userStudies.stream().forEach(item -> {
            Course course = courseService.getCourse(item.getCourseId());
            if (course != null) {
                item.setCourseName(course.getName());
            }
        });
        return userStudies;
    }

}
