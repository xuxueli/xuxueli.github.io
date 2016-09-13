package com.xxl.service;

import java.util.Map;

import com.xxl.core.model.main.WallInfo;
import com.xxl.core.result.ReturnT;

/**
 * 一面墙
 * @author xuxueli
 */
public interface IWallService {

	public Map<String, Object> wallQuery(int page, int rows, String content);

	public ReturnT<Integer> wallAdd(WallInfo wallInfo);

	public ReturnT<Integer> wallDel(int[] ids);

	public ReturnT<Integer> wallUpdate(WallInfo wallInfo);
	
	/**
	 * 清空表数据,初始化自增序号
	 */
	public void freshTable();

}
