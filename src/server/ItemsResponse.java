package server;

import java.util.List;

import model.BoughtItem;

public class ItemsResponse implements IResponse{
	private List<BoughtItem> items;

	public ItemsResponse(List<BoughtItem> items) {
		this.items = items;
	}

	public List<BoughtItem> getItems() {
		return items;
	}

	public void setItems(List<BoughtItem> items) {
		this.items = items;
	}
}
