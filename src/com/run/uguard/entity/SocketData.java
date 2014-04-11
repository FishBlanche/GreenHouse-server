package com.run.uguard.entity;

import com.run.park.entity.DataType;

/**
 * 
 * @author zhouzefeng
 * @use    描述接受到的数据
 */
public class SocketData implements Cloneable{

	//用户标识
	private String userCode;
	//数据对象
	private DataType dataType;
	
	
	public SocketData(String proid) {
		// TODO Auto-generated constructor stub
		this.userCode = proid;
	}


	public String getUserCode() {
		return userCode;
	}


	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}


	public DataType getDataType() {
		return dataType;
	}


	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}


	public SocketData clone() throws CloneNotSupportedException{
		SocketData temp = (SocketData) super.clone();
		DataType tmp = dataType.clone();
		temp.setDataType(tmp);
		return temp;
	}
	
}
