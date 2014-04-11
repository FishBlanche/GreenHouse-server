package com.run.uguard.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import utility.DealData;
import utility.SensingFilter;
import com.run.uguard.entity.SocketData;
import com.run.uguard.facade.NotifyDao;

public class acceptDataListener extends Thread implements NotifyDao{
	
	 
private DealData dealData=new DealData();
private SensingFilter sf=new SensingFilter();
    // 声明一个容量为10000的缓存队列
	public static BlockingQueue<SocketData> queue = new ArrayBlockingQueue<SocketData>(10000);
    
	//创建两条线程
    private Thread thread1 = new Thread(this);
    private Thread thread2 = new Thread(this);
    
     

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
	//	System.out.println("-----------eventChanged--------------");
		try {
			 //   System.out.println("-----------eventChanged--------------");
				SocketData new_data = sf.correct(data);
				//向BlockingQueue添加数据
				if(new_data!=null){
					queue.put(new_data);
				}				

			//如果等待数据超过一定长度，则启动另一条线程
			if(queue.size()>=1000){
				if(thread2==null){
					thread2 = new acceptDataListener();
				}
				thread2.start();
			}
			//判断对象有没有被实例化
			if(thread1==null){
				thread1 = new acceptDataListener();
			}
			//判断线程是否已经中断,确保至少有一条线程在工作
			if(this.thread1.isAlive()==false){
				thread1.start();
			}
			if(queue.size()<=100 && thread2.isAlive()){
				thread2.interrupt();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 重写run方法
	 */
	@Override
	public void run() {
		//获取service
	//	System.out.println("-----------"+Thread.currentThread().getName()+"--------------");
		 
		while(true){ 
			try {
				SocketData data = queue.take();
				if(data!=null){
					dealData.DealWithData(data);
			 }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
