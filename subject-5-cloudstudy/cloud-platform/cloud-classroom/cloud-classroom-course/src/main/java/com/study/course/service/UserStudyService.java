package com.study.course.service;

import com.study.course.entity.UserStudy;
import com.study.course.common.JwtUtil;
import com.study.course.mapper.UserStudyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by andy on 2019/1/24.
 */
@Service
public class UserStudyService {
    @Autowired
    UserStudyMapper userStudyMapper;
    public int addUserStudy(UserStudy userStudy) {
        return userStudyMapper.insert(userStudy);
    }

    public List<UserStudy> getAllUserStudy() {
        return userStudyMapper.getAllUserStudy();
    }
    public List<UserStudy> getAllUserStudiesOfCurrentUser() {
        int userId = Integer.valueOf( JwtUtil.getUserId());
        return userStudyMapper.getAllUserStudiesOfCurrentUser(userId);
    }

    public List<UserStudy> getUserStudiesByPage(Integer page, Integer size,String name) {
        int start = (page-1)*size;
        return userStudyMapper.getUserStudiesByPage(start,size,name);
    }


    public Long getCountByKey(String name) {
        return userStudyMapper.getCountByKey(name);
    }

    public UserStudy getUserStudy(String id) {
        return userStudyMapper.selectByPrimaryKey(id);
    }


    public int updateUserStudy(UserStudy userStudy) {
        return userStudyMapper.updateByPrimaryKeySelective(userStudy);
    }

    public int deleteUserStudy(String id) {
        return userStudyMapper.deleteByPrimaryKey(id);
    }

}
