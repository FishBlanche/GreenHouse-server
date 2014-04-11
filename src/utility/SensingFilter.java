package utility;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import service.DeviceControlService;

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
		System.out.println("-----------saveErrorData--------------");
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
	public  SocketData correct(SocketData data){
		
		String currentArea="";
		String newestDataKey="";
		int index = 0;
		DataType dataType = data.getDataType();
		DataType_Property property = null;
		double value = 0;
		List<DataType_Property> dplist = data.getDataType().getPropertyList();
		int node_id = 0;
		 
		int nodeType = 0;
		for(int i=0;i<dplist.size();i++){
			//获取id
			if(dplist.get(i).getColTable().equals("Moteid_ID")){
				node_id = Integer.parseInt(dplist.get(i).getValue());	
	     	}
			 
			//获取nodeType
			if(dplist.get(i).getColTable().equals("nodetype")){
				nodeType = Integer.parseInt(dplist.get(i).getValue());				
			}
		}
		if(MapData.moteAreaMap.containsKey(String.valueOf(node_id)))
		{
			currentArea=MapData.moteAreaMap.get(String.valueOf(node_id));//获得当前新数据节点所在的区域
		}
	 
		//nodeType出错
		if(nodeType>10 || nodeType<0){
			System.out.println("-----------nodeType出错--------------");
			return null;
		}
		
		while(index < dataType.getPropertyList().size()){
			property = dataType.getPropertyList().get(index);
			//检测是否符合条件			
			if(property.getColTable().equals(null) || property.getColTable() == "" || property.getColTable().equals("")) {
				index++;
				continue;				
			}
			switch (property.getColTable()) {
			case "temperature":
				value = Double.parseDouble(property.getValue());							
				if(value<-30 || value>100){
					newestDataKey="1"+","+currentArea;//某个区域内的温度
					value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
				} 
				property.setValue(value+"");
				newestDataKey="1"+","+currentArea;//某个区域内的温度
				MapData.newestDataMap.put(newestDataKey,value);
				break;
			case "humidity":
				 value = Double.parseDouble(property.getValue());
				if(value<0 || value>100){
					 newestDataKey="2"+","+currentArea;
					 value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
				}
				property.setValue(value+"");
				newestDataKey="2"+","+currentArea;
				MapData.newestDataMap.put(newestDataKey,value);
				break;
			case "photo_active":
			    value = Double.parseDouble(property.getValue());
				if(value<0 || value>10000){
					 newestDataKey="5"+","+currentArea;
				     value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
				}
				property.setValue(value+"");
				newestDataKey="5"+","+currentArea;
				MapData.newestDataMap.put(newestDataKey,value);
				break;
			case "co2":
			    value = Double.parseDouble(property.getValue());
				if(nodeType==1){//一氧化碳
					 if(value<0 || value>200){
						 newestDataKey="7"+","+currentArea;
						value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
					}
				    property.setValue(value+"");
				    
				    newestDataKey="7"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				}else if(nodeType==2){//二氧化碳
					if(value<=0 || value>5000){
						newestDataKey="8"+","+currentArea;
						value = MapData.newestDataMap.get(newestDataKey)==null?0: MapData.newestDataMap.get(newestDataKey);
					}
					property.setValue(value+"");
				    newestDataKey="8"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				} 
				else if(nodeType==8){//PH
					if(value<0 || value>14){
					    newestDataKey="6"+","+currentArea;
						value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
					}
				
					property.setValue(value+"");
				    newestDataKey="6"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				} 
				break;
			case "A0":
				if(nodeType==9){//土壤温度
					value = Double.parseDouble(property.getValue());							
					if(value<-30 || value>70){
						 newestDataKey="3"+","+currentArea;
						value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
					}
				    property.setValue(value+"");
					 newestDataKey="3"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				}
				break;
			case "A1":
                if(nodeType==9){//土壤湿度
                	value = Double.parseDouble(property.getValue());
    				if(value<0 || value>100){
    					  newestDataKey="4"+","+currentArea;
    					 value = MapData.newestDataMap.get(newestDataKey)==null?0:MapData.newestDataMap.get(newestDataKey);
    				}
    	            property.setValue(value+"");
    	            newestDataKey="4"+","+currentArea;
					MapData.newestDataMap.put(newestDataKey,value);
				}
				break;
			default:
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
                  //  System.out.println("myValue[0] myValue[1]"+myValue[0]+"   "+myValue[1]);
                    controlJudge(myValue[0],myValue[1]);
                   }  
   	 }
	}
	public  void controlJudge(String myCondition,String myControl){
		//System.out.println("myCondition"+myCondition+"myControl"+myControl);
		String[] myConditions=myCondition.split(":");
		String[] myControls=myControl.split("a");
		String[] tempCondition=null;
		String[] tempControl=null;
		String myKey="";
		String myLogic="r";
		boolean flag=false;
		for(int i=0;i<myControls.length;i++)
		{
			 myLogic="r";
			 flag=false;
			tempControl=myControls[i].split(",");
			if(tempControl[0].equals("1"))//设备类型为灌溉(花洒),要分区域
			{
				 
				for(int j=0;j<myConditions.length;j++)
				{
					tempCondition=myConditions[j].split(",");
					
					if(tempCondition[1].equals("1"))//大于
					{
						
						if(MapData.newestDataMap.containsKey(tempCondition[0]+","+tempControl[1])&&MapData.newestDataMap.get(tempCondition[0]+","+tempControl[1])>Double.parseDouble(tempCondition[2]))
						{
							 
							 if(myLogic.equals("a"))
							 {
								 flag=flag&&true;
							 }
							 else
							 {
								 flag=flag||true;
							 }
						}
						else
						{
							if(myLogic.equals("a"))
							 {
								 flag=flag&&false;
							 }
							 else
							 {
								 flag=flag||false;
							 }
					 	}
					}
					else//小于
					{
						 
							
						if(MapData.newestDataMap.containsKey(tempCondition[0]+","+tempControl[1])&&MapData.newestDataMap.get(tempCondition[0]+","+tempControl[1])<Double.parseDouble(tempCondition[2]))
						{
							
							 if(myLogic.equals("a"))
							 {
								 flag=flag&&true;
							 }
							 else
							 {
								 flag=flag||true;
							 }
						}
						else
						{
							if(myLogic.equals("a"))
							 {
								
								 flag=flag&&false;
							 
							 }
							 else
							 {
								 flag=flag||false;
							 }
						}
					}
					if(tempCondition.length==4)//后面还有判断条件
					{
						if(tempCondition[3].equals("a"))
						{
							myLogic="a";
						}
						else
						{
							myLogic="r";
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
						if((MapData.newestDataMap.containsKey(tempCondition[0]+","+"1")&&MapData.newestDataMap.get(tempCondition[0]+","+"1")>Double.parseDouble(tempCondition[2]))||(MapData.newestDataMap.containsKey(tempCondition[0]+","+"2")&&MapData.newestDataMap.get(tempCondition[0]+","+"2")>Double.parseDouble(tempCondition[2]))||(MapData.newestDataMap.containsKey(tempCondition[0]+","+"3")&&MapData.newestDataMap.get(tempCondition[0]+","+"3")>Double.parseDouble(tempCondition[2]))||(MapData.newestDataMap.containsKey(tempCondition[0]+","+"4")&&MapData.newestDataMap.get(tempCondition[0]+","+"4")>Double.parseDouble(tempCondition[2])))
						{
							 if(myLogic.equals("a"))
							 {
								 flag=flag&&true;
							 }
							 else
							 {
								 flag=flag||true;
							 }
						}
						else
						{
							if(myLogic.equals("a"))
							 {
								 flag=flag&&false;
							 }
							 else
							 {
								 flag=flag||false;
							 }
						}
					}
					else//小于
					{
						if((MapData.newestDataMap.containsKey(tempCondition[0]+","+"1")&&MapData.newestDataMap.get(tempCondition[0]+","+"1")<Double.parseDouble(tempCondition[2]))||(MapData.newestDataMap.containsKey(tempCondition[0]+","+"2")&&MapData.newestDataMap.get(tempCondition[0]+","+"2")<Double.parseDouble(tempCondition[2]))||(MapData.newestDataMap.containsKey(tempCondition[0]+","+"3")&&MapData.newestDataMap.get(tempCondition[0]+","+"3")<Double.parseDouble(tempCondition[2]))||(MapData.newestDataMap.containsKey(tempCondition[0]+","+"4")&&MapData.newestDataMap.get(tempCondition[0]+","+"4")<Double.parseDouble(tempCondition[2])))
						{
							 if(myLogic.equals("a"))
							 {
								 flag=flag&&true;
							 }
							 else
							 {
								 flag=flag||true;
							 }
						}
						else
						{
							if(myLogic.equals("a"))
							 {
								 flag=flag&&false;
							 }
							 else
							 {
								 flag=flag||false;
							 }
						}
					}
					if(tempCondition.length==4)//后面还有判断条件
					{
						if(tempCondition[3].equals("a"))
						{
							myLogic="a";
						}
						else
						{
							myLogic="r";
						}
					}
					else//判断条件结束
						break;
				}
			}
			if(flag)
			{
				if(tempControl[0].equals("2"))//风扇,默认两个风扇都开
				{
					SendControl(convert("5",tempControl[0],tempControl[3],tempControl[2]));
					SendControl(convert("6",tempControl[0],tempControl[3],tempControl[2]));
				}
				else
				{
					SendControl(convert(tempControl[1],tempControl[0],tempControl[3],tempControl[2]));
				}
			}
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
		return str;
	}
	public  void SendControl(String message){
		System.out.println("自动控制触发："+message);
		Iterator<CurrentSocketDao> temps = SocketManageImpl.getSocketManage().getLinkList().iterator();
		while(temps.hasNext()){
			CurrentSocketDao temp = temps.next();
		//	System.out.println("当前："+temp.getUser().getProid());
			SocketManageImpl.getSocketManage().sendMessage(temp.getUser().getProid(),message);
		}
	}
}
