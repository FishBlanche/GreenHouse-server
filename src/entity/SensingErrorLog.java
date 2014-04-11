package entity;

import java.util.Date;

 


public class SensingErrorLog {
	private String id; 
 
	private Integer node_id;  
	private String info; 
	private String createDate; 
	
	public String getId() {
		return this.id;
	}
    public void setId(String id) {
		this.id = id;
	}
   
    public Integer getNode_id() {
		return this.node_id;
	}
    public void setNode_id(Integer node_id) {
		this.node_id = node_id;
	}
    public String getInfo() {
		return this.info;
	}
    public void setInfo(String info) {
		this.info = info;
	}

	public String getCreateDate() {
		return this.createDate;
	}
    public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
