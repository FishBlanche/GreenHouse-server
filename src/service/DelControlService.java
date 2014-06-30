package service;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import utility.UuidGenerator;

import com.run.park.entity.DataType_Property;

import dao.MySqlConnectionHelper;
 
import entity.MapData;
public class DelControlService {
	private  Connection connection = null;
	private  Statement stmt = null;

	public  Connection getConnection() throws SQLException {
		return MySqlConnectionHelper.getConnection();
	}
	 
	public String  DelControlSet(String controlGp,String userIdentity) {
	   String status="";
     //  System.out.println(userIdentity);
 
		if(userIdentity.equals(""))
		{
			return status;
		}
		else 
		{
			if(MapData.identityMap.get("identity")!=null&&MapData.identityMap.get("identity").equals(userIdentity))
			{
				String[] cigp=controlGp.split(";");
				if(MapData.settingMap.containsKey(cigp[0]))
				{
					MapData.settingMap.remove(cigp[0]);
					Sql_deal(cigp[0]);
				}
				 status="ok";
			}
			else
			{
				status="fail";
				return status;
			}
		}
		return status;
	} 
	
	 
	private void Sql_deal(String myKey)
	{
		 
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			ResultSet rs = null;
			
			String sql="select * from MyControlInfo where groupId='"+myKey+"'";
			
			rs= stmt.executeQuery(sql);
			if(rs.next())
			{
				//修改
				String sql1= "delete from MyControlInfo where groupId='"+myKey+"'";
				 
				stmt.executeUpdate(sql1);
			}
			 
		} catch (SQLException e) {
             throw new RuntimeException(e);
		} finally {
            MySqlConnectionHelper.closeStatement(stmt);
			MySqlConnectionHelper.close(connection);
		} 
	} 
}