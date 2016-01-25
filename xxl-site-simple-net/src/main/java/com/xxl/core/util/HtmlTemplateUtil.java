package com.xxl.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.xxl.service.ResourceBundle;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * HTML模板.Util
 * @author xuxueli
 */
public class HtmlTemplateUtil {
	
	/**
	 * 生成HTML字符串
	 * 
	 * @param freeMarkerConfig	: FreeMarker配置
	 * @param content			: 页面传参
	 * @param templatePathName	: 模板名称路径,相对于模板目录
	 */
	public static String generateString(Map<?, ?> content, String templateName) {
		String htmlText = "";
		try {
			// 通过指定模板名获取FreeMarker模板实例
			Template tpl = ResourceBundle.getInstance().getFreemarkerConfig().getConfiguration().getTemplate(templateName);
			htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlText;
	}

	/**
	 * 生成HTML页面 (依赖于SPring)
	 * 
	 * @param freeMarkerConfig	: FreeMarker配置
	 * @param content			: 页面传参
	 * @param templatePathName	: 模板名称路径,相对于模板目录
	 * @param filePathName		: 文件名称路径,相对于项目跟目录
	 * DEMO:HtmlTemplateUtil.generate(freemarkerConfig, new HashMap<String, Object>(), "net/index.ftl", "/index.ftl");
	 */
	public static void generate(Map<?, ?> content, String templatePathName, String filePathName) {
		Writer out = null;
		try {
			// mkdirs
			File htmlFile = new File(PathUtil.webPath() + filePathName);
            if (!htmlFile.getParentFile().exists()) {
                htmlFile.getParentFile().mkdirs();
            }
            // process
            Template template = ResourceBundle.getInstance().getFreemarkerConfig().getConfiguration().getTemplate(templatePathName);
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
