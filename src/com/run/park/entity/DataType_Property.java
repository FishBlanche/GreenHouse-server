package com.run.park.entity;

import java.io.Serializable;

public class DataType_Property implements Serializable,Cloneable{
	private static final long serialVersionUID = -5240152692961888096L;
	private String mean      = null;
	private String colTable  = null; 
	private String type      = null;
	private int    index     = -1;
	private int    length    = -1;
	//该属性由解析时候赋值
	private String    value  = null;
	
	public DataType_Property(){
		super();
	}
	public DataType_Property(String mean ,String colTable , String type , int index , int length){
		this.mean = mean;
		this.colTable = colTable;
		this.type = type;
		this.index = index;
		this.length = length;
	}


	public String getMean() {
		return mean;
	}


	public String getColTable() {
		return colTable;
	}


	public String getType() {
		return type;
	}


	public int getIndex() {
		return index;
	}


	public int getLength() {
		return length;
	}


	public void setMean(String mean) {
		this.mean = mean;
	}


	public void setColTable(String colTable) {
		this.colTable = colTable;
	}


	public void setType(String type) {
		this.type = type;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public void setLength(int length) {
		this.length = length;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public DataType_Property clone() throws CloneNotSupportedException{
		return (DataType_Property) super.clone();
		
	}
	
}
