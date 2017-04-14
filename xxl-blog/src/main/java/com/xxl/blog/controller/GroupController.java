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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xuxueli on 17/4/14.
 */
@Controller
@RequestMapping("/group")
public class GroupController {

    public static final int ARTICLE_GROUP_TOP = 0;	// 顶级分组
    public static final int ARTICLE_PAGESIZE = 2;

    @Autowired
    private IArticleGroupDao articleGroupDao;
    @Autowired
    private IArticleInfoDao articleInfoDao;


    @RequestMapping("")
    public String groupList(Model model, @RequestParam(required = false, defaultValue = "-1") int g
            , @RequestParam(required = false, defaultValue = "1") int p) {

        // group list
        List<ArticleGroup> groupList = articleGroupDao.getByParentId(ARTICLE_GROUP_TOP);
        model.addAttribute("groupList", groupList);

        // selected groupA + groupB
        int groupAId = -1;
        int groupBId = -1;
        if (CollectionUtils.isNotEmpty(groupList)) {
            for (ArticleGroup groupA: groupList) {
                if (groupA.getId() == g) {
                    groupAId = groupA.getId();
                    break;
                } else {
                    if (CollectionUtils.isNotEmpty(groupA.getChildren())) {
                        for (ArticleGroup groupB: groupA.getChildren()) {
                            if (groupB.getId() == g) {
                                groupAId = groupA.getId();
                                groupBId = groupB.getId();
                            }
                        }
                    }
                    if (groupAId > 0 && groupBId > 0) {
                        break;
                    }
                }
            }

            if (groupAId == -1) {
                groupAId = groupList.get(0).getId();
                groupBId = -1;
            }
        }
        model.addAttribute("groupAId", groupAId);
        model.addAttribute("groupBId", groupBId);

        // article under group
        List<Integer> groupIds = new ArrayList<Integer>();
        if (groupBId > -1) {
            groupIds.add(groupBId);
        } else if (groupAId > -1) {
            for (ArticleGroup item: groupList) {
                if (item.getId() == groupAId && item.getChildren()!=null && item.getChildren().size() > 0) {
                    for (ArticleGroup itemB: item.getChildren()) {
                        groupIds.add(itemB.getId());
                    }
                }
            }
        }

        // article list
        List<ArticleInfo> pageList = null;
        int pageTotal = 0;
        if (CollectionUtils.isNotEmpty(groupIds)) {
            int offset = (p-1)*ARTICLE_PAGESIZE;

            pageList = articleInfoDao.pageList(offset, ARTICLE_PAGESIZE, groupIds, 0);
            int total = articleInfoDao.pageListCount(offset, ARTICLE_PAGESIZE, groupIds, 0);

            pageTotal = (total%ARTICLE_PAGESIZE==0)?(total/ARTICLE_PAGESIZE):(total/ARTICLE_PAGESIZE+1);
        }
        model.addAttribute("pageList", pageList);
        model.addAttribute("pageTotal", pageTotal);
        model.addAttribute("pageNum", p);

        return "article/groupList";
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
