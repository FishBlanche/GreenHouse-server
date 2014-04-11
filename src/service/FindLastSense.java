package service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import entity.APPData;

import dao.MySqlConnectionHelper;
import entity.SensingEntry;

 

 

public class FindLastSense {
	private Connection connection = null;
	private Statement stmt = null;

	public Connection getConnection() throws SQLException {
		 
		return MySqlConnectionHelper.getConnection();
	}
	public List<APPData> GetData(String sql)
	{
		List<APPData> listData = new ArrayList<APPData>();
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			String t1[];
			String t2[];
		 
			ResultSet rs1 = null;
			rs1 = stmt.executeQuery(sql);
		 
			while(rs1.next()){			
				APPData vl = new APPData();
				vl.setValue(rs1.getString(1));
				t1=rs1.getString(2).split(" ");
			 
			 
				vl.setTimestamparrive_tm(t1[1]);
			 
				listData.add(vl);
			}
			
		} catch (SQLException e) {

			throw new RuntimeException(e);
			
		} finally {
           MySqlConnectionHelper.closeStatement(stmt);
			MySqlConnectionHelper.close(connection);
		}
		return listData;
	}
	public List<List> GetLastSense(String TH_ID,String STH_ID,String CO_ID,String PH_ID,String CO2_ID,String L_ID)
	{
	//	System.out.println("GetLastSense"+TH_ID+","+STH_ID+","+CO_ID+","+PH_ID+","+CO2_ID+","+L_ID);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = sdf.format(date);
		//dateString=dateString+" "+"00:00:00";
		 List<List> list = new ArrayList<List>();
		 if(TH_ID.equals(""))
		 {
			 list.add(null);
		 }
		 else
		 {
			 String sql1 = "select temperature,TimestampArrive_TM from DataCollect where Moteid_ID="+TH_ID+" and TimestampArrive_TM> '"+dateString+" 00:00:00' order by TimestampArrive_TM asc";
			// System.out.println(sql1);
		//	 System.out.println("GetData(sql1).size()"+GetData(sql1).size());
		        list.add(GetData(sql1));
		 }
		 if(TH_ID.equals(""))
		 {
			 list.add(null);
		 }
		 else
		 {
			  String sql2 = "select humidity,TimestampArrive_TM from DataCollect where Moteid_ID="+TH_ID+" and TimestampArrive_TM> '"+dateString+" 00:00:00' order by TimestampArrive_TM asc";
			//  System.out.println(sql2);
		//	  System.out.println("GetData(sql2).size()"+GetData(sql2).size());
		       list.add(GetData(sql2));
		 }
        if(STH_ID.equals(""))
        {
        	list.add(null);
        }
        else
        {
        	 String sql3 = "select temperature,TimestampArrive_TM from DataCollect where Moteid_ID="+STH_ID+" and TimestampArrive_TM> '"+dateString+" 00:00:00' order by TimestampArrive_TM asc";
        //	  System.out.println(sql3); 
        //	  System.out.println("GetData(sql3).size()"+GetData(sql3).size());
             list.add(GetData(sql3));
        }
       if(STH_ID.equals(""))
       {
    	   list.add(null);
       }
       else
       {
    	   String sql4 = "select humidity,TimestampArrive_TM from DataCollect where Moteid_ID="+STH_ID+" and TimestampArrive_TM> '"+dateString+" 00:00:00' order by TimestampArrive_TM asc";
    	 //  System.out.println(sql4);
    	//   System.out.println("GetData(sql4).size()"+GetData(sql4).size());
           list.add(GetData(sql4));
       }
       if(CO_ID.equals(""))
       {
    	   list.add(null);
       }
       else
       {
    	   String sql5 = "select co2,TimestampArrive_TM from DataCollect where Moteid_ID="+CO_ID+" and TimestampArrive_TM> '"+dateString+" 00:00:00' order by TimestampArrive_TM asc";
    	//   System.out.println(sql5);
    	 //  System.out.println("GetData(sql5).size()"+GetData(sql5).size());
           list.add(GetData(sql5));
       }
       if(PH_ID.equals(""))
       {
    	   list.add(null);
       }
       else
       {
    	   String sql6 = "select co2,TimestampArrive_TM from DataCollect where Moteid_ID="+PH_ID+" and TimestampArrive_TM> '"+dateString+" 00:00:00' order by TimestampArrive_TM asc";
  //  	   System.out.println(sql6);
//    	   System.out.println("GetData(sql6).size()"+GetData(sql6).size());
           list.add(GetData(sql6));
       }
       if(CO2_ID.equals(""))
       {
    	   list.add(null);
       }
       else
       {
    	   String sql7 = "select co2,TimestampArrive_TM from DataCollect where Moteid_ID="+CO2_ID+" and TimestampArrive_TM> '"+dateString+" 00:00:00' order by TimestampArrive_TM asc";
    	//   System.out.println(sql7);
    //	   System.out.println("GetData(sql7).size()"+GetData(sql7).size());
           list.add(GetData(sql7));
       }
       if(L_ID.equals(""))
       {
    	   list.add(null);
       }
       else
       {
    	   String sql8 = "select photo_active,TimestampArrive_TM from DataCollect where Moteid_ID="+L_ID+" and TimestampArrive_TM> '"+dateString+" 00:00:00' order by TimestampArrive_TM asc";
    //       System.out.println(sql8);
       //    System.out.println("GetData(sql8).size()"+GetData(sql8).size());
           list.add(GetData(sql8));
       }
       System.out.println("list.size()"+list.size());
		return list;
	}
}
