package com.run.uguard.facade;

import java.util.Map;

import com.run.park.entity.User;
import com.run.uguard.entity.RouteInfo;

/**
 * 
 * @author zhouzefeng
 * @use    建立连接的对象
 */
public interface CurrentSocketDao {

	/**
	 * 
	 * @返回连接的用户信息
	 */
	public User getUser();
	/**
	 * @设置阻塞标识
	 */
	public void block();
	/**
	 * @关闭连接
	 */
	public boolean close();
	/**
	 * @下行
	 */
	public boolean sendAll(String message);
	public boolean sendKye(String key ,String message);
	/**
	 * @返回路由
	 */
	public Map<Integer, RouteInfo>  getRouteMap();
	/**
	 * @刷新路由表
	 */
	public Map<String,RouteInfo>  refreshRouteMap();
	/**
	 * 
	 * @初始化
	 */
	public boolean init();
	public boolean test();
}
