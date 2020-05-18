package server.dataaccess;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import model.ShopItem;

@Repository
public class GroceriesDAO implements IGroceriesDAO {

	@Autowired
	private SessionFactory sessionFactory;
	private Session session;
	
	
	@Transactional
	public List<ShopItem> readAllGroceries() {
		List<ShopItem> groceries = null;
		session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from ShopItem");
		groceries = query.list();

		return groceries;
	}
	
}
