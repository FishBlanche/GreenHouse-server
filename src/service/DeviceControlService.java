package service;


import java.util.Iterator;

import com.run.uguard.facade.CurrentSocketDao;
import com.run.uguard.server.SocketManageImpl;


import entity.MapData;

public class DeviceControlService {
		
	    
	public String  operation(int deviceType,int deviceId,int duration,int status,String userIdentity) {
		String result="";
		 
		System.out.println(userIdentity);
	//	System.out.println("deviceType deviceId duration status"+deviceType+","+deviceId+","+duration+","+status);
		if(userIdentity.equals(""))
		{
			return result;
		}
		else 
		{
			if(MapData.identityMap.get("identity")!=null&&MapData.identityMap.get("identity").equals(userIdentity))
			{
				SendControl(convert(String.valueOf(deviceId),String.valueOf(deviceType),String.valueOf(duration),String.valueOf(status)));
				result="ok";
			}
			else
			{
				result="fail";
				return result;
			}
		}
		
        return result;
	}
	public  String convert(String ID,String Type,String Time,String Status){//十进制转十六进制
		String str="";
		
		if(Integer.toHexString(Integer.parseInt(ID)).toUpperCase().length()<2)
		{
			str+="0"+Integer.toHexString(Integer.parseInt(ID)).toUpperCase();
		}
		else
		{
			str+=Integer.toHexString(Integer.parseInt(ID)).toUpperCase();
		}
		if(Integer.toHexString(Integer.parseInt(Type)).toUpperCase().length()<2)
		{
			str+="0"+Integer.toHexString(Integer.parseInt(Type)).toUpperCase();
		}
		else
		{
			str+=Integer.toHexString(Integer.parseInt(Type)).toUpperCase();
		}
		if(Integer.toHexString(Integer.parseInt(Time)).toUpperCase().length()==1)
		{
			str+="000"+Integer.toHexString(Integer.parseInt(Time)).toUpperCase();
		}
		else if(Integer.toHexString(Integer.parseInt(Time)).toUpperCase().length()==2)
		{
			str+="00"+Integer.toHexString(Integer.parseInt(Time)).toUpperCase();
		}
		else if(Integer.toHexString(Integer.parseInt(Time)).toUpperCase().length()==3)
		{
			str+="0"+Integer.toHexString(Integer.parseInt(Time)).toUpperCase();
		}
		else
		{
			str+=Integer.toHexString(Integer.parseInt(Time)).toUpperCase();
		}
		if(Integer.toHexString(Integer.parseInt(Status)).toUpperCase().length()<2)
		{
			str+="0"+Integer.toHexString(Integer.parseInt(Status)).toUpperCase();
		}
		else
		{
			str+=Integer.toHexString(Integer.parseInt(Status)).toUpperCase();
		}
		str+="01";
		StringBuffer tempStr = new StringBuffer(str);
		int temp = tempStr.indexOf("7D");
			while(temp != -1){
				if(temp %2 == 0){
					tempStr.replace(temp, temp+2, "7D5D");
				}
				temp = str.indexOf("7D");
		}
			
		temp = tempStr.indexOf("7E");
		while(temp != -1){
			
			if(temp %2 == 0){
				tempStr.replace(temp, temp+2, "7D5E");
			}
			temp = str.indexOf("7E");
		}
		return tempStr.toString();
	}
	public  void SendControl(String message){
		System.out.println("手动控制"+":"+message);
		Iterator<CurrentSocketDao> temps = SocketManageImpl.getSocketManage().getLinkList().iterator();
		while(temps.hasNext()){
			CurrentSocketDao temp = temps.next();
		//	System.out.println("当前："+temp.getUser().getProid());
			SocketManageImpl.getSocketManage().sendMessage(temp.getUser().getProid(),message);
		}
	}
}
