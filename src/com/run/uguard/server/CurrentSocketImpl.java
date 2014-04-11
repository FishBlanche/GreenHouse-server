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
	private Map<Integer, RouteInfo> routeMap = null;
	private boolean isBlock = false; 
	private boolean isWork = true;
	private SocketData temp ;
	//维护自身路由表
	private Timer timer = new Timer();;
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
				//sendAll("数据下行测试机123abc");
				
//				
//				String message = "01 01 00 0A 00 01";
//				String leng = Integer.toHexString(message.length()/2+8);
//				System.out.println("payload:"+message);
//				if(leng.length() !=2) leng = "0"+leng;
//				System.out.println("leng:"+leng);
//				String temp = CRC.getCrcTool().getCRCCode("440000FFFF000F"+leng+"3F89"+message);
//				System.out.println("crcString:"+"440000FFFF000F"+leng+"3F89"+message);
//				message = "7E 44 00 00 FF FF 00 0F "+leng+" 3F 89 "+message+" "+temp.substring(2)+" "+temp.substring(0,2)+" 7E";
//			//	sendAll("7E 44 00 00 FF FF 00 0F 0a 3F 89 FF FF 88 0B 7E");
//			//	sendAll("7E 44 00 00 FF FF 00 0F 0E 3F 89 01 01 00 0A 00 01 0A 26 7E");
//				sendAll(message.toUpperCase());
				
//				System.out.println("message:"+message);
				
				
				this.user = user;
			}
		} catch (Exception e ) {
			// TODO Auto-generated catch block
			return false;
		}
		routeMap = new HashMap<>(); 
		thread = new Thread(this);
		thread.start();
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshRouteMap();
				
			}
		}, 300000, 600000);
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
	public void block() {
		// TODO Auto-generated method stub
		isBlock = true;
	}

	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		try {
			socket.shutdownInput();
			socket.shutdownOutput();
		    socket.close();
		    timer.cancel();
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
		
		return routeMap;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		temp = new SocketData(user.getProid());
		while(isWork){
			try {
				DataType dataType = (DataType) objectInputStream.readObject();	
				temp.setDataType(dataType);
				//维护表  节点编号+真实入口
				int key =Integer.parseInt(dataType.getPropertyList().get(2).getValue(),16);	
			   RouteInfo info =	routeMap.get(key);
			   if(info == null){
				   info = new RouteInfo();
				   routeMap.put(key, info);
			   }else{
				   info.setNumber(info.getNumber()+1);
				   info.setLastTime(new Date());
				   info.setIp(dataType.getEntryName().trim());
			   }
			   
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
}
