package application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Controller implements Initializable {
	
	private BigDecimal left;
	private String selectedOperator;
	private boolean numberInputting;
	
	@FXML
	private TextField textField;
	
	public Controller(){
		this.left = BigDecimal.ZERO;
		this.selectedOperator = "";
		this.numberInputting = false;
	}

	//button controls 
	@FXML
	public void buttonPressed(ActionEvent evt){
		Button button = (Button)evt.getSource();
		String bText = button.getText();
		
		//clears all
		if(bText.equals("AC")){
			if(bText.equals("AC")){
				left = BigDecimal.ZERO;
			}
			selectedOperator = "";
			numberInputting = true;
			textField.clear();
			return;
		}
		//clears operator
		if(bText.equals("C")){
			selectedOperator = "";
			numberInputting = false;
			return;
		}
		//entering numbers and . 
		if(bText.matches("[0-9\\,.]")){
			if(!numberInputting){
				numberInputting = true;
				textField.clear();
			}
			textField.appendText(bText);
			
			//makes sure there are no invalid integer  
			int totalDots=0;
			for(int i=0; i<textField.getText().toString().length(); i++) {
			   if(textField.getText().toString().charAt(i) == '.') {
				   totalDots++;
			   } 
			   if(totalDots>1) {
				   AlertBox("Invaild Integer", "Cannot Use More Than One Decimal Point");
				   textField.clear();
				   return;
			   }
			}
			return;
		}
		//entering operators 
		if(bText.matches("\\+") || bText.matches("\\*") || bText.matches("\\/") || bText.matches("\\-")){
			//nothing will be done if an integer isn't entered already 
			if(textField.getText().toString().equals("")) {
				return;
			}
			//special case for "." into operators 
			if(textField.getText().equals(".")){
				textField.setText("0");
			}
			left = new BigDecimal(textField.getText());
			selectedOperator = bText;
			numberInputting = false;
			return;
		}
		//when equal is pressed 
		if(bText.equals("=")){
			//special case for "." into "="
			if(textField.getText().equals(".")){
				textField.setText("0");
			}
			
			final BigDecimal right = numberInputting ? new BigDecimal(textField.getText()) : left;
			left = math(selectedOperator,left, right);
			
			//get rid of leading zeros 
			textField.setText(left.stripTrailingZeros().toPlainString());
			
			numberInputting = false;
			
			//clear operator
			selectedOperator = "";
			
			return;
		}
		
		//flips signs 
		if(bText.equals("+/-")){
			left = new BigDecimal(textField.getText());
			if(left ==  BigDecimal.ZERO)
					return;
			left = left.negate();
			textField.setText(left.toString());
			return;
		}
		
	}
	
	//math of the calculator 
	public BigDecimal math(String operator, BigDecimal left, BigDecimal right){
		switch(operator){
			case "+":
				return left.add(right);
			case "-":
				return left.subtract(right);
			case "*":
				return left.multiply(right);
			case "/":
				if (right.intValueExact() == 0){
					AlertBox("ERROR", "You Cannot Divide By Zero");
					return BigDecimal.ZERO;
				}
				return left.divide(right, 8, RoundingMode.HALF_UP);
				
		}
		return right;
	}
	
	//An alert box for when you try to divide by zero 
	private void AlertBox(String title, String message) {
		Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	
}
