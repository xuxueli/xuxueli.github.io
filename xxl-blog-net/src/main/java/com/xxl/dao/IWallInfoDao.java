package com.xxl.dao;

import java.util.List;

import com.xxl.core.model.main.WallInfo;


/**
 * 一面墙,信息
 * @author xuxueli
 */
public interface IWallInfoDao {

	/**
	 * 分页列表
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public List<WallInfo> getPageList(int offset, int pagesize);

}
