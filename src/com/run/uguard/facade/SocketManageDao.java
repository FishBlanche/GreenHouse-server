package com.run.uguard.facade;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.run.uguard.entity.RouteInfo;
import com.run.uguard.entity.SocketData;

/**
 * 
 * @author zhouzefeng
 * @use    用于管理当前连接
 */
public interface SocketManageDao {

	/**
	 * 初始化
	 */
	public  boolean init(int port , int block); 
	/**
	 * 获取当前的总连接
	 */
	public List<CurrentSocketDao> getLinkList();
	/**
	 * 获取指定连接
	 */
	public CurrentSocketDao getLink(String userCodeCode);
	/**
	 * 阻塞当前连接
	 */
	public boolean blockClient (String userCodeCode);
	/**
	 * 中断当前连接
	 */
	public boolean closeClient(String userCodeCode);
	/**
	 * 唤醒阻塞的连接(当前已经阻塞的均被唤醒)
	 */
	public void weakUpClient();
	/**
	 * 获取路由表
	 */
	public List<Map<Integer, RouteInfo>> getCurrentRouteInfo();
	/**
	 * 获取指定路由表
	 */
	public Map<Integer, RouteInfo> getCurrentRouteInfo(String userCodeCode);
	/**
	 * 数据下行
	 */
	public boolean sendMessage(String userCodeCode,String message);	
	public boolean sendMessage(String userCodeCode , int key ,String message);
	public boolean sendMessage(String userCodeCode ,List<String> keys,String message);
	public boolean sendMessage(List<String> userCodes ,String key,String message);
	public boolean sendMessage(List<String> userCodes,String message);
	/**
	 * 获取当前队列
	 */
	public BlockingQueue<SocketData>  getSocketDataQueue();
}
