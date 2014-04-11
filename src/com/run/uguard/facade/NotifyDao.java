package com.run.uguard.facade;
/**
 * 兼听则明要实现的方法
 */
import com.run.uguard.entity.SocketData;

public interface NotifyDao {
	public void eventChanged(SocketData data);
}
