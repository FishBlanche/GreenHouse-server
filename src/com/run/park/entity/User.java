package com.run.park.entity;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = -5240152692961888097L;
	private  String  name = null;
	private  String  pw = null;
	private  String  proid = null;
	private  String  mean = null;
	public User(){
		super();
	}
	public User (String name , String pw ){
		this.name = name;
		this.pw = pw;
		proid = "MESABC";
		mean = "1001";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getProid() {
		return proid;
	}
	public void setProid(String proid) {
		this.proid = proid;
	}
	public String getMean() {
		return mean;
	}
	public void setMean(String mean) {
		this.mean = mean;
	}
	
}
