package server.dataaccess;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import model.User;

@Repository
public class UsersDAO implements IUsersDAO{
	
	@Autowired
	private SessionFactory sessionFactory;
	private Session session;
	
	@Transactional
	public int login(String username, String password) {
		session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("select id_users from users where username = :username and password = :password and status=0")
				    .setParameter("username", username)
				    .setParameter("password", password);
		if(query.list().isEmpty()) {
			return 0;
		}
		int id = (int) query.getResultList().get(0);
		session = sessionFactory.getCurrentSession();
		session.get(User.class,id).setStatus(1);
		query = session.createSQLQuery(
			    "update users set status = 0" + " where username = :username ");
		query.setParameter("username", username);
		query.executeUpdate();
		return id;
}
	@Transactional
	public int logout(String username,String password){
		session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(
			    "update users set status = 0" + " where username = :username");
		query.setParameter("username", username);
		query.executeUpdate();
		return 0;
}
}
