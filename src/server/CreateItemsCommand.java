package server;

import java.util.List;

import model.BoughtItem;

public class CreateItemsCommand implements IRequest{

	private List<BoughtItem> items;
	private int id;
	
	public CreateItemsCommand(List<BoughtItem> items, int id) {
		this.items = items;
		this.id = id;
	}

	public List<BoughtItem> getItems() {
		return items;
	}

	public void setItems(List<BoughtItem> items) {
		this.items = items;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

}
