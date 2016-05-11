package com.run.uguard.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.run.park.entity.Command;
import com.run.uguard.entity.RouteInfo;
import com.run.uguard.entity.SocketData;
import com.run.uguard.facade.CurrentSocketDao;
import com.run.uguard.facade.SocketManageDao;
import com.run.uguard.tools.CRC;

/**
 * 
 * @author zhouzefeng
 * @use    实现socketManage的业务逻辑
 */
public class SocketManageImpl implements SocketManageDao , Runnable{
	private static SocketManageImpl socketManageImpl = null;
	public  static  BlockingQueue<SocketData> socketDatas = null;
	private ServerSocket serverSocket = null;
	private List<CurrentSocketDao> currentSocketDaos = null;
	private List<Map<Integer, RouteInfo>> routeMaps = null;
	private List<Command>  commands = new ArrayList<>();
	private Thread thread = null;
	private boolean isWork = true;
	private SocketManageImpl(){

	}

	@Override
	public boolean init(int port , int block) {
		// TODO Auto-generated method stub
		try {
			serverSocket = new ServerSocket(port, block);
		} catch (IOException e) {
			// TODO Auto-generated catch block

			return false;
		}
		socketDatas = new  ArrayBlockingQueue<SocketData>(10000);
		currentSocketDaos = new ArrayList<>();
		routeMaps = new ArrayList<>();
		thread = new Thread(this);
		thread.start();
		return true;
	}

	/**
	 * 获取单例
	 */
	public static SocketManageDao getSocketManage(){
		if(socketManageImpl == null){
			socketManageImpl = new SocketManageImpl();
		}
		return socketManageImpl;
	}


	@Override
	public List<CurrentSocketDao> getLinkList() {
		// TODO Auto-generated method stub
		return currentSocketDaos;
	}

	@Override
	public CurrentSocketDao getLink(String userCodeCode) {
		// TODO Auto-generated method stub
		Iterator<CurrentSocketDao> temps = currentSocketDaos.iterator();
		while(temps.hasNext()){
			CurrentSocketDao temp = temps.next();
			
			if(temp.getUser().getProid().equals(userCodeCode)){
				return temp;
			}
		}
		return null;
	}

	@Override
	public boolean blockClient(String userCodeCode) {
		// TODO Auto-generated method stub
		CurrentSocketDao temp  = getLink( userCodeCode);
		temp.block();
		return true;
	}

	@Override
	public boolean closeClient(String userCodeCode) {
		// TODO Auto-generated method stub
		CurrentSocketDao temp  = getLink( userCodeCode);
		if(temp.close()){
			currentSocketDaos.remove(temp);
			routeMaps.remove(temp.getRouteMap());
			return true;
		}else{
			return false;
		}

	}

	@Override
	public void weakUpClient() {
		// TODO Auto-generated method stub
		socketManageImpl.notifyAll();
	}

	@Override
	public List<Map<Integer, RouteInfo>> getCurrentRouteInfo() {
		// TODO Auto-generated method stub
		return routeMaps;
	}

	@Override
	public Map<Integer, RouteInfo> getCurrentRouteInfo(String userCodeCode) {
		// TODO Auto-generated method stub
		CurrentSocketDao temp = getLink(userCodeCode);
		int index = routeMaps.indexOf(temp.getRouteMap());

		//		Iterator<Map<String, RouteInfo>>  temps = routeMaps.iterator();
		//		while(temps.hasNext()){
		//			Map<String, RouteInfo> temp = temps.next();
		//			Set<String> keyTemp = temp.keySet();
		//			 for (Iterator<String> it = keyTemp.iterator(); it.hasNext();) {
		//		         if(it.next().startsWith(userCodeCode)) return temp;
		//		        }
		//		}


		//		int index = 0;
		//		while(index < currentSocketDaos.size()){
		//			if(currentSocketDaos.get(index).getUser().getProid().equals(userCodeCode)){
		//				return routeMaps.get(index);
		//			}
		//			index++;
		//		}
		return routeMaps.get(index);
	}

