package server;

import java.util.List;

public class UpdateItemsCommand implements IRequest{
	
	private List<String> itemNames;
	private int id;
	
	public UpdateItemsCommand(List<String> itemNames, int id) {
		this.itemNames = itemNames;
		this.id = id;
	}

	public List<String> getItemNames() {
		return itemNames;
	}

	public void setItemNames(List<String> itemNames) {
		this.itemNames = itemNames;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
