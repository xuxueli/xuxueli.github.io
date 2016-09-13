package com.xxl.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.EmailSendInfo;
import com.xxl.dao.IEmailSendInfoDao;

/**
 * 邮件发送信息
 * @author xuxueli
 */
@Repository
public class EmailSendInfoDaoImpl implements IEmailSendInfoDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	/*
	 * 保存
	 * @see com.xxl.dao.IEmailSendInfoDao#insert(com.xxl.core.model.main.EmailSendInfo)
	 */
	@Override
	public int insert(EmailSendInfo email) {
		return sqlSessionTemplate.insert("EmailSendInfoMapper.insert", email);
	}

	/*
	 * 查询邮件列表,根据status
	 * @see com.xxl.dao.IEmailSendInfoDao#getListByStatus(int, int)
	 */
	@Override
	public List<EmailSendInfo> getListByStatus(int emailStatus, int pagesize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("emailStatus", emailStatus);
		params.put("pagesize", pagesize);
		return sqlSessionTemplate.selectList("EmailSendInfoMapper.getListByStatus", params);
	}

	/*
	 * 更新状态
	 * @see com.xxl.dao.IEmailSendInfoDao#updateStatus(int, int)
	 */
	@Override
	public int updateStatus(int id, int emailStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("emailStatus", emailStatus);
		return sqlSessionTemplate.update("EmailSendInfoMapper.updateStatus", params);
	}

	/*
	 * 查询邮件
	 * @see com.xxl.dao.IEmailSendInfoDao#getEmail(int, int)
	 */
	@Override
	public EmailSendInfo getEmail(int userId, int emailType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("emailType", emailType);
		return sqlSessionTemplate.selectOne("EmailSendInfoMapper.getEmail", params);
	}

}
