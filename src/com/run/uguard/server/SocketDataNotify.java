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
		thread.start();
		notifyDaos.add(new acceptDataListener());
		return true;
		
	}
	public static SocketDataNotify getSocketDataInotify(){
		if(dataInotify == null){
			dataInotify = new SocketDataNotify();
		}
		return dataInotify;
	}
	public void notifys(SocketData data){		
//	    System.out.println("监听对象大小："+notifyDaos.size()+"到达数据：\n"+ 
//	data.getDataType().getMapTable()+":"+data.getDataType().getPropertyList().size()+
//	"eg:\n"+data.getDataType().getPropertyList().get(0).getMean()+":"+data.getDataType().getPropertyList().get(0).getType()+":"+data.getDataType().getPropertyList().get(0).getValue());	

		//System.out.println("监听对象大小："+notifyDaos.size());
	    
		 valueChange(data);
		// System.out.println("notifys"+ notifyDaos.size());
		
		Iterator<NotifyDao>  temps =  notifyDaos.iterator();
		while(temps.hasNext()){
			//System.out.println("temps.hasNext()");
			temps.next().eventChanged(data);
		}
		
	}
	private void valueChange(SocketData data) {
		// TODO Auto-generated method stub
		
		List<DataType_Property> temp = data.getDataType().getPropertyList();
		
		  	int index = 0;
		  	int nodetype = 0;
		  	int moteid =0;
		   
		  	nodetype= Integer.parseInt(temp.get(11).getValue(),16);
		  	moteid =  Integer.parseInt(temp.get(0).getValue(),16);
			System.out.println("moteid nodetype"+moteid+":"+nodetype);
			
			DataType_Property property = null;
			double temperature = 0;
			while(index <temp.size()){
				property = temp.get(index);
				//检测是否符合条件
				if(property.getColTable().equals(null) || property.getColTable() == "" || property.getColTable().equals("")) {
					index++;
					continue;
				}
				switch (property.getType()) {
				case "int":
					 if(property.getColTable().equals("co2")){
						
							//11
							if(nodetype == 1){  //一氧化碳
								DecimalFormat df=new DecimalFormat(".##");
								String str = df.format(((Integer.parseInt(property.getValue(), 16)*(2048.0/32767))*0.199204-224.1));
								property.setValue(str);
								property.setType("float");
								break;
							}
							if(nodetype == 2){  //二氧化碳
								String str =	String.valueOf(Integer.parseInt(property.getValue(),16)) ;
								property.setValue(str);
								property.setType("float");
								break; 
							}
							else if(nodetype ==8)//ph
							{
							    // System.out.println("ph"+property.getValue());
								System.out.println("ph"+Integer.parseInt(property.getValue(),16));
						         double phtem =Integer.parseInt(property.getValue(),16)/100.0;
								 String str =	String.valueOf(phtem) ;
							 
								property.setValue(str);
								property.setType("int");
							 	System.out.println("ph"+property.getValue());
							}
							else
							{
								String str =	String.valueOf(Integer.parseInt(property.getValue(),16)) ;
								property.setValue(str);
								property.setType("int");
							}
		     	}
				else if(property.getColTable().equals("humidity")){
						
							double tem = Integer.parseInt(property.getValue(), 16);
							tem = (tem*0.0405 - tem*tem*0.0000028 -4);
							DecimalFormat df=new DecimalFormat(".##");
							String str = df.format(tem);
							property.setValue(str);
							if(temperature!=25.0){
								tem = (double) ((temperature - 25)*(0.01+0.00008*tem)+tem);
								str = df.format(tem);
								property.setValue(str);
							}
							property.setType("float");
					}
				else if(property.getColTable().equals("temperature")){
						
							temperature = (Integer.parseInt(property.getValue(), 16) * 0.01 - 40);
							DecimalFormat df=new DecimalFormat(".##");
							String str = df.format(temperature);
							property.setValue(str);
							property.setType("float");
								
					}
				else if(property.getColTable().equals("photo_active")){
						
							DecimalFormat df=new DecimalFormat(".##");
							String str = df.format((Integer.parseInt(property.getValue(), 16) * 2.888));
							property.setValue(str);
							property.setType("float");
					}
				else if(property.getColTable().equals("A0")){
					if(nodetype==9)//土壤温度
					{
						double t= Integer.parseInt(property.getValue(), 16)*25/4096.0 ;
						temperature=11.68*(5*(t-4)/32)*(5*(t-4)/32)*(5*(t-4)/32)-31.27*(5*(t-4)/32)*(5*(t-4)/32)+43.22*(5*(t-4)/32)-2.64;
						DecimalFormat df=new DecimalFormat(".##");
						String str = df.format(temperature);
						property.setValue(str);
						property.setType("float");
						//System.out.println("土壤温度"+str);
					}
					else
					{
						String str =	String.valueOf(Integer.parseInt(property.getValue(),16)) ;
						property.setValue(str);
						property.setType("int");
					}
				 }
				else if(property.getColTable().equals("A1")){
					if(nodetype==9)//土壤湿度
					{
						double t= Integer.parseInt(property.getValue(), 16)*25/4096.0 ;
						 
						DecimalFormat df=new DecimalFormat(".##");
						String str = df.format(6.345*t-57.698);
						property.setValue(str);
						property.setType("float");
						//System.out.println("土壤湿度"+str);
						
					}
					else
					{
						String str =	String.valueOf(Integer.parseInt(property.getValue(),16)) ;
						property.setValue(str);
						property.setType("int");
					}
				}
				else {
					String str =String.valueOf(Integer.parseInt(property.getValue(),16));
					property.setValue(str);
					property.setType("int");
			        }
					break;
				case "long":
					property.setValue(""+Long.parseLong(property.getValue(), 16));
					break;
				case "string":
		        	break;
				default:
					break;
				}
				index++;
			}
		/*	index=0;
			while(index <temp.size())
			{
				System.out.println("键值"+temp.get(index).getColTable()+","+temp.get(index).getValue());
				index++;
			}*/
		
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
