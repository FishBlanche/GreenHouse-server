package service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import utility.ComparatorUtil;


import entity.APPData;

import dao.MySqlConnectionHelper;
import entity.SensingEntry;

 

 

public class FindLastSense {
	private Connection connection = null;
	private Statement stmt = null;
	ComparatorUtil comparator=new ComparatorUtil();
	private SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd   HH:mm:ss"); 
	public Connection getConnection() throws SQLException {
		 
		return MySqlConnectionHelper.getConnection();
	}
 
	public List<List> newGetLastSense(String TH_ID, String STH_ID,
			String CO_ID, String PH_ID, String CO2_ID, String L_ID,int days) {
	//	System.out.println("newGetLastSense");
		
		List<APPData> temperlistData = new ArrayList<APPData>();
		List<APPData> humilistData = new ArrayList<APPData>();
		List<APPData> stemperlistData = new ArrayList<APPData>();
		List<APPData> shumilistData = new ArrayList<APPData>();
		List<APPData> colistData = new ArrayList<APPData>();
		List<APPData> phlistData = new ArrayList<APPData>();
		List<APPData> co2listData = new ArrayList<APPData>();
		List<APPData> llistData = new ArrayList<APPData>();
		List<List> list = new ArrayList<List>();
	//	Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	//	String dateString = sdf.format(date);
		try {
			connection = getConnection();
			stmt = connection.createStatement();

			ResultSet rs1 = null;
		//	int days=30;
	//		String sql = "select Moteid_ID,temperature,humidity,photo_active,co2,A0,A1,TimestampArrive_TM from DataCollect where  TimestampArrive_TM> '"+ dateString + " 00:00:00'";
			String sql = "select Moteid_ID,temperature,humidity,photo_active,co2,A0,A1,TimestampArrive_TM from DataCollect where  TimestampArrive_TM >TIMESTAMP(DATE_SUB(NOW(), INTERVAL "
					+ days + " day))";
					 
            
			rs1 = stmt.executeQuery(sql);
			
			while (rs1.next()) {
			 
				if (rs1.getString("Moteid_ID").equals(TH_ID)) {
					APPData temper = new APPData();
					temper.setValue(rs1.getString("temperature"));
				//	temper.setTimestamparrive_tm(rs1.getString("TimestampArrive_TM").split(" ")[1]);
					 
					try {
						temper.setTimestamparrive_tm(sDateFormat.parse(sDateFormat.format(rs1.getTimestamp("TimestampArrive_TM"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					temperlistData.add(temper);
					APPData humi = new APPData();
					humi.setValue(rs1.getString("humidity"));
				//	humi.setTimestamparrive_tm(rs1.getString("TimestampArrive_TM").split(" ")[1]);
					try {
						humi.setTimestamparrive_tm(sDateFormat.parse(sDateFormat.format(rs1.getTimestamp("TimestampArrive_TM"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					humilistData.add(humi);
				} else if (rs1.getString("Moteid_ID").equals(STH_ID)) {
					APPData stemper = new APPData();
					stemper.setValue(rs1.getString("A0"));
				//	stemper.setTimestamparrive_tm(rs1.getString("TimestampArrive_TM").split(" ")[1]);
					try {
						stemper.setTimestamparrive_tm(sDateFormat.parse(sDateFormat.format(rs1.getTimestamp("TimestampArrive_TM"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stemperlistData.add(stemper);
					APPData shumi = new APPData();
					shumi.setValue(rs1.getString("A1"));
				//	shumi.setTimestamparrive_tm(rs1.getString("TimestampArrive_TM").split(" ")[1]);
					try {
						shumi.setTimestamparrive_tm(sDateFormat.parse(sDateFormat.format(rs1.getTimestamp("TimestampArrive_TM"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					shumilistData.add(shumi);
				} else if (rs1.getString("Moteid_ID").equals(CO_ID)) {
					APPData myco = new APPData();
					myco.setValue(rs1.getString("co2"));
				//	myco.setTimestamparrive_tm(rs1.getString("TimestampArrive_TM").split(" ")[1]);
					try {
						myco.setTimestamparrive_tm(sDateFormat.parse(sDateFormat.format(rs1.getTimestamp("TimestampArrive_TM"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					colistData.add(myco);
				} else if (rs1.getString("Moteid_ID").equals(PH_ID)) {

					APPData myph = new APPData();
					myph.setValue(rs1.getString("co2"));
					//myph.setTimestamparrive_tm(rs1.getString("TimestampArrive_TM").split(" ")[1]);
					try {
						myph.setTimestamparrive_tm(sDateFormat.parse(sDateFormat.format(rs1.getTimestamp("TimestampArrive_TM"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					phlistData.add(myph);
				} else if (rs1.getString("Moteid_ID").equals(CO2_ID)) {
                    APPData myco2 = new APPData();
					myco2.setValue(rs1.getString("co2"));
			//		myco2.setTimestamparrive_tm(rs1.getString("TimestampArrive_TM").split(" ")[1]);
					try {
						myco2.setTimestamparrive_tm(sDateFormat.parse(sDateFormat.format(rs1.getTimestamp("TimestampArrive_TM"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					co2listData.add(myco2);
				} else if (rs1.getString("Moteid_ID").equals(L_ID)) {

					APPData myl = new APPData();
					myl.setValue(rs1.getString("photo_active"));
			//		myl.setTimestamparrive_tm(rs1.getString("TimestampArrive_TM").split(" ")[1]);
					try {
						myl.setTimestamparrive_tm(sDateFormat.parse(sDateFormat.format(rs1.getTimestamp("TimestampArrive_TM"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					llistData.add(myl);
				} else {

				}
			}

		} catch (SQLException e) {

			throw new RuntimeException(e);

		} finally {
			MySqlConnectionHelper.closeStatement(stmt);
			MySqlConnectionHelper.close(connection);
		}
		Collections.sort(temperlistData, comparator);
		list.add(temperlistData);
		Collections.sort(humilistData, comparator);
		list.add(humilistData);
		Collections.sort(stemperlistData, comparator);
		list.add(stemperlistData);
		Collections.sort(shumilistData, comparator);
		list.add(shumilistData);
		Collections.sort(colistData, comparator);
		list.add(colistData);
		Collections.sort(phlistData, comparator);
		list.add(phlistData);
		Collections.sort(co2listData, comparator);
		list.add(co2listData);
		Collections.sort(llistData, comparator);
		list.add(llistData);
		 /*
	 	System.out.println("list.size()" + list.size() + ","
				+ temperlistData.size() + "," + humilistData.size() + ","
				+ stemperlistData.size() + "," + shumilistData.size() + ","
				+ colistData.size() + "," + phlistData.size() + ","
				+ co2listData.size() + "," + llistData.size());*/
		return list;
	}
}
