package com.study.course.controller;

import com.study.course.entity.Course;
import com.study.course.bean.FileUploadResult;
import com.study.course.bean.RespBean;
import com.study.course.client.CloudAdminClient;
import com.study.course.client.CloudOssClient;
import com.study.course.elasticsearch.service.CourseEsSearchService;
import com.study.course.service.CourseService;
import com.study.security.id.IGenerateIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 课程
 */
@RestController
@RequestMapping("/cloud/classroom/course")
public class CourseController {
    @Autowired
    CourseService courseService;

    @Autowired
    CloudAdminClient client;

    @Autowired
    IGenerateIdService redisGenerateIdService;

    @Autowired
    CloudOssClient cloudOssClient;

    @Autowired
    CourseEsSearchService courseEsSearchService;
    //@Autowired
    //CloudClassroomEsClient cloudClassroomEsClient;

    /**
     * 添加课程
     *
     * @param course
     * @param uploadFile
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public RespBean addCourse(Course course, @RequestParam("file") MultipartFile uploadFile) {
        FileUploadResult fileUploadResult = cloudOssClient.fileUpload(uploadFile);
        if ("success".equalsIgnoreCase(fileUploadResult.getResponse())) {
            course.setImage(fileUploadResult.getName());
            course.setId(redisGenerateIdService.getId());
            if (courseService.addCourse(course) == 1) {
                try {
                    //Feign调用转本地调用
                    //cloudClassroomEsClient.addEsCourse(course);
                    courseEsSearchService.save(course);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return RespBean.ok("添加成功!");
            }
        }
        return RespBean.error("添加失败!");
    }

    /**
     * 根据id获取课程
     *
     * @param id 课程id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Course getCourse(@PathVariable Long id) {
        return courseService.getCourse(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Course> getAllCourses() {
        return courseService.getAllCourse();
    }

    @RequestMapping(value = "/page/list", method = RequestMethod.GET)
    public Map<String, Object> getCoursesByPage(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer limit, String name) {
        name = Optional.ofNullable(name).orElse("");
        Long count = courseService.getCountByKey(name);
        List<Course> courses = null;
        courses = courseService.getCoursesByPage(page, limit, name);
        Map<Integer, String> userNames = client.getUserNames();
        if (userNames != null) {
            courses.stream().forEach(item -> {
                item.setTeacherName(userNames.get(item.getTeacherId()));
            });
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Courses", courses);
        map.put("count", count);
        return map;
    }

    @PostMapping(value = "/{id}")
    public RespBean updateCourse(Course course, HttpServletRequest request) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile uploadFile = multipartRequest.getFile("file");
        if (uploadFile != null) {
            FileUploadResult fileUploadResult = cloudOssClient.fileUpload(uploadFile);
            if ("success".equalsIgnoreCase(fileUploadResult.getResponse())) {
                try {
                    cloudOssClient.removeFile(course.getImage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                course.setImage(fileUploadResult.getName());
            }
        }
        if (courseService.updateCourse(course) == 1) {
            try {
                //cloudClassroomEsClient.addEsCourse(course);
                courseEsSearchService.save(course);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public RespBean deleteCourse(@PathVariable Long id) {
        if (courseService.deleteCourse(id) == 1) {
            try {
                //cloudClassroomEsClient.deleteEsCourse(id);
                courseEsSearchService.delete(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @GetMapping("/names")
    public Map<Long, String> getCourseNames(){
        return this.getAllCourses().stream().collect(Collectors.toMap(item -> item.getId(), item -> item.getName()));
    }

}
