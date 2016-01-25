package com.xxl.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.core.constant.CommonDic;
import com.xxl.core.constant.CommonDic.ReturnCodeEnum;
import com.xxl.core.model.main.WallInfo;
import com.xxl.core.result.ReturnT;
import com.xxl.core.util.URLEncoderUtil;
import com.xxl.dao.IWallInfoDao;
import com.xxl.service.IWallService;

/**
 * 一面墙
 * @author xuxueli
 */
@Service("wallService")
public class WallServiceImpl implements IWallService {
	
	@Autowired
	private IWallInfoDao wallInfoDao;
	
	@Override
	public Map<String, Object> wallQuery(int page, int rows, String content) {
		content = URLEncoderUtil.decoderUTF8(content);
		
		int offset = page<1 ? 0 : (page-1)*rows;
		int pagesize = rows;
		
		List<WallInfo> rowsData = wallInfoDao.query(offset, pagesize, content);
		int totalNumber = wallInfoDao.queryCount(offset, pagesize, content);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rows", rowsData);  
		resultMap.put("total", totalNumber);
		return resultMap;
	}

	@Override
	public ReturnT<Integer> wallAdd(WallInfo wallInfo) {
		if (wallInfo == null || StringUtils.isBlank(wallInfo.getContent())) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,参数异常");
		}
		
		wallInfo.setSource(CommonDic.WallDic.WALL_SOURCE_BACK);
		return wallInfoDao.add(wallInfo);
	}

	@Override
	public ReturnT<Integer> wallDel(int[] ids) {
		return wallInfoDao.del(ids);
	}

	@Override
	public ReturnT<Integer> wallUpdate(WallInfo wallInfo) {
		if (wallInfo == null || StringUtils.isBlank(wallInfo.getContent())) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,参数异常");
		}
		
		return wallInfoDao.udpate(wallInfo);
	}

	/**
	 * 清空表数据,初始化自增序号
	 */
	@Override
	public void freshTable() {
		wallInfoDao.freshTable();
	}
	
}
