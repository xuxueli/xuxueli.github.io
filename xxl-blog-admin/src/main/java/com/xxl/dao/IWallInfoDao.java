package com.xxl.dao;

import java.util.List;

import com.xxl.core.model.main.WallInfo;
import com.xxl.core.result.ReturnT;

/**
 * 一面墙
 * @author xuxueli
 */
public interface IWallInfoDao {

	public List<WallInfo> query(int offset, int pagesize, String content);
	
	public int queryCount(int offset, int pagesize, String content);

	public ReturnT<Integer> add(WallInfo wallInfo);

	public ReturnT<Integer> del(int[] ids);

	public ReturnT<Integer> udpate(WallInfo wallInfo);
	
	/**
	 * 清空表数据,初始化自增序号
	 */
	public void freshTable();

}
