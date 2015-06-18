package com.xxl.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

/**
 * 资源实例
 * @author xuxueli
 */
public class ResourceBundle {

	private static final ResourceBundle resource = new ResourceBundle();

	public static ResourceBundle getInstance() {
		return resource;
	}

	private FreeMarkerConfig freemarkerConfig;
	private JavaMailSender javaMailSender;
	private IEmailSendService emailSendService;

	public FreeMarkerConfig getFreemarkerConfig() {
		return freemarkerConfig;
	}
	public void setFreemarkerConfig(FreeMarkerConfig freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
	}
	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	public IEmailSendService getEmailSendService() {
		return emailSendService;
	}
	public void setEmailSendService(IEmailSendService emailSendService) {
		this.emailSendService = emailSendService;
	}
	
}
