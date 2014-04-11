package service;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utility.UuidGenerator;
import dao.MySqlConnectionHelper;

import entity.MapData;
public class AreaMapService{
	private  Connection connection = null;
	private  Statement stmt = null;

	public  Connection getConnection() throws SQLException {
		return MySqlConnectionHelper.getConnection();
	}
public String  saveAreaMap(List<String> areaMap,String userIdentity)
{
	String[] tempStr=null;
	String status="";
	if(userIdentity.equals(""))
	{
		return status;
	}
	else 
	{
		if(MapData.identityMap.get("identity").equals(userIdentity))
		{
			for(int i=0;i<areaMap.size();i++)
			{
				//System.out.println(areaMap.get(i));
				tempStr=areaMap.get(i).split(",");
				MapData.moteAreaMap.put(tempStr[0], tempStr[1]);
			}
			sql_deal(areaMap);
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
public void sql_deal(List<String> areaMap)
{
	try {
		connection = getConnection();
		stmt = connection.createStatement();
		String[] tempStr=null;
		String sql="";
		String sql1="";
		ResultSet rs = null;
		for(int i=0;i<areaMap.size();i++)
		{
			//System.out.println(areaMap.get(i));
			tempStr=areaMap.get(i).split(",");
			
		
		sql="select * from Node where Moteid_ID="+tempStr[0];
		rs= stmt.executeQuery(sql);
		if(rs.next())
		{
			//修改
			sql1="update Node set area="+tempStr[1]+" where  Moteid_ID="+tempStr[0];
			System.out.println(sql1);
			stmt.executeUpdate(sql1);
			
		}
		else
		{
			System.out.println("节点---区域对应关系没有保存成功");
		}
		}
		
		
	} catch (SQLException e) {
         throw new RuntimeException(e);
	} finally {
        MySqlConnectionHelper.closeStatement(stmt);
		MySqlConnectionHelper.close(connection);
	} 
}
}
