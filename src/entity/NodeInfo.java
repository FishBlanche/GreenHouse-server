package entity;
import java.io.Serializable;

public class NodeInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8087623959232235060L;
	
	private int id;
	private int mote_id;
	private int cluster_id;
    private String node_type;
    public int getId() {
		return this.id;
	}
    public void setId(int Id) {
		this.id = Id;
	}
	 public int getMoteId() {
			return this.mote_id;
	}
	public void setMoteId(int Id) {
		this.mote_id = Id;
	}
	 public int getClusterId() {
			return this.cluster_id;
	}
	public void setClusterId(int Id) {
		this.cluster_id = Id;
	}
	public String getNodeType() {
		return this.node_type;
	}
    public void setNodeType(String nodeType) {
		this.node_type = nodeType;
	}
}
