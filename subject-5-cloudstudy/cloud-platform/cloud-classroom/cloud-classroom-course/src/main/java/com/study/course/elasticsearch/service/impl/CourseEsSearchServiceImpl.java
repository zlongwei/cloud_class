package com.study.course.elasticsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.study.course.elasticsearch.repository.CourseDocumentRepository;
import com.study.course.elasticsearch.service.CourseEsSearchService;
import com.study.course.entity.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * elasticsearch 搜索引擎 service实现
 * @author andy
 * @version 0.1
 * @date 2018/12/13 15:33
 */
@Service
public class CourseEsSearchServiceImpl extends BaseSearchServiceImpl<Course> implements CourseEsSearchService {
    private Logger log = LoggerFactory.getLogger(getClass());
//    @Resource
//    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private CourseDocumentRepository courseDocumentRepository;

    @Override
    public void save(Course... course) {
//        elasticsearchTemplate.putMapping(Course.class);
        if(course.length > 0){
            /*Arrays.asList(courseDocuments).parallelStream()
                    .map(courseDocumentRepository::save)
                    .forEach(courseDocument -> log.info("【保存数据】：{}", JSON.toJSONString(courseDocument)));*/
            log.info("【保存索引】：{}",JSON.toJSONString(courseDocumentRepository.saveAll(Arrays.asList(course))));
        }
    }

    @Override
    public void delete(Long id) {
        courseDocumentRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        courseDocumentRepository.deleteAll();
    }

    @Override
    public Course getById(Long id) {
        return courseDocumentRepository.findById(id).get();
    }

    @Override
    public List<Course> getAll() {
        List<Course> list = new ArrayList<>();
        courseDocumentRepository.findAll().forEach(item->list.add((Course) item));
        return list;
    }

}
