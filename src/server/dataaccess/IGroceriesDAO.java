package server.dataaccess;

import java.util.List;

import model.ShopItem;

public interface IGroceriesDAO {
	public List<ShopItem> readAllGroceries();
}
