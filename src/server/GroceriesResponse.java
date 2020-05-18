package server;

import java.util.List;

import model.ShopItem;

public class GroceriesResponse implements IResponse{
	private List<ShopItem> groceries;

	public GroceriesResponse(List<ShopItem> groceries) {
		this.groceries = groceries;
	}

	public List<ShopItem> getGroceries() {
		return groceries;
	}

	public void setGroceries(List<ShopItem> groceries) {
		this.groceries = groceries;
	}
	
}
