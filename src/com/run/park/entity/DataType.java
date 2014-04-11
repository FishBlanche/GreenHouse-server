package com.run.park.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataType implements Serializable ,Cloneable{
	private static final long serialVersionUID = -5240152692961888096L;
	//入口名字
	private String entryName = null;
	//入口信息
	private String entryInfo = null;
	//
	private String attachedInfo = null;
	private String  dataType      = null;   
	private String  mapTable      = null;    
	private String  dataName      = null;   
	private String  useRuleName   = null;   
	private int  dataLength       = -1;
	private List<DataType_Property> propertyList  = null;
	private String temp = "";
	private String colTemp = "";
	private String valTemp = "";
	private String value = "";
	private String  originalData= "";
	private String datainfo = "";
	private String propertyinfo = "";
	public DataType() {
		super();
	}
	public DataType(String dataType ,String mapTable , 
			String dataName ,String useRule, List<DataType_Property> Propertys){
		this.dataType = dataType;
		this.mapTable = mapTable;
		this.dataName = dataName;
		this.useRuleName = useRule;
		this.propertyList = Propertys;
	
	}


	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public String getDataType() {
		return dataType;
	}

	public String getMapTable() {
		return mapTable;
	}

	public String getDataName() {
		return dataName;
	}

	public String getUseRuleName() {
		return useRuleName;
	}

	
	public List<DataType_Property> getPropertyList() {
		return propertyList;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setMapTable(String mapTable) {
		this.mapTable = mapTable;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public void setUseRuleName(String useRuleName) {
		this.useRuleName = useRuleName;
	}

	public void setPropertyList(List<DataType_Property> propertyList) {
		this.propertyList = propertyList;
	}
	
	public String toInfo(){
		DataType_Property property = null;
		int index = 0;
		while(index < this.propertyList.size()){
			property = propertyList.get(index);
			temp += property.getMean() +" : "+property.getValue()+"\n";
			index++;
		}
		property = null;
		return temp;
		
	}
	public List<DataType_Property> getUnknow(){
		List<DataType_Property> propertys = new ArrayList<>();
		Iterator<DataType_Property > iterators = propertyList.iterator();
		while(iterators.hasNext()){
			DataType_Property temp = iterators.next();
			if(temp.getType().equals("unknown"))  propertys.add(temp);
		}
		return propertys;
	}

	
	
	public String toData(){
		DataType_Property property = null;
		int index = 0;
		while(index < this.propertyList.size()){
			property = propertyList.get(index);
			temp += property.getColTable() +" : "+property.getValue()+"\n";
			index++;
		}
		property = null;
		return temp;
		
	}

	public String toOriginalData(){
		originalData += this.entryName;
		originalData +=" "+this.entryInfo;
		originalData +=" "+this.attachedInfo;
		originalData +=" "+ this.dataType +":";
		int index = 0;
		DataType_Property property = null;
		while(index < this.propertyList.size()){
			property = propertyList.get(index);
			originalData += property.getValue();
			index++;
		}
		return originalData;
	}
	public String toString(){
		datainfo = "";
		propertyinfo = "";
		datainfo += this.entryInfo;
		datainfo += " "+this.attachedInfo;
		datainfo += " "+this.dataType;
		datainfo += " "+this.mapTable;
		datainfo += " "+this.dataName;
		datainfo += " "+this.useRuleName;
		datainfo += " "+this.dataLength;
	
		DataType_Property property = null;
		int index = 0;
		while(index < this.propertyList.size()){
			property = propertyList.get(index);
			propertyinfo += " "+property.getMean();
			propertyinfo += ":"+property.getColTable();
			propertyinfo += ":"+property.getType();
			propertyinfo += ":"+property.getIndex();
			propertyinfo += ":"+property.getLength();
			propertyinfo += ":"+ property.getValue();	
			index++;
		}
		return datainfo+propertyinfo;
		
	}
	public String getEntryInfo() {
		return entryInfo;
	}
	public void setEntryInfo(String entryInfo) {
		this.entryInfo = entryInfo;
	}
	public String getAttachedInfo() {
		return attachedInfo;
	}
	public void setAttachedInfo(String attachedInfo) {
		this.attachedInfo = attachedInfo;
	}
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
	public DataType clone() throws CloneNotSupportedException{
		DataType temp = (DataType) super.clone();
		List<DataType_Property> tempList = new ArrayList<>();
		int index = 0;
		while(index < this.getPropertyList().size() ){
			tempList.add(this.getPropertyList().get(index).clone());
			index++;
		}
		temp.setPropertyList(tempList);
		return temp;
		
	}
}
