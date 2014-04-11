package com.run.park.entity;

import java.util.Date;

public class Command {

	public int id;
	public int type;
	public int lastTime;
	public int state;
	public long time;
	public int isForce ;
	public Command(String id , String type ,String lastTime , String state ,String isForce) throws Exception{
		this.id = Integer.parseInt(id, 16);
		this.type = Integer.parseInt(type,16);
		this.lastTime = Integer.parseInt(lastTime, 16);
		this.state = Integer.parseInt(state, 16);
		this.time = new Date().getTime();
		this.isForce = Integer.parseInt(isForce, 16);
	}
	
}
