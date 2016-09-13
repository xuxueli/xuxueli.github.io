package com.xxl.service;

/**
 * 资源实例
 * @author xuxueli
 */
public class ResourceBundle {

	private static final ResourceBundle resource = new ResourceBundle();
	public static ResourceBundle getInstance() {
		return resource;
	}
	
	private IWallService wallService;
	public IWallService getWallService() {
		return wallService;
	}
	public void setWallService(IWallService wallService) {
		this.wallService = wallService;
	}
}
