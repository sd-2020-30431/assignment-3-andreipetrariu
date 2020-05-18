package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.dataaccess.IUsersDAO;

@Component
public class UpdateUserHandler implements IHandler<UpdateUserCommand,LoginResponse>{

	@Autowired
	private IUsersDAO userDAO;
	
	@Override
	public LoginResponse handle(UpdateUserCommand q) {
		int result;
		if(q.getType()==1)
			result = userDAO.login(q.getUsername(), q.getPassword());
		else result = userDAO.logout(q.getUsername(), q.getPassword());
		return new LoginResponse(result);
	}

	public void setUserDAO(IUsersDAO userDAO) {
		this.userDAO = userDAO;
	}

}
