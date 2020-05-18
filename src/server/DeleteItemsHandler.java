package server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.BoughtItem;
import server.dataaccess.IItemsDAO;
import server.dataaccess.ItemsDAO;

@Component
public class DeleteItemsHandler implements IHandler<DeleteItemsCommand,ItemsResponse>{

	@Autowired
	private IItemsDAO itemsDAO;
	
	@Override
	public ItemsResponse handle(DeleteItemsCommand q) {
		List<BoughtItem> items = itemsDAO.initWastedItems(q.getId());
		return new ItemsResponse(items);
	}

	public void setItemsDAO(ItemsDAO itemsDAO) {
		this.itemsDAO = itemsDAO;
	}
	
}
