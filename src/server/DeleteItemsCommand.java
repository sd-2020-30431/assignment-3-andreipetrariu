package server;

public class DeleteItemsCommand implements IRequest {

	private int id;
	
	public DeleteItemsCommand(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
