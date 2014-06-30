package utility;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import utility.SendDataToApp;
import utility.UuidGenerator;
import com.run.park.entity.DataType;
import com.run.park.entity.DataType_Property;
import com.run.uguard.entity.SocketData;

import dao.MySqlConnectionHelper;
import java.util.Timer;
import java.util.TimerTask;
public class DealData {
	private  Connection connection = null;
	private  Statement stmt = null;
	private	SocketData currentdata=null;
	
	public  Connection getConnection() throws SQLException {
		return MySqlConnectionHelper.getConnection();
	}
	public DealData()
	{
		Timer timer = new Timer();
		timer.schedule(new MyTask(), 10000, 1000*60*30);
	}
	class MyTask extends TimerTask {
		public void run() {
			if(currentdata!=null)
			{
				List<DataType_Property> dplist = currentdata.getDataType().getPropertyList();
				try {
					connection = getConnection();
					stmt = connection.createStatement();
					String colStr="";
					String valueStr="";
					
					colStr+="TimestampArrive_TM,";
					 
				    Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dt = sdf.format(date);
					 
					//String idStr="";
				//	idStr=UuidGenerator.generate32UUID();
				    valueStr+="'"+dt+"',";
				 
					String sql="";
					for(DataType_Property dp : dplist){
				//	System.out.println(dt+"||"+dp.getColTable()+"::"+dp.getValue());
						if(dp.getColTable().equals("")||dp.getColTable()==null)
						{
							
						}
						else
						{
							 if(dp.getType().equals("string"))
							{
								colStr+=dp.getColTable()+",";
								valueStr+="'"+dp.getValue()+"',";
							}
							else
							{
								colStr+=dp.getColTable()+",";
								valueStr+=dp.getValue()+",";
							}
						}
				
					}
					 
					colStr=colStr.substring(0, colStr.length()-1);
					valueStr=valueStr.substring(0, valueStr.length()-1);
				 		sql = "INSERT INTO  DataCollect"+"("+colStr+")"+" VALUES "+"("+valueStr+")";
				 //     System.out.println(sql);
				 
				    stmt.executeUpdate(sql);
					
				} catch (SQLException e) {
		             throw new RuntimeException(e);
				} finally {
		            MySqlConnectionHelper.closeStatement(stmt);
					MySqlConnectionHelper.close(connection);
				}
			}
			
		}
	}
	public  void DealWithData(SocketData data) {
		currentdata=data;
		List<DataType_Property> dplist = data.getDataType().getPropertyList();
		DataType dataType = data.getDataType();	
		SendDataToApp.SendData(dataType);
		
	}
}
