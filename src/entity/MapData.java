package entity;

import java.util.HashMap;
import java.util.Map;
 

 
public class MapData {
 
	public static Map<String,String> identityMap = new HashMap<String,String>();//存储当前用户
	public static Map<String,String> settingMap = new HashMap<String,String>();//存储控制组
	public static Map<String,String> moteAreaMap = new HashMap<String,String>();//存储节点，区域对应关系
	public static Map<String,Double> newestDataMap = new HashMap<String,Double>();
}
