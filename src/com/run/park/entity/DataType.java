package com.run.park.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class DataType implements Serializable ,Cloneable{
	private static final long serialVersionUID = -5240152692961888096L;
	//�������
	private String entryName = null;
	//�����Ϣ
	private String entryInfo = null;
	//
	private String attachedInfo = null;
	private String  dataType      = null;   
	private String  mapTable      = null;    
	private String  dataName      = null;   
	private String  useRuleName   = null;   
	private int  dataLength       = -1;
	private List<DataType_Property> propertyList  = null ;
	
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
	//��ȡunknow����
	public List<DataType_Property> getUnknow(){
		List<DataType_Property> propertys = new ArrayList<DataType_Property>();
		Iterator<DataType_Property > iterators = propertyList.iterator();
		while(iterators.hasNext()){
			DataType_Property temp = iterators.next();
			if(temp.getType().equals("unknown"))  propertys.add(temp);
		}
		return propertys;
	}
//��ȡ����
//	public DataType_Property getDataType_Property(String colTable){
//		Iterator<DataType_Property > iterators = propertyList.iterator();
//		while(iterators.hasNext()){
//			DataType_Property temp = iterators.next();
//			if(temp.getColTable().equals(colTable)) return temp;
//		}
//		return null;
//	}
//�޸����
//	public void updatePropertyvalue(String colTable ,String value){
//		DataType_Property temp = getDataType_Property(colTable);
//		temp.setValue(value);
//	}
//���������
//	public void addPropertys(DataType_Property dataType_Property){
//		propertyList.add(dataType_Property);
//	}
//�Ƴ�����
//	public boolean removePropertys(String colTable){
//		DataType_Property tempProperty =getDataType_Property(colTable);
//		if(tempProperty == null){
//			return false;
//		}
//		propertyList.remove(tempProperty);
//		return true;
//	}
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
		String temp = "";
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

	private int mean(String type) {
		// TODO Auto-generated method stub
		
		if(type.equals("int"))
			return 1;
		else if(type.equals("long"))
			return 2;
		else if(type.equals("string"))
			return 3;
		else if(type.equals("time"))
			return 4;
		else
		return 0;
	}
	public String toData(){
		String temp ="";
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
//	public DataType stringToObject(String object){
//	DataType type =	new DataType();
//	String[] temp = object.split(" ");
//	type.setEntryInfo(temp[0]);
//	type.setAttachedInfo(temp[1]);
//	type.setDataType(temp[2]);
//	type.setMapTable(temp[3]);
//	type.setDataName(temp[4]);
//	type.setUseRuleName(temp[5]);
//	type.setDataLength(Integer.parseInt(temp[6]));
//	int index = 7;
//	List<DataType_Property> properties = new ArrayList<>();
//	while(index < object.length() ){
//		DataType_Property type_Property = new DataType_Property();
//		String porperty = temp[index];
//		String[] porpertyTemp = porperty.split("|");
//		type_Property.setMean(porpertyTemp[0]);
//		type_Property.setColTable(porpertyTemp[1]);
//		type_Property.setType(porpertyTemp[2]);
//		type_Property.setIndex(Integer.parseInt(porpertyTemp[3]));
//		type_Property.setLength(Integer.parseInt(porpertyTemp[4]));
//		type_Property.setValue(porpertyTemp[5]);
//		properties.add(type_Property);
//		index++;
//	}
//	type.setPropertyList(properties);
//		return type;
//		
//	}
	public String toOriginalData(){
		String originalData ="";
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
		String datainfo = "";
		String propertyinfo = "";
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
		List<DataType_Property> tempList = new ArrayList<DataType_Property>();
		int index = 0;
		while(index < this.getPropertyList().size() ){
			tempList.add(this.getPropertyList().get(index).clone());
			index++;
		}
		temp.setPropertyList(tempList);
		return temp;
		
	}
}
