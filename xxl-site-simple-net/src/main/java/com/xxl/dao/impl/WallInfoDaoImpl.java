package com.xxl.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.WallInfo;
import com.xxl.dao.IWallInfoDao;

/**
 * 一面墙,信息
 * @author xuxueli
 */
@Repository
public class WallInfoDaoImpl implements IWallInfoDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	/*
	 * 分页列表
	 * @see com.xxl.dao.IWallInfoDao#getPageList(int, int)
	 */
	@Override
	public List<WallInfo> getPageList(int offset, int pagesize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		return sqlSessionTemplate.selectList("WallInfoMapper.getPageList", params);
	}

}
