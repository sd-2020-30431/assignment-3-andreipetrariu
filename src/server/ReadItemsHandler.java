package server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.BoughtItem;
import server.dataaccess.IItemsDAO;

@Component
public class ReadItemsHandler implements IHandler<ReadItemsQuery,ItemsResponse>{

	@Autowired
	private IItemsDAO itemsDAO;

	@Override
	public ItemsResponse handle(ReadItemsQuery q) {
		List<BoughtItem> items;
		if(q.getType().compareTo("all")==0)
			items = itemsDAO.readAllItems(q.getId());
		else items = itemsDAO.readUnusedItems(q.getId());
		return new ItemsResponse(items);
	}

	public void setItemsDAO(IItemsDAO itemsDAO) {
		this.itemsDAO = itemsDAO;
	}

}
