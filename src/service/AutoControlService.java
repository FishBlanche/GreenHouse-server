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
public class AutoControlService {
	private  Connection connection = null;
	private  Statement stmt = null;

	public  Connection getConnection() throws SQLException {
		return MySqlConnectionHelper.getConnection();
	}
	 
	public String  ControlSet(String controlGp,String userIdentity) {
	   String status="";
   //    System.out.println(userIdentity);
	 //System.out.println("ci.SensorType ci.RangeType ci.Range ci.deviceType ci.deviceId   ci.status ci.Duration"+ci.SensorType+","+ci.RangeType+","+ci.Range+","+ci.deviceType+","+ci.deviceId+","+ci.status+","+ci.Duration);

		if(userIdentity.equals(""))
		{
			return status;
		}
		else 
		{
			if(MapData.identityMap.get("identity")!=null&&MapData.identityMap.get("identity").equals(userIdentity))
			{
				String myKey="";
				String myValue="";
				String[] cigp=controlGp.split(";");
				myKey=cigp[0];
				myValue=cigp[1]+";"+cigp[2]+";"+cigp[3];
			//	System.out.println("ControlSet"+myKey+"::"+myValue);
				MapData.settingMap.put(myKey, myValue);
				Sql_deal(myKey,myValue);
				status=myKey+";"+myValue;
		//		System.out.println("status"+status);
			}
			else
			{
				status="fail";
				return status;
			}
		}
		return status;
	} 
	
	 
	private void Sql_deal(String myKey,String myValue)
	{
		 
		try {
			String[] valuegp=myValue.split(";");
			connection = getConnection();
			stmt = connection.createStatement();
			ResultSet rs = null;
			
			String sql="select * from MyControlInfo where groupId='"+myKey+"'";
			rs= stmt.executeQuery(sql);
			if(rs.next())
			{
				//修改
				String sql1="update MyControlInfo set mycondition='"+valuegp[0]+"',operation='"+valuegp[1]+"',time='"+valuegp[2]+"'  where groupId='"+myKey+"'";
			//	System.out.println(sql1);

				stmt.executeUpdate(sql1);
			}
			else
			{
				//插入
			//	String idStr="";
			//	idStr=UuidGenerator.generate32UUID();
				String sql2="insert into MyControlInfo(groupId,mycondition,operation,time) values('"+myKey+"','"+valuegp[0]+"','"+valuegp[1]+"','"+valuegp[2]+"')";
			//	System.out.println(sql2);
				stmt.executeUpdate(sql2);
			}
		} catch (SQLException e) {
             throw new RuntimeException(e);
		} finally {
            MySqlConnectionHelper.closeStatement(stmt);
			MySqlConnectionHelper.close(connection);
		} 
	} 
}
