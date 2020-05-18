package server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.ShopItem;
import server.dataaccess.GroceriesDAO;
import server.dataaccess.IGroceriesDAO;

@Component
public class ReadGroceriesHandler implements IHandler<ReadGroceriesQuery,GroceriesResponse>{

	@Autowired
	private IGroceriesDAO groceriesDAO;
	
	@Override
	public GroceriesResponse handle(ReadGroceriesQuery q) {
		List<ShopItem> groceries = groceriesDAO.readAllGroceries();
		return new GroceriesResponse(groceries);
	}
	
	public void setGroceriesDAO(GroceriesDAO groceriesDAO) {
		this.groceriesDAO = groceriesDAO;
	}

}
