package server;

public class ReadItemsQuery implements IRequest{

	private int id;
	private String type;
	
	public ReadItemsQuery(String type,int id) {
		this.id=id;
		this.type=type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}

}
