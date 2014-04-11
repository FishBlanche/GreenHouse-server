package entity;

import java.io.Serializable;

public class SensingEntry implements Serializable{

	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 9204541991671169264L;
	
	private int moteid_id;
	private int area;
	private String sensing_type;
	private float temperature;
	private float humidity;
	private float light;
	private float co2;
	private int nodetype;
	 public int getMoteId() {
			return moteid_id;
	}
	public void setMoteId(int id) {
		this.moteid_id= id;
	}
	 public int getArea() {
			return area;
	}
	public void setArea(int field) {
		this.area= field;
	}
	public String getSenseType() {
		return sensing_type;
	}
    public void setSenseType(String sensetype) {
		this.sensing_type =sensetype;
	}
    public float getTemp() {
		return temperature;
    }
   public void setTemp(float temp) {
	this.temperature= temp;
   }
   public float getHumi() {
		return humidity;
   }
  public void setHumi(float humi) {
	this.humidity= humi;
  }
  public float getLight() {
		return light;
  }
  public void setLight(float lt) {
	this.light= lt;
  }
  public float getCO2() {
	return co2;
  }
  public void setCO2(float co2) {
    this.co2= co2;
   }
  public int getNodetype() {
		return nodetype;
   }
  public void setNodetype(int ntype) {
	this.nodetype = ntype;
  }
	 
}
