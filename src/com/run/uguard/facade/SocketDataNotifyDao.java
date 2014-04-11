package com.run.uguard.facade;
/**
 * @author zhouzefeng
 * @use      事件发动者的接口
 *  
 */
public interface SocketDataNotifyDao {

	  public  boolean  addListener(NotifyDao   notifyDao);
	  public  boolean  removeListener(NotifyDao   notifyDao);
}
