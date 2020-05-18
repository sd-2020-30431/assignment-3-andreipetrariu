package client;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class RateLabelDecorator{
	private Label theLabel;
	private int idealRate;
	private int userRate;
	
	public RateLabelDecorator(Label label) {
		theLabel = label;
		idealRate=0;
		userRate=0;
	}
	
	public void setIdealRate(int idealRate) {
		this.idealRate = idealRate;
	}
	
	public void setUserRate(int userRate) {
		this.userRate = userRate;
		theLabel.setText(theLabel.getText().split(":")[0] +": "+ userRate);
		if(userRate>idealRate)
			theLabel.setTextFill(Color.web("11EE11"));
		else theLabel.setTextFill(Color.web("FF1111"));
	}
	

	public Label getLabel() {
		if(userRate>idealRate) 
			theLabel.setTextFill(Color.web("11EE11"));
		else theLabel.setTextFill(Color.web("FF1111"));
		return theLabel;
	}
}
