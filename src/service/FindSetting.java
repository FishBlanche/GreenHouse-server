package service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import entity.ControlGroup;
import entity.MapData;
 

public class FindSetting {
     public List<ControlGroup>  findControInfo()
     {
    	  
    	 List<ControlGroup> resultCollect=new ArrayList<ControlGroup>();
    	 String skey="";
    	 String svalue="";
    	 
    	 Set keys=MapData.settingMap.keySet();
    	 if(keys!= null) {  
               Iterator iterator = keys.iterator( );  
               while(iterator.hasNext()) { 
            	      ControlGroup cg=new ControlGroup();
                      skey = iterator.next( ).toString();  
                      svalue = MapData.settingMap.get(skey);
                      cg.gpId=skey;
                      cg.gpValue=svalue;
                     
                      resultCollect.add(cg);
                     }  
    	 }
      return resultCollect;
     }
  
}
