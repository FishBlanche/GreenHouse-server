package com.run.uguard.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import utility.DealData;
import utility.SensingFilter;

import com.run.uguard.entity.SocketData;
import com.run.uguard.facade.NotifyDao;

public class acceptDataListener implements NotifyDao{


	private DealData dealData=new DealData();
	private SensingFilter sf=new SensingFilter();

	//   private Thread thread2 = new Thread(this);
	/**
	 * 添加监听
	 * 类的构造方法
	 */
	public acceptDataListener(){

	}
	/**
	 * 接收数据
	 */
	@Override
	public void eventChanged(SocketData data) {	
		SocketData new_data = sf.correct(data);
		//向BlockingQueue添加数据
		if(new_data!=null){
			dealData.DealWithData(new_data);
		} 
	}


}
