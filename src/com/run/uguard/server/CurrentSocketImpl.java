package com.run.uguard.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import com.run.park.entity.DataType;
import com.run.park.entity.DataType_Property;
import com.run.park.entity.User;
import com.run.uguard.entity.RouteInfo;
import com.run.uguard.entity.SocketData;
import com.run.uguard.facade.CurrentSocketDao;
import com.run.uguard.facade.SocketManageDao;
import com.run.uguard.tools.CRC;


public class CurrentSocketImpl implements CurrentSocketDao ,Runnable{

	//连接对象
	private Socket socket = null;
	private ObjectInputStream objectInputStream = null;
	private ObjectOutputStream dataOutputStream = null;
	private User user = null;
	private Thread thread = null;
	private boolean isWork = true;
	
	private BlockingQueue<SocketData> socketDatas  = SocketManageImpl.socketDatas;
	private SocketManageDao manageImpl = SocketManageImpl.getSocketManage();
	public CurrentSocketImpl(Socket socket){
		this.socket = socket; 
	};
	@Override
	public boolean init(){
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			dataOutputStream = new ObjectOutputStream(socket.getOutputStream());
			User user = (User) objectInputStream.readObject();	
			System.out.println("IP："+socket.getInetAddress()+"连接者："+user.getName()+";产品号："+user.getProid());
			if(!IsvValidUser(user)) {
				dataOutputStream.writeUnshared("00");
				dataOutputStream.flush();
				return false;
			}
			else{
				dataOutputStream.writeUnshared("FF");
				dataOutputStream.flush();
				this.user = user;
			}
		} catch (Exception e ) {
			// TODO Auto-generated catch block
			return false;
		}
		thread = new Thread(this);
		thread.start();
		return true;
		
	}
//	private boolean IsvValidUser(User user) {
//		// TODO Auto-generated method stub
//		if(manageImpl.getLink(user.getProid())!=null) return false;
//		 return true;
//	}
	private boolean IsvValidUser(User user) {
		// TODO Auto-generated method stub
		if(manageImpl.getLink(user.getProid())!=null) {
			//    
			
			if(manageImpl.getLink(user.getProid()).test()){
				return false;
			}else{
				manageImpl.getLink(user.getProid()).close();
				return true;
			}
		
			
		}else 
			return true;
	}

	@Override
	public User getUser() {
		// TODO Auto-generated method stub
		return user;
	}


	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		try {
			socket.shutdownInput();
			socket.shutdownOutput();
		    socket.close();
		    isWork = false;
		    if(thread != null)
		    	thread.interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}

	@Override
	public boolean sendAll(String message) {
		// TODO Auto-generated method stub
//		int index = 0 ;
//	while(index < routeMap.size()){
//		index++;
//	}
		try {
			//标记本信息为指令信息
			dataOutputStream.writeUnshared("88");
			dataOutputStream.flush();
			dataOutputStream.writeUnshared(message);
			dataOutputStream.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean sendKye(String key, String message) {
		// TODO Auto-generated method stub
		try {
			//标记本信息为指令信息
			dataOutputStream.writeUnshared("88");
			dataOutputStream.flush();
			dataOutputStream.writeUnshared(key+"@"+message);
			dataOutputStream.flush();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Map<Integer, RouteInfo> getRouteMap() {
		// TODO Auto-generated method stub
		
		return null;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isWork){
			try {
				SocketData temp  = new SocketData(user.getProid());
				DataType dataType = (DataType) objectInputStream.readObject();	
				temp.setDataType(dataType);
			   socketDatas.put(temp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				manageImpl.closeClient(user.getProid());
				e.printStackTrace();
			}
		}
	}
	@Override
	public Map<String, RouteInfo> refreshRouteMap() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean test() {
		// TODO Auto-generated method stub
	try {
		if (!socket.isConnected()) {
			close();
			return false;
		}
		dataOutputStream.writeUnshared("55");
		dataOutputStream.flush();
		return true;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	}
	}
	@Override
	public void block() {
		// TODO Auto-generated method stub
		
	}
}
