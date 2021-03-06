package com.study.course.elasticsearch.service;

import com.study.course.entity.Course;

import java.util.List;

/**
 * @author andy
 * @version 0.1
 * @date 2018/12/13 15:32
 */
public interface CourseEsSearchService extends BaseSearchService<Course> {
    /**
     * 保存
     * @auther: andy
     * @date: 2018/12/13 16:02
     */
    void save(Course... course);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Course getById(Long id);

    /**
     * 查询全部
     * @return
     */
    List<Course> getAll();

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 清空索引
     */
    void deleteAll();

}
