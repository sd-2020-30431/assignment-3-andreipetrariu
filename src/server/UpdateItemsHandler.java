package server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.BoughtItem;
import server.dataaccess.IItemsDAO;

@Component
public class UpdateItemsHandler implements IHandler<UpdateItemsCommand,TrueFalseResponse>{

	@Autowired
	private IItemsDAO itemsDAO;
	
	@Override
	public TrueFalseResponse handle(UpdateItemsCommand q) {
		boolean result = itemsDAO.updateItems(q.getItemNames(),q.getId());
		return new TrueFalseResponse(result);
	}

	public void setItemsDAO(IItemsDAO itemsDAO) {
		this.itemsDAO = itemsDAO;
	}

}
