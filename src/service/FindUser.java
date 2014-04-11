package service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import dao.MySqlConnectionHelper;
import entity.APPData;
import entity.MapData;



public class FindUser {
	private Connection connection = null;
	private Statement stmt = null;

	public Connection getConnection() throws SQLException {
		 
		return MySqlConnectionHelper.getConnection();
	}
	public String  queryUser(String userName, String password) {
		String sql = "select * from UserInfo where username='"+userName+"' and password='"+password+"'";
		String currentIdentity="";
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			
			ResultSet rs= null;
			rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = sdf.format(date);
				currentIdentity=userName+":"+password+":"+dateString;
				MapData.identityMap.put("identity", currentIdentity);
				System.out.println("当前活动者"+MapData.identityMap.get("identity"));
			}
			
			
		} catch (SQLException e) {

			throw new RuntimeException(e);
			
		} finally {
           MySqlConnectionHelper.closeStatement(stmt);
			MySqlConnectionHelper.close(connection);
		}
		return	currentIdentity;
	}

}
