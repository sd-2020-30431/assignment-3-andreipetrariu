package server.dataaccess;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.BoughtItem;

@Repository
public class ItemsDAO implements IItemsDAO{

	@Autowired
	private SessionFactory sessionFactory;
	private Session session;
	
	@Transactional
	public boolean createItems(List<BoughtItem> items, int id) {
		try {
			org.springframework.orm.hibernate5.LocalSessionFactoryBean sf;
			session = sessionFactory.getCurrentSession();
			for(BoughtItem item : items) {
				item.setIdUser(id);
				session.save(item);
			}
		}
		catch(Exception e) {
			Alert failedLogin = new Alert(AlertType.ERROR);
			failedLogin.setHeaderText("Create Item failed!");
			failedLogin.setContentText(e.toString());
			failedLogin.showAndWait();
			return false;
		}
		return true;
	}
	
	@Transactional
	public List<BoughtItem> readAllItems(int id) {
		List<BoughtItem> items = null;
		try {
			session = sessionFactory.getCurrentSession();
			items = session.createQuery("from BoughtItem w where w.idUser = :id").setParameter("id",id).list();
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
		return items;
	}

	@Transactional
	public List<BoughtItem> readUnusedItems(int id) {
		List<BoughtItem> items = null;
		try {
			session = sessionFactory.getCurrentSession();
			items = session.createQuery("from BoughtItem w where w.idUser = :id and w.consumptionDate = null").setParameter("id",id).list();
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
		return items;
	}
	
	@Transactional
	public boolean updateItems(List<String> removedItems,int id) {
		try {
			session = sessionFactory.getCurrentSession();
			for(String itemName : removedItems) {
				Query query = session.createQuery("update BoughtItem w set w.consumptionDate = :today where w.name = :name and w.idUser = :id").setParameter("today",new Date()).setParameter("name", itemName).setParameter("id", id);
				query.executeUpdate();
			}
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	
	//called at the beginning of the application
	//checks the item database and moves items that have been wasted to the report
	@Transactional
	public List<BoughtItem> initWastedItems(int id) {
		List<BoughtItem> wastedItems = null;
		Date today = new Date();
		session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from BoughtItem w where w.expirationDate < :date and w.idUser = :id").setParameter("date",today).setParameter("id",id);
		wastedItems = query.list();
		for(BoughtItem i : wastedItems) 
			session.createQuery("delete BoughtItem w where w.name = :name").setParameter("name", i.getName()).executeUpdate();
		return wastedItems;
	}

}