	public Command getCommand (String id){
		int index = 0;
		while(index< commands.size()){
			if(commands.get(index).id == Integer.parseInt(id, 16)){
				return commands.get(index);
			}
			index++;
		}
		return null;

	}
	@Override
	public boolean sendMessage(String userCodeCode,String message) {
	
		// TODO Auto-generated method stub
		//System.out.println("sendMessage"+"::"+message);
		String id = message.substring(0, 2);
		String type = message.substring(2,4);
		String lastTime = message.substring(4,8);
		String state = message.substring(8,10);
		String isForce =  message.substring(10);
		Command cmd  =  getCommand(id);
		long tempTime = new Date().getTime();
		if(cmd == null){
			try {
				cmd = new Command(id, type, lastTime, state, isForce);
				commands.add(cmd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}else{
			
			try{
				if(Integer.parseInt(isForce, 16) != 1){
					if((tempTime - cmd.time)< cmd.lastTime*1000){
						return false;
				}
				}
				cmd.isForce = Integer.parseInt(isForce, 16);
				cmd.type = Integer.parseInt(type, 16);
				cmd.lastTime = Integer.parseInt(lastTime, 16);
				cmd.time = new Date().getTime();
				cmd.state = Integer.parseInt(state, 16);
			}catch (Exception e){
				return false;
			}
		}
		System.out.println("自动控制触发:"+message+"------"+new Date().getTime());
		System.out.println(cmd.lastTime);
		CurrentSocketDao temp = getLink(userCodeCode);
		if(temp == null) return false;
		String leng = Integer.toHexString(message.length()/2+8);
		
		if(leng.length() !=2) leng = "0"+leng;
		
		String tempCRC = CRC.getCrcTool().getCRCCode("440000FFFF000F"+leng+"3F89"+message);
		message = "7E440000FFFF000F"+leng+"3F89 "+message+" "+tempCRC.substring(2)+tempCRC.substring(0,2)+"7E";
		return temp.sendAll(message.toUpperCase());

	}
	@Override
	public boolean sendMessage(String userCodeCode, int sink,String message) {
		// TODO Auto-generated method stub
		CurrentSocketDao temp = getLink(userCodeCode);
		if(temp == null) return false;
		String leng = Integer.toHexString(message.length()/2+8);
		if(leng.length() !=2) leng = "0"+leng;
		message = "7E440000FFFF000F"+leng+"3F89"+message+CRC.getCrcTool().getCRCCode(message)+"7E";
		return temp.sendKye(temp.getRouteMap().get(sink).getIp(), message);

	}

	@Override
	public boolean sendMessage(String userCodeCode, List<String> keys,String message) {
		// TODO Auto-generated method stub
		CurrentSocketDao temp = getLink(userCodeCode);
		Iterator< String > tempKeys = keys.iterator();
		while(tempKeys.hasNext()){
			temp.sendKye(tempKeys.next(), message);
		}

		return false;
	}

	@Override
	public boolean sendMessage(List<String> userCodes, String key,String message) {
		// TODO Auto-generated method stub
		Iterator<String> temps = userCodes.iterator();
		while(temps.hasNext()){
			CurrentSocketDao temp = getLink(temps.next());
			temp.sendKye(key, message);
		}	
		return false;
	}

	@Override
	public boolean sendMessage(List<String> userCodes,String message) {
		// TODO Auto-generated method stub
		Iterator<String> temps = userCodes.iterator();
		while(temps.hasNext()){
			CurrentSocketDao temp = getLink(temps.next());
			temp.sendAll(message);
		}	
		return false;
	}

	@Override
	public BlockingQueue<SocketData> getSocketDataQueue() {
		// TODO Auto-generated method stub
		return socketDatas;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isWork){
			try {
				Socket temp =	serverSocket.accept();
				CurrentSocketDao currentTemp = new CurrentSocketImpl(temp);
				if(currentTemp.init()) {	
					currentSocketDaos.add(currentTemp);
					routeMaps.add(currentTemp.getRouteMap());
				}else{
					currentTemp.close();
				}
				System.out.println("当前连接数："+currentSocketDaos.size());
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
		}
	}



}
