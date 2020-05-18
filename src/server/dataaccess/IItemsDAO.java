package server.dataaccess;

import java.util.List;

import model.BoughtItem;

public interface IItemsDAO {
	public boolean createItems(List<BoughtItem> items, int id);
	public List<BoughtItem> readAllItems(int id);
	public List<BoughtItem> readUnusedItems(int id);
	public boolean updateItems(List<String> removedItems, int id);
	public List<BoughtItem> initWastedItems(int id);
}
