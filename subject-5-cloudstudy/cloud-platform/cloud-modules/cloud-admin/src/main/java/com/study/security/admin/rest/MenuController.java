package com.study.security.admin.rest;

import com.study.security.admin.biz.UserBiz;
import com.study.security.admin.biz.MenuBiz;
import com.study.security.admin.entity.Menu;
import com.study.security.admin.vo.AuthorityMenuTree;
import com.study.security.admin.vo.MenuTree;
import com.study.security.common.rest.BaseController;
import com.study.security.common.util.TreeUtil;
import com.study.security.admin.constant.AdminCommonConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MenuController
 * @Description 菜单 控制器
 * @Author 网易云课堂微专业-java高级开发工程师
 * @Date 2019/6/11 15:39
 * @Version 1.0
 */
@Api(tags = "菜单模块")
@RestController
@RequestMapping("menu")
public class MenuController extends BaseController<MenuBiz, Menu> {
    @Autowired
    private UserBiz userBiz;

    @ApiOperation(value = "查询查单列表")
    @GetMapping(value = "/list")
    public List<Menu> list(String title) {
        Example example = new Example(Menu.class);
        if (StringUtils.isNotBlank(title)) {
            example.createCriteria().andLike("title", "%" + title + "%");
        }
        return baseBiz.selectByExample(example);
    }

    @GetMapping(value = "/tree")
    public List<MenuTree> getTree(String title) {
        Example example = new Example(Menu.class);
        if (StringUtils.isNotBlank(title)) {
            example.createCriteria().andLike("title", "%" + title + "%");
        }
        return getMenuTree(baseBiz.selectByExample(example), AdminCommonConstant.ROOT);
    }

    @GetMapping(value = "/system")
    public List<Menu> getSystem() {
        Menu menu = new Menu();
        menu.setParentId(AdminCommonConstant.ROOT);
        return baseBiz.selectList(menu);
    }

    @GetMapping(value = "/menuTree")
    public List<MenuTree> listMenu(Integer parentId) {
        try {
            if (parentId == null) {
                parentId = this.getSystem().get(0).getId();
            }
        } catch (Exception e) {
            return new ArrayList<MenuTree>();
        }
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        Example example = new Example(Menu.class);
        Menu parent = baseBiz.selectById(parentId);
        example.createCriteria().andLike("path", parent.getPath() + "%").andNotEqualTo("id",parent.getId());
        return getMenuTree(baseBiz.selectByExample(example), parent.getId());
    }

    @GetMapping(value = "/authorityTree")
    public List<AuthorityMenuTree> listAuthorityMenu() {
        List<AuthorityMenuTree> trees = new ArrayList<AuthorityMenuTree>();
        AuthorityMenuTree node = null;
        for (Menu menu : baseBiz.selectListAll()) {
            node = new AuthorityMenuTree();
            node.setText(menu.getTitle());
            BeanUtils.copyProperties(menu, node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees, AdminCommonConstant.ROOT);
    }

    @GetMapping(value = "/user/authorityTree")
    public List<MenuTree> listUserAuthorityMenu(Integer parentId){
        int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        try {
            if (parentId == null) {
                parentId = this.getSystem().get(0).getId();
            }
        } catch (Exception e) {
            return new ArrayList<MenuTree>();
        }
        return getMenuTree(baseBiz.getUserAuthorityMenuByUserId(userId),parentId);
    }

    @GetMapping(value = "/user/system")
    public List<Menu> listUserAuthoritySystem() {
        int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        return baseBiz.getUserAuthoritySystemByUserId(userId);
    }

    private List<MenuTree> getMenuTree(List<Menu> menus,int root) {
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        for (Menu menu : menus) {
            node = new MenuTree();
            BeanUtils.copyProperties(menu, node);
            node.setLabel(menu.getTitle());
            trees.add(node);
        }
        return TreeUtil.bulid(trees,root) ;
    }


}
