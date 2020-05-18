package server;

public class TrueFalseResponse implements IResponse{
	private boolean result;
	
	public TrueFalseResponse(boolean result) {
		this.result = result;
	}

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
	
}
