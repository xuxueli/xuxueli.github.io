package com.xxl.blog.core.util;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.Map;

/**
 * HTML模板.Util
 * @author xuxueli
 */
public class HtmlTemplateUtil {
	
	/**
	 * 生成HTML字符串
	 * 
	 * @param content			: 页面传参
	 */
	public static String generateString(FreeMarkerConfigurer freemarkerConfig, Map<?, ?> content, String templateName) {
		String htmlText = "";
		try {
			// 通过指定模板名获取FreeMarker模板实例
			Template tpl = freemarkerConfig.getConfiguration().getTemplate(templateName);
			htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlText;
	}

	/**
	 * 生成HTML页面 (依赖于SPring)
	 * 
	 * @param freemarkerConfig	: FreeMarker配置
	 * @param content			: 页面传参
	 * @param templatePathName	: 模板名称路径,相对于模板目录
	 * @param filePathName		: 文件名称路径,相对于项目跟目录
	 * DEMO:HtmlTemplateUtil.generate(freemarkerConfig, new HashMap<String, Object>(), "net/index.ftl", "/index.ftl");
	 */
	public static void generate(FreeMarkerConfigurer freemarkerConfig, Map<?, ?> content, String templatePathName, String filePathName) {

		Writer out = null;
		try {
			// mkdirs
			File htmlFile = new File(PathUtil.webPath() + filePathName);
            if (!htmlFile.getParentFile().exists()) {
                htmlFile.getParentFile().mkdirs();
            }
            // process
            Template template = freemarkerConfig.getConfiguration().getTemplate(templatePathName);
			out = new OutputStreamWriter(new FileOutputStream(PathUtil.webPath() + filePathName), "UTF-8");
			template.process(content, out);
			out.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
