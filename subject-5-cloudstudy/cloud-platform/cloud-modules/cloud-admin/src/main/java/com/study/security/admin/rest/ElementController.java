package com.study.security.admin.rest;

import com.study.security.admin.biz.ElementBiz;
import com.study.security.admin.biz.UserBiz;
import com.study.security.admin.entity.Element;
import com.study.security.common.msg.ObjectRestResponse;
import com.study.security.common.msg.TableResultResponse;
import com.study.security.common.rest.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName ElementController
 * @Description 资源元素 控制器
 * @Author 网易云课堂微专业-java高级开发工程师
 * @Date 2019/6/11 15:39
 * @Version 1.0
 */
@Api(tags = "资源模块")
@RestController
@RequestMapping("element")
public class ElementController extends BaseController<ElementBiz, Element> {
    @Autowired
    private UserBiz userBiz;

    @ApiOperation(value = "分页查询资源信息")
    @GetMapping(value = "/list")
    public TableResultResponse<Element> page(@RequestParam(defaultValue = "10") int limit,
                                             @RequestParam(defaultValue = "1") int offset, String name, @RequestParam(defaultValue = "0") int menuId) {
        Example example = new Example(Element.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuId", menuId);
        if (StringUtils.isNotBlank(name)) {
            criteria.andLike("name", "%" + name + "%");
        }
        List<Element> elements = baseBiz.selectByExample(example);
        return new TableResultResponse<Element>(elements.size(), elements);
    }

    @GetMapping(value = "/user")
    public ObjectRestResponse<Element> getAuthorityElement(String menuId) {
        int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        List<Element> elements = baseBiz.getAuthorityElementByUserId(userId + "", menuId);
        return new ObjectRestResponse<List<Element>>().data(elements);
    }

    @GetMapping(value = "/user/menu")
    public ObjectRestResponse<Element> getAuthorityElement() {
        int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        List<Element> elements = baseBiz.getAuthorityElementByUserId(userId + "");
        return new ObjectRestResponse<List<Element>>().data(elements);
    }
}
