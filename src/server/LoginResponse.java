package server;

public class LoginResponse implements IResponse{
	private int id;

	public LoginResponse(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
