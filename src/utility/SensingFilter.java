package utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import service.DeviceControlService;
import utility.DealData.MyTask;

import dao.MySqlConnectionHelper;
import entity.ControlGroup;
import entity.MapData;

import entity.SensingErrorLog;

import com.run.park.entity.DataType;
import com.run.park.entity.DataType_Property;
import com.run.uguard.entity.SocketData;
import com.run.uguard.facade.CurrentSocketDao;
import com.run.uguard.server.SocketManageImpl;

 

public class SensingFilter {
	private   Connection connection = null;
	private   Statement stmt = null;
	private DeviceControlService dcs=new DeviceControlService();
	 
	public   Connection getConnection() throws SQLException {
		return MySqlConnectionHelper.getConnection();
	}
	public   void  saveErrorData(SensingErrorLog sel)
	{
		//System.out.println("-----------saveErrorData--------------");
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			String sql = "INSERT INTO  ErrorLog VALUES('"+sel.getId()+"',"+sel.getNode_id()+",'"+sel.getInfo()+"','"+sel.getCreateDate()+"')";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			MySqlConnectionHelper.closeStatement(stmt);
			MySqlConnectionHelper.close(connection);
		}
	}
	public   SocketData correct(SocketData data){
		//System.out.println("sensingdata");
		DecimalFormat df=new DecimalFormat(".##");
		String currentArea="";
		String newestDataKey="";

		DataType dataType = data.getDataType();

		List<DataType_Property> dplist = dataType.getPropertyList();
		int node_id = 0;
		int nodeType = 0;
		//int countHumi=0;
		for(int i=0;i<dplist.size();i++){
			//获取nodeType
			if(dplist.get(i).getColTable().equals("nodetype")){
				nodeType = Integer.parseInt(dplist.get(i).getValue(),16);	
				//nodeType出错
				if(nodeType>10 || nodeType<0){
					return null;
				}
			}
			//获取id
			if(dplist.get(i).getColTable().equals("Moteid_ID")){
				node_id = Integer.parseInt(dplist.get(i).getValue(),16);	

			}

		}

		if(! MapData.moteAreaMap.containsKey(String.valueOf(node_id)) || MapData.moteTypeMap.get(String.valueOf(node_id))==null || nodeType!=MapData.moteTypeMap.get(String.valueOf(node_id)))
		{
			return null;
		}
		
		currentArea=MapData.moteAreaMap.get(String.valueOf(node_id));//获得当前新数据节点所在的区域

		int index = 0;
		while(index < dplist.size()){
			double value = 0;
			double tem=0;
			double t=0;
			DataType_Property property = dplist.get(index);
		
			switch (property.getColTable()) {
			case "temperature":
				value =Integer.parseInt(property.getValue(), 16) * 0.01 - 40;
				if(nodeType==0)
				{
					if(value<-30 || value>100){
						newestDataKey="1"+","+currentArea;//某个区域内的温度
						value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
					} 
					newestDataKey="1"+","+currentArea;//某个区域内的温度
					MapData.newestDataMap.put(newestDataKey,value);
				}
				property.setValue( df.format(value));
				property.setType("float");
				break;
			case "humidity":
				//	//System.out.println("humidity before"+node_id+","+property.getValue());
				tem = Integer.parseInt(property.getValue(), 16);
				value = (tem*0.0405 - tem*tem*0.0000028 -4);
				if(nodeType==0)
				{
					if(value<0 || value>100){
						newestDataKey="2"+","+currentArea;
						value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
					}
					newestDataKey="2"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				}

				property.setValue(df.format(value));
				property.setType("float");
				break;
			case "photo_active":
				value = Integer.parseInt(property.getValue(), 16) * 2.888;
				if(nodeType==10)
				{
					if(value<0 || value>10000){
						newestDataKey="5"+","+currentArea;
						value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
					}
					newestDataKey="5"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				}
				property.setValue(df.format(value));
				property.setType("float");
				break;
			case "co2":

				if(nodeType==1){//一氧化碳
					value = (Integer.parseInt(property.getValue(), 16)*(2048.0/32767))*0.199204-224.1;
					if(value<0 || value>200){
						newestDataKey="7"+","+currentArea;
						value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
					}
					newestDataKey="7"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				}else if(nodeType==2){//二氧化碳
					value = Integer.parseInt(property.getValue(),16);
					if(value<=0 || value>5000){
						newestDataKey="8"+","+currentArea;
						value = MapData.newestDataMap.get(newestDataKey)==null?0: MapData.newestDataMap.get(newestDataKey);
					}
					newestDataKey="8"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				} 
				else if(nodeType==8){//PH
					value =Integer.parseInt(property.getValue(),16)/100.0;
					if(value<0 || value>14){
						newestDataKey="6"+","+currentArea;
						value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
					}
					newestDataKey="6"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				}
				else{
					value = Integer.parseInt(property.getValue(),16);
				}
				property.setValue(df.format(value));
				property.setType("float");
				break;
			case "A0":
				if(nodeType==9){//土壤温度
					t= Integer.parseInt(property.getValue(), 16)*25/4096.0 ;
					value=11.68*(5*(t-4)/32)*(5*(t-4)/32)*(5*(t-4)/32)-31.27*(5*(t-4)/32)*(5*(t-4)/32)+43.22*(5*(t-4)/32)-2.64;
			
					if(value<-30 || value>70){
						newestDataKey="3"+","+currentArea;
						value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
					}
					property.setValue(df.format(value));
					property.setType("float");		
					newestDataKey="3"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				}
				else
				{
					property.setValue(String.valueOf(Integer.parseInt(property.getValue(),16)));
					property.setType("int");

				}
				break;
			case "A1":
				if(nodeType==9){//土壤湿度
					value = 6.345*(Integer.parseInt(property.getValue(), 16)*25/4096.0)-57.698;
					
					if(value<0 || value>100){
						newestDataKey="4"+","+currentArea;
						value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
					}
				
					newestDataKey="4"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				}
				else
				{
					value = Integer.parseInt(property.getValue(),16);
				}
				property.setValue(df.format(value));
				property.setType("float");
				break;
			default:
				property.setValue(String.valueOf(Integer.parseInt(property.getValue(),16)));
				property.setType("int");
				break;
			}
			index++;
		}
		controlSetting();
		return data;
	}
	public  void controlSetting(){
		String skey="";
		String svalue="";
		String[] myValue=null;
		Set keys=MapData.settingMap.keySet();
		if(keys!= null) {  
			Iterator iterator = keys.iterator( );  
			while(iterator.hasNext()) { 
				skey = iterator.next( ).toString();  
				svalue = MapData.settingMap.get(skey);
				myValue= svalue.split(";");
				controlJudge(myValue[0],myValue[1]);
			}  
		}
	}
	
	public  void controlJudge(String myCondition,String myControl){
		////System.out.println("myCondition"+myCondition+"myControl"+myControl);
		String[] myConditions=myCondition.split(":");
		String[] myControls=myControl.split("a");
		String[] tempCondition=null;
		String[] tempControl=null;
		ScriptEngineManager manager = new ScriptEngineManager();
	      ScriptEngine engine = manager.getEngineByName("js");
	//	String myKey="";
	//	String myLogic="r";
	//	boolean flag=false;
		for(int i=0;i<myControls.length;i++)
		{
			String LogicStr="";
			tempControl=myControls[i].split(",");
			if(tempControl[0].equals("1"))//设备类型为灌溉(花洒),要分区域
			{
                for(int j=0;j<myConditions.length;j++)
				{
					tempCondition=myConditions[j].split(",");
					if(tempCondition[1].equals("1"))//大于
					{

						if(MapData.newestDataMap.get(tempCondition[0]+","+tempControl[1])!=null)
						{
							LogicStr+=MapData.newestDataMap.get(tempCondition[0]+","+tempControl[1])+">"+Double.parseDouble(tempCondition[2]);
							 
						}
						else
						{
							LogicStr+="0>1";
						}
					}
					else//小于
					{
                    	if(MapData.newestDataMap.get(tempCondition[0]+","+tempControl[1])!=null)
						{
							LogicStr+=MapData.newestDataMap.get(tempCondition[0]+","+tempControl[1])+"<"+Double.parseDouble(tempCondition[2]);
						}
						else
						{
							LogicStr+="0>1";
						}
					}
					if(tempCondition.length==4)//后面还有判断条件
					{
						if(tempCondition[3].equals("a"))
						{
							LogicStr+="&&";
						}
						else
						{
							LogicStr+="||";
						}
					}
					else//判断条件结束
						break;
				}
			}
			else
			{
				for(int j=0;j<myConditions.length;j++)
				{
					tempCondition=myConditions[j].split(",");
					if(tempCondition[1].equals("1"))//大于
					{
						if(MapData.newestDataMap.get(tempCondition[0]+","+"1")!=null||MapData.newestDataMap.get(tempCondition[0]+","+"2")!=null||MapData.newestDataMap.get(tempCondition[0]+","+"3")!=null||MapData.newestDataMap.get(tempCondition[0]+","+"4")!=null)
						{
							LogicStr+="(1>2";
							if(MapData.newestDataMap.get(tempCondition[0]+","+"1")!=null)
							{
								LogicStr+="||"+MapData.newestDataMap.get(tempCondition[0]+","+"1")+">"+Double.parseDouble(tempCondition[2]);
							}
							if(MapData.newestDataMap.get(tempCondition[0]+","+"2")!=null)
							{
								LogicStr+="||"+MapData.newestDataMap.get(tempCondition[0]+","+"2")+">"+Double.parseDouble(tempCondition[2]);
							}
							if(MapData.newestDataMap.get(tempCondition[0]+","+"3")!=null)
							{
								LogicStr+="||"+MapData.newestDataMap.get(tempCondition[0]+","+"3")+">"+Double.parseDouble(tempCondition[2]);
							}
							if(MapData.newestDataMap.get(tempCondition[0]+","+"4")!=null)
							{
								LogicStr+="||"+MapData.newestDataMap.get(tempCondition[0]+","+"4")+">"+Double.parseDouble(tempCondition[2]);
							}
							LogicStr+=")";
						}
						else
						{
							 return;
						}
					}
					else//小于
					{
						if(MapData.newestDataMap.get(tempCondition[0]+","+"1")!=null||MapData.newestDataMap.get(tempCondition[0]+","+"2")!=null||MapData.newestDataMap.get(tempCondition[0]+","+"3")!=null||MapData.newestDataMap.get(tempCondition[0]+","+"4")!=null)
						{
							LogicStr+="(1>2";
							if(MapData.newestDataMap.get(tempCondition[0]+","+"1")!=null)
							{
								LogicStr+="||"+MapData.newestDataMap.get(tempCondition[0]+","+"1")+"<"+Double.parseDouble(tempCondition[2]);
							}
							if(MapData.newestDataMap.get(tempCondition[0]+","+"2")!=null)
							{
								LogicStr+="||"+MapData.newestDataMap.get(tempCondition[0]+","+"2")+"<"+Double.parseDouble(tempCondition[2]);
							}
							if(MapData.newestDataMap.get(tempCondition[0]+","+"3")!=null)
							{
								LogicStr+="||"+MapData.newestDataMap.get(tempCondition[0]+","+"3")+"<"+Double.parseDouble(tempCondition[2]);
							}
							if(MapData.newestDataMap.get(tempCondition[0]+","+"4")!=null)
							{
								LogicStr+="||"+MapData.newestDataMap.get(tempCondition[0]+","+"4")+"<"+Double.parseDouble(tempCondition[2]);
							}
							LogicStr+=")";
						}
						else
						{
							 return;
						}
					}
					if(tempCondition.length==4)//后面还有判断条件
					{
						if(tempCondition[3].equals("a"))
						{
							LogicStr+="&&";
						}
						else
						{
							LogicStr+="||";
						}
					}
					else//判断条件结束
						break;
				}
			}
			
		   Object result = null;
		//   System.out.println("LogicStr:" +  LogicStr);
			try {
				result = engine.eval(LogicStr);
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if((boolean)result)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 if(tempControl[0].equals("2"))//风扇,默认两个风扇都开
					{
						SendControl(convert("5",tempControl[0],tempControl[3],tempControl[2]));
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						SendControl(convert("6",tempControl[0],tempControl[3],tempControl[2]));
					}
					else
					{
						
						SendControl(convert(tempControl[1],tempControl[0],tempControl[3],tempControl[2]));
					}
			}
	        //System.out.println("结果类型:" + result.getClass().getName() + ",计算结果:" + result);
		}
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
		str+="00";
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
		 System.out.println("自动控制触发:"+message);
		Iterator<CurrentSocketDao> temps = SocketManageImpl.getSocketManage().getLinkList().iterator();
		while(temps.hasNext()){
			CurrentSocketDao temp = temps.next();
			//	//System.out.println("当前："+temp.getUser().getProid());
			SocketManageImpl.getSocketManage().sendMessage(temp.getUser().getProid(),message);
		}
	}
}
