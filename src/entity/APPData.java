package entity;

import java.io.Serializable;

public class APPData implements Serializable{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 5942572320825465294L;
	private String value;//数据库字段名
	private String timestamparrive_tm;//时间

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTimestamparrive_tm() {
		return timestamparrive_tm;
	}
	public void setTimestamparrive_tm(String timestamparrive_tm) {
		this.timestamparrive_tm = timestamparrive_tm;
	}

}
