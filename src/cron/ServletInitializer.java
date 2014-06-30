package cron;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.UuidGenerator;

import dao.MySqlConnectionHelper;
import entity.MapData;

 

public class ServletInitializer extends HttpServlet {
	/**
	 * 这个在服务器开启时就启动
	 */
	private static final long serialVersionUID = 7861033593851152881L;
	Timer timer;
	private  Connection connection = null;
	private  Statement stmt = null;

	public  Connection getConnection() throws SQLException {
		return MySqlConnectionHelper.getConnection();
	}
	public void init() throws ServletException {
		// / Automatically java script can run here
		 
		
		System.out.println("************");
		System.out.println("***Cron Servlet Initialized successfully ***..");
		System.out.println("***********");
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		timer = new Timer();
		timer.schedule(new RemindTask(), 5* 1000);
		 
	}

	class RemindTask extends TimerTask {
		public void run() {
			//System.out.println("Time's up!");
			timer.cancel(); // Terminate the timer thread
			try {
				connection = getConnection();
				stmt = connection.createStatement();
				String sql="";
				ResultSet rs= null;
				
			    sql="select Moteid_ID,area,SensingType from Node";
				rs= stmt.executeQuery(sql);
				while(rs.next())
				{
					System.out.println(rs.getString("Moteid_ID")+","+rs.getString("area"));
					MapData.moteAreaMap.put(rs.getString("Moteid_ID"), rs.getString("area"));
					switch(rs.getString("SensingType"))
					{
					case "HT":MapData.moteTypeMap.put(rs.getString("Moteid_ID"), 0);
						      break;
					case "CO2":MapData.moteTypeMap.put(rs.getString("Moteid_ID"),2);
						       break;
					case "CO":MapData.moteTypeMap.put(rs.getString("Moteid_ID"),1);
						      break;
					case "L":MapData.moteTypeMap.put(rs.getString("Moteid_ID"),10);
						      break;
					case "SHT":MapData.moteTypeMap.put(rs.getString("Moteid_ID"),9);
						      break;
					case "PH":MapData.moteTypeMap.put(rs.getString("Moteid_ID"),8);
						      break;
					default:break;
					}
				//	System.out.println("节点 类型"+rs.getString("Moteid_ID")+","+MapData.moteTypeMap.get(rs.getString("Moteid_ID")));
				}
				
				 sql="select * from MyControlInfo order by time asc";
				 rs= stmt.executeQuery(sql);
				 String myKey="";
				 String myValue="";
				 while(rs.next())
					{
						 myKey=rs.getString("groupId");
						 myValue=rs.getString("mycondition")+";"+rs.getString("operation")+";"+rs.getString("time");
			//	 	 System.out.println( myKey+","+ myValue);
						 MapData.settingMap.put(myKey, myValue);
					}
				
				} catch (SQLException e) {
	             throw new RuntimeException(e);
			} finally {
	            MySqlConnectionHelper.closeStatement(stmt);
				MySqlConnectionHelper.close(connection);
			} 
		}
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
}