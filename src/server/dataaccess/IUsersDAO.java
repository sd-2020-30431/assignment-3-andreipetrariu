package server.dataaccess;

public interface IUsersDAO {
	public int login(String username, String password);
	public int logout(String username,String password);
}
