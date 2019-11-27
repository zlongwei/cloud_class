package com.study.course.service;

import com.study.course.entity.Course;
import com.study.course.common.JwtUtil;
import com.study.course.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by andy on 2018/1/24.
 */
@Service
public class CourseService {
    @Autowired
    CourseMapper courseMapper;

    public int addCourse(Course course) {
        if (course != null) {
            course.setTeacherId(Long.parseLong(JwtUtil.getUserId()));
        }
        course.setUpdateTime(new Date(System.currentTimeMillis()));
        return courseMapper.insert(course);
    }

    public List<Course> getAllCourse() {
        return courseMapper.getAllCourse();
    }

    public List<Course> getCoursesByPage(Integer page, Integer size, String name) {
        int start = (page - 1) * size;
        return courseMapper.getCoursesByPage(start, size, name);
    }

    public Long getCountByKey(String name) {
        return courseMapper.getCountByKey(name);
    }

    public Course getCourse(Long id) {
        return courseMapper.selectByPrimaryKey(id);
    }


    public int updateCourse(Course course) {
        course.setUpdateTime(new Date(System.currentTimeMillis()));
        return courseMapper.updateByPrimaryKeySelective(course);
    }

    public int deleteCourse(Long id) {
        return courseMapper.deleteByPrimaryKey(id);
    }

}
