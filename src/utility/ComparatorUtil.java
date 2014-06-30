package utility;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import entity.APPData;
public class ComparatorUtil implements Comparator{
	 public int compare(Object arg0, Object arg1) {
		 APPData user0=(APPData)arg0;
		 APPData user1=(APPData)arg1;

		   //首先比较年龄，如果年龄相同，则比较名字

		  int flag=user0.getTimestamparrive_tm().compareTo(user1.getTimestamparrive_tm());
		  
		   return flag;
		 
		 }
}
