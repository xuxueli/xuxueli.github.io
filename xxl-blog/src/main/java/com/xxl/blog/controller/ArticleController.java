package com.xxl.blog.controller;

import com.xxl.blog.controller.annotation.PermessionLimit;
import com.xxl.blog.core.model.ArticleGroup;
import com.xxl.blog.core.model.ArticleInfo;
import com.xxl.blog.core.result.ReturnT;
import com.xxl.blog.dao.IArticleGroupDao;
import com.xxl.blog.dao.IArticleInfoDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xuxueli on 17/4/14.
 */
@Controller
@RequestMapping("/article")
public class ArticleController {

    public static final int ARTICLE_GROUP_TOP = 0;	// 顶级分组

    @Autowired
    private IArticleGroupDao articleGroupDao;
    @Autowired
    private IArticleInfoDao articleInfoDao;

    @RequestMapping("")
    @PermessionLimit
    public String groupList(Model model, int a) {

        List<ArticleGroup> groupList = articleGroupDao.getByParentId(ARTICLE_GROUP_TOP);
        model.addAttribute("groupList", groupList);

        ArticleInfo articleInfo = articleInfoDao.load(a);
        model.addAttribute("articleInfo", articleInfo);

        return "article/articleInfo";
    }

    @RequestMapping("/addGroup")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> addGroup(ArticleGroup articleGroup) {

        // valid
        if (StringUtils.isBlank(articleGroup.getName())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入分组名称");
        }
        if (articleGroup.getParentId() != ARTICLE_GROUP_TOP) {
            ArticleGroup parent = articleGroupDao.load(articleGroup.getParentId());
            if (parent == null) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, "父分组ID非法");
            }
            if (parent.getParentId() != ARTICLE_GROUP_TOP) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, "父分组必须为顶级分组");
            }
        }

        int ret = articleGroupDao.add(articleGroup);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/updateGroup")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> updateGroup(ArticleGroup articleGroup) {

        // valid
        if (StringUtils.isBlank(articleGroup.getName())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入分组名称");
        }
        if (articleGroup.getParentId() != ARTICLE_GROUP_TOP) {
            ArticleGroup parent = articleGroupDao.load(articleGroup.getParentId());
            if (parent == null) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, "父分组ID非法");
            }
            if (parent.getParentId() != ARTICLE_GROUP_TOP) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, "父分组必须为顶级分组");
            }
        }

        int ret = articleGroupDao.update(articleGroup);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/deleteGroup")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> deleteGroup(int id) {

        List<ArticleGroup> groupList = articleGroupDao.getByParentId(id);
        if (CollectionUtils.isNotEmpty(groupList)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "存在子分组，不可删除");
        }
        List<ArticleInfo> articleInfoList = articleInfoDao.pageList(0, 1, Arrays.asList(id), -1);
        if (CollectionUtils.isNotEmpty(articleInfoList)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "该分组下存在文章，不可删除");
        }

        int ret = articleGroupDao.delete(id);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }



}
