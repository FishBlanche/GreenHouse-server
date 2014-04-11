package com.run.uguard.server;

import com.run.uguard.facade.SocketManageDao;


public class UGuardSystem {

	public static boolean init(){
		//初始化日志文件
		
		//初始化SocketServer
		if(initManage())
		{
			System.out.println("**-------------socket服务初始划成功-------------**");
			 
		}else{
			System.out.println("**-------------socket服务初始划失败，本系统不能正常工作，将放弃后续的操作-------------**");
			return false;
		}
		//
		SocketDataNotify.getSocketDataInotify().init();
		
		return true;
	}
	
	private static boolean initManage(){
		SocketManageDao socketManageDao  = SocketManageImpl.getSocketManage();
		if(socketManageDao.init(WebAppParameter.socketPort, WebAppParameter.serverBlock)){
			return true;
		}else return false;
	}
}
