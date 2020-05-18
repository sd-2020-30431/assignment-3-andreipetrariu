package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.dataaccess.IItemsDAO;
import server.dataaccess.ItemsDAO;

@Component
public class CreateItemsHandler implements IHandler<CreateItemsCommand,TrueFalseResponse>{

	@Autowired
	private IItemsDAO itemsDAO;
	
	@Override
	public TrueFalseResponse handle(CreateItemsCommand q) {
		boolean result = itemsDAO.createItems(q.getItems(), q.getId());
		return new TrueFalseResponse(result);
	}

	public void setItemsDAO(ItemsDAO itemsDAO) {
		this.itemsDAO = itemsDAO;
	}
}
