package utility;

import java.sql.Connection;
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

public class DealData {
	private  Connection connection = null;
	private  Statement stmt = null;

	public  Connection getConnection() throws SQLException {
		return MySqlConnectionHelper.getConnection();
	}
	
	public  void DealWithData(SocketData data) {
		
		List<DataType_Property> dplist = data.getDataType().getPropertyList();
		DataType dataType = data.getDataType();	
		SendDataToApp.SendData(dataType);
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			String colStr="";
			String valueStr="";
			colStr+="TimestampArrive_TM,Id,";
		     
		    Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dt = sdf.format(date);
			
			String idStr="";
			idStr=UuidGenerator.generate32UUID();
		     valueStr+="'"+dt+"','"+idStr+"',";
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
			//	System.out.println(sql);
		 
		    stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
             throw new RuntimeException(e);
		} finally {
            MySqlConnectionHelper.closeStatement(stmt);
			MySqlConnectionHelper.close(connection);
		}
	}
}
