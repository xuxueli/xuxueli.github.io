package com.xxl.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.WallInfo;
import com.xxl.core.result.ReturnT;
import com.xxl.dao.IWallInfoDao;

/**
 * 一面墙
 * @author xuxueli
 */
@Repository
public class WallInfoDaoImpl implements IWallInfoDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<WallInfo> query(int offset, int pagesize, String content) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("content", content);
		return sqlSessionTemplate.selectList("WallInfoMapper.query", params);
	}
	
	@Override
	public int queryCount(int offset, int pagesize, String content) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("content", content);
		return sqlSessionTemplate.selectOne("WallInfoMapper.queryCount", params);
	}

	@Override
	public ReturnT<Integer> add(WallInfo wallInfo) {
		int ret = sqlSessionTemplate.insert("WallInfoMapper.add", wallInfo);
		return new ReturnT<Integer>(ret);
	}

	@Override
	public ReturnT<Integer> del(int[] ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		int ret = sqlSessionTemplate.delete("WallInfoMapper.del", params);
		return new ReturnT<Integer>(ret);
	}

	@Override
	public ReturnT<Integer> udpate(WallInfo wallInfo) {
		int ret = sqlSessionTemplate.update("WallInfoMapper.udpate", wallInfo);
		return new ReturnT<Integer>(ret);
	}

	/*
	 * 清空表数据,初始化自增序号
	 * @see com.xxl.dao.IWallInfoDao#freshTable()
	 */
	@Override
	public void freshTable() {
		sqlSessionTemplate.update("WallInfoMapper.freshDate");
		sqlSessionTemplate.update("WallInfoMapper.freshIncrement");
	}

}
