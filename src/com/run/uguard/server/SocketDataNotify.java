package com.run.uguard.server;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.run.park.entity.DataType;
import com.run.park.entity.DataType_Property;
import com.run.uguard.entity.SocketData;
import com.run.uguard.facade.NotifyDao;
import com.run.uguard.facade.SocketDataNotifyDao;

public class SocketDataNotify implements Runnable,SocketDataNotifyDao{

	private static SocketDataNotify dataInotify = null;
	private BlockingQueue<SocketData> socketDatas  = SocketManageImpl.socketDatas;
	private boolean isWork = true; 
	private Thread thread = null;
	private List<NotifyDao> notifyDaos = null;
	private SocketDataNotify(){
		thread = new Thread(this);
		notifyDaos = new ArrayList<>();

	}
	protected boolean init(){
		notifyDaos.add(new acceptDataListener());
		thread.start();

		return true;

	}
	public static SocketDataNotify getSocketDataInotify(){
		if(dataInotify == null){
			dataInotify = new SocketDataNotify();
		}
		return dataInotify;
	}
	public  void notifys(SocketData data){		

		Iterator<NotifyDao>  temps =  notifyDaos.iterator();
		if(temps.hasNext()){

			temps.next().eventChanged(data);
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isWork){
			try {
				notifys(socketDatas.take());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public  boolean addListener(NotifyDao notifyDao) {
		// TODO Auto-generated method stub
		if(isListenerExist(notifyDao)) 
			return false;
		else{
			notifyDaos.add(notifyDao);
			return true;
		}
	}
	@Override
	public  boolean removeListener(NotifyDao notifyDao) {
		// TODO Auto-generated method stub
		if(!isListenerExist(notifyDao)) 
			return false;
		else{
			notifyDaos.add(notifyDao);
			return true;
		}
	}
	private boolean isListenerExist(NotifyDao notifyDao ){
		if(notifyDaos.indexOf(notifyDao)>0)  return true;
		else return false;

	}
}
