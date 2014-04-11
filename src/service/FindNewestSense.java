package service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.MySqlConnectionHelper;

import entity.NodeInfo;
import entity.SensingEntry;
public class FindNewestSense {
	private Connection connection = null;
	private Statement stmt = null;

	public Connection getConnection() throws SQLException {
		 
		return MySqlConnectionHelper.getConnection();
	}
	public  List<String> GetNewestSense () {
		System.out.println("GetNewestSense");
		 List<String> list = new ArrayList<String>();
		
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			
			ResultSet rs = null;
			String sql = "SELECT t.Moteid_ID,area,SensingType,temperature,humidity,photo_active,co2,nodetype FROM Node t LEFT OUTER JOIN (SELECT DataCollect.Moteid_ID,temperature,humidity,photo_active,co2,nodetype,max(DataCollect.TimestampArrive_TM) as TimestampArrive FROM DataCollect GROUP BY Moteid_ID )AS Data1 ON t.Moteid_ID=Data1.Moteid_ID;";
			rs = stmt.executeQuery(sql);
			while(rs.next()){ 
				String re="";
				SensingEntry se = new SensingEntry();
				se.setMoteId(rs.getInt("Moteid_ID"));
				se.setArea(rs.getInt("area"));
				se.setTemp(rs.getFloat("temperature"));
				se.setHumi(rs.getFloat("humidity"));
				se.setLight(rs.getFloat("photo_active"));
				se.setCO2(rs.getFloat("co2"));
				se.setSenseType(rs.getString("SensingType")); 
				se.setNodetype(rs.getInt("nodetype"));
				re="moteid_id:"+se.getMoteId()+" "+"area:"+se.getArea()+" "+"sensing_type:"+se.getSenseType()+" "+"temperature:"+se.getTemp()+" "+"humidity:"+se.getHumi()+" "+"light:"+se.getLight()+" "+"co2:"+se.getCO2()+" "+"nodetype:"+se.getNodetype();
			 	list.add(re);
		//		System.out.println("节点信息"+se.getMoteId()+","+se.getSenseType()+","+se.getTemp()+","+se.getHumi()+","+se.getLight()+","+se.getCO2()+","+se.getNodetype());
			}
			
		} catch (SQLException e) {

			throw new RuntimeException(e);
			
		} finally {
           MySqlConnectionHelper.closeStatement(stmt);
			MySqlConnectionHelper.close(connection);
		}
		return list;
	}
}
