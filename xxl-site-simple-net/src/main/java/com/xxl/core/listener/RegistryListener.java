package com.xxl.core.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.xxl.core.chat.netty.NettyChatServer;
import com.xxl.core.chat.socketio.SioChatServer;
import com.xxl.core.thread.EmailSendConsumerThread;
import com.xxl.core.thread.EmailSendProductThread;
import com.xxl.service.IEmailSendService;
import com.xxl.service.ITriggerService;
import com.xxl.service.ResourceBundle;

/**
 * 注册监听器
 * @author xuxueli
 */
public class RegistryListener implements ServletContextListener {
	private static transient Logger logger = LoggerFactory.getLogger(RegistryListener.class);
	
	public void contextDestroyed(ServletContextEvent context) {
		
	}

	public void contextInitialized(ServletContextEvent sc) {
		logger.info("[5i net registry listener starting...]");
		
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc.getServletContext());
		
		// 资源实例
		ResourceBundle resource = ResourceBundle.getInstance();
		resource.setFreemarkerConfig((FreeMarkerConfig) context.getBean("freemarkerConfig"));
		resource.setJavaMailSender((JavaMailSender) context.getBean("javaMailSender"));
		resource.setEmailSendService((IEmailSendService) context.getBean("emailSendService"));
		resource.setTriggerService((ITriggerService) context.getBean("triggerService"));
		
		// 开启多线程
		ExecutorService exec = Executors.newCachedThreadPool();
		
		// 邮件发送
		exec.execute(new Thread(new EmailSendProductThread()));
		exec.execute(new Thread(new EmailSendConsumerThread()));
		
		// netty-socketio 服务器启动
		new SioChatServer().init();
		// netty websocket 服务器启动
		new NettyChatServer().init();
		
		// 全站静态化
		resource.getTriggerService().generateNetHtml();
		
		logger.info("[5i net registry listener finished...]");
	}

}
