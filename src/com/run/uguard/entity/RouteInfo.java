package com.run.uguard.entity;

import java.util.Date;

/**
 * 
 * @author zhouzefeng
 * @use    描述路由表
 */
public class RouteInfo {

	//ip
	private String ip = null;
	//最后通信时间时间
	private Date lastTime = null;
	//数据量
	private int number = 1;
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
