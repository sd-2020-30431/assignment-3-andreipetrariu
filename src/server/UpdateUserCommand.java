package server;

public class UpdateUserCommand implements IRequest{
	private String username;
	private String password;
	private int type;
	public UpdateUserCommand(String username, String password, int type) {
		super();
		this.username = username;
		this.password = password;
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public int getType() {
		return type;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
