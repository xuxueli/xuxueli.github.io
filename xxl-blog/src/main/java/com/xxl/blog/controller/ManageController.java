package com.xxl.blog.controller;

import com.xxl.blog.controller.annotation.PermessionLimit;
import com.xxl.blog.core.result.ReturnT;
import com.xxl.blog.service.IHtmlGenerateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 控制面板
 * @author xuxueli
 */
@Controller
@RequestMapping("/manage")
public class ManageController {
    private static transient Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Resource
    private IHtmlGenerateService htmlGenerateService;



    @RequestMapping(value="/generateHtml")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> test(){
        long start = System.currentTimeMillis();
        logger.info("全站静态化... start:{}", start);

        htmlGenerateService.generateNetHtml();

        long end = System.currentTimeMillis();
        logger.info("全站静态化... end:{}, cost:{}", end, end - start);
        return new ReturnT<String>(null);
    }

}
