package utility;

import java.util.List;

import com.run.park.entity.DataType;
import com.run.park.entity.DataType_Property;

import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

 
public class SendDataToApp {
	
	 
	public static void SendData(DataType datatype){
		//"字段名 字段值 ，字段名 字段值"
	//	System.out.println("-----------SendDataToApp--------------");
		MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
		String clientID = UUIDUtils.createUUID();
		List<DataType_Property> list = datatype.getPropertyList();
		String data = "";
		for(DataType_Property dp : list){
		//	System.out.println(dp.getColTable()+"//"+dp.getValue());
			if(dp.getColTable().equals("")||dp.getColTable()==null)
			{
				
			}
			else
			{
				data+=dp.getValue()+",";
			}
			
		}		
		data=data.substring(0, data.length()-1);
		AsyncMessage msg = new AsyncMessage();
		msg.setDestination("newSensing");
		msg.setHeader("DSSubtopic", "LiveSense");
		msg.setClientId(clientID);
		msg.setMessageId(UUIDUtils.createUUID());
		msg.setTimestamp(System.currentTimeMillis());
		msg.setBody(data);
		while(msgBroker==null){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	   msgBroker.routeMessageToService(msg, null);
	}

}
