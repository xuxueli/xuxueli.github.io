package com.xxl.core.util;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.xxl.service.ResourceBundle;

/**
 * 邮件发送.Util
 * 
 * @author xuxueli
 */
public class MailUtil {
	private static Logger logger = LoggerFactory.getLogger(MailUtil.class);
	
	private static String host;
	private static String port;
	private static String username;
	private static String password;
	private static String sendFrom;
	private static String sendNick;
	static{
		Properties mailProp = PropertiesUtil.loadProperties("mail.properties");
		host = PropertiesUtil.getString(mailProp, "mail.host");
		port = PropertiesUtil.getString(mailProp, "mail.port");
		username = PropertiesUtil.getString(mailProp, "mail.username");
		password = PropertiesUtil.getString(mailProp, "mail.password");
		sendFrom = PropertiesUtil.getString(mailProp, "mail.sendFrom");
		sendNick = PropertiesUtil.getString(mailProp, "mail.sendNick");
	}
	
	/**
	 * 发送邮件 (完整版)(结合Spring)
	 * 
	 * @param javaMailSender: 发送Bean
	 * @param sendFrom		: 发送人邮箱
	 * @param sendNick		: 发送人昵称
	 * @param toAddress		: 收件人邮箱
	 * @param mailSubject	: 邮件主题
	 * @param mailBody		: 邮件正文
	 * @param mailBodyIsHtml: 邮件正文格式,true:HTML格式;false:文本格式
	 * @param files[]		: 附件
	 */
	public static boolean sendMailSpring(String toAddress, String mailSubject, String mailBody, boolean mailBodyIsHtml,File[] attachments) {
		JavaMailSender javaMailSender = ResourceBundle.getInstance().getJavaMailSender();
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, ArrayUtils.isNotEmpty(attachments), "UTF-8"); // 设置utf-8或GBK编码，否则邮件会有乱码;multipart,true表示文件上传
			
			helper.setFrom(sendFrom, sendNick);
			helper.setTo(toAddress);

			// 设置收件人抄送的名片和地址(相当于群发了)
			//helper.setCc(InternetAddress.parse(MimeUtility.encodeText("邮箱001") + " <@163.com>," + MimeUtility.encodeText("邮箱002") + " <@foxmail.com>"));

			helper.setSubject(mailSubject);
			helper.setText(mailBody, mailBodyIsHtml);
			
			// 添加附件
			if (ArrayUtils.isNotEmpty(attachments)) {
				for (File file : attachments) {
					helper.addAttachment(MimeUtility.encodeText(file.getName()), file);	
				}
			}
			
			// 群发
			//MimeMessage[] mailMessages = { mimeMessage };
			
			javaMailSender.send(mimeMessage);
			return true;
		} catch (Exception e) {
			logger.info("{}", e);
		}
		return false;
	}
	
	/**
	 * 发送邮件 (完整版) (纯JavaMail)
	 * 
	 * @param toAddress		: 收件人邮箱
	 * @param mailSubject	: 邮件主题
	 * @param mailBody		: 邮件正文
	 * @param mailBodyIsHtml: 邮件正文格式,true:HTML格式;false:文本格式
	 * @param inLineFile	: 内嵌文件
	 * @param files[]		: 附件
	 */
	public static boolean sendMail (String toAddress, String mailSubject, String mailBody, 
			boolean mailBodyIsHtml, File[] attachments){
        try {
			// 创建邮件发送类 JavaMailSender (用于发送多元化邮件，包括附件，图片，html 等    )
        	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        	mailSender.setHost(host); 			// 设置邮件服务主机    
        	mailSender.setUsername(username); 	// 发送者邮箱的用户名    
        	mailSender.setPassword(password); 	// 发送者邮箱的密码    
        	
			//配置文件，用于实例化java.mail.session    
			Properties pro = System.getProperties();
			pro.put("mail.smtp.auth", "true");		// 登录SMTP服务器,需要获得授权 (网易163邮箱新近注册的邮箱均不能授权,测试 sohu 的邮箱可以获得授权)
			pro.put("mail.smtp.socketFactory.port", port);
			pro.put("mail.smtp.socketFactory.fallback", "false");
			mailSender.setJavaMailProperties(pro);
			
			//创建多元化邮件 (创建 mimeMessage 帮助类，用于封装信息至 mimeMessage)
			MimeMessage mimeMessage = mailSender.createMimeMessage();						
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, ArrayUtils.isNotEmpty(attachments), "UTF-8");
			
			helper.setFrom(sendFrom, sendNick);
			helper.setTo(toAddress);

			helper.setSubject(mailSubject);
			helper.setText(mailBody, true); 
			
			// 添加内嵌文件，第1个参数为cid标识这个文件,第2个参数为资源
			//helper.addInline(MimeUtility.encodeText(inLineFile.getName()), inLineFile);	
			
			// 添加附件    
			if (ArrayUtils.isNotEmpty(attachments)) {
				for (File file : attachments) {
					helper.addAttachment(MimeUtility.encodeText(file.getName()), file);	
				}
			}
			
			mailSender.send(mimeMessage);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	static int total = 0;
	public static void main(String[] args) {
		
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 20; i++) {
			exec.execute(new Thread(new Runnable() {
				@Override
				public void run() {
					while(total < 1){
						String mailBody = "<html><head><meta http-equiv="
								+ "Content-Type"
								+ " content="
								+ "text/html; charset=gb2312"
								+ "></head><body><h1>新书快递通知</h1>你的新书快递申请已推送新书，请到<a href=''>空间"
								+ "</a>中查看</body></html>";
						
						sendMail("ovono802302@163.com", "测试邮件", mailBody, false, null);
						System.out.println(total);
						total++;
					}
				}
			}));
		}
	}
	
}
