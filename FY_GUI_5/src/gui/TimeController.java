package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class TimeController implements Initializable{
	
	private final int ARRAY_SIZE = 3; //3 parameters [2-4]
	TextField[] allTextFields = new TextField[ARRAY_SIZE];
	
	@FXML TextField endTimeText;
	@FXML TextField beginTimeText;
	@FXML TextField dtText;
	
	@FXML Button timeBackBtn;
	@FXML Button timeNextBtn;
	@FXML Button cancelBtn;
	
	
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		allTextFields[0] = endTimeText;
		allTextFields[1] = beginTimeText;
		allTextFields[2] = dtText;
		
	}
	
	@FXML
	private void goToBasic(ActionEvent event) throws IOException{ //PREVIOUS SCENE
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Basic.fxml"));
		Parent root = loader.load();
		BasicController newBasic = loader.getController(); //Get the previous page's controller
		
		newBasic.showInfo(); //Set the values of the page //Values.chid, Values.title
		Scene basicScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(basicScene);
		mainWindow.show();
		
	}
	
	@FXML
	private void goToCatf(ActionEvent event) throws IOException{ //NEXT SCENE
		
		//check if time is numeric
		boolean checkFloat = true;
		
		//check if the required time field is filled
		boolean checkEndTime = checkTimeEnd(allTextFields[0].getText());
		
		//store values
		storeValues();
		
		//check the type values and store them
		for (int i=0; i<ARRAY_SIZE; i++){
			if (!(allTextFields[i].getText().equals(""))){
				checkFloat = checkTimeFloat(allTextFields[i].getText(), i);
			}
			if (!checkFloat){ //if either are false, break
				break;
			}
		}
		
		if (checkEndTime && checkFloat){
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Catf.fxml"));
			Parent root = loader.load();
			
			CatfController catfCont = loader.getController(); //Get the next page's controller
			catfCont.showInfo(); //Set the values of the page
			Scene catfScene = new Scene(root);
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			mainWindow.setScene(catfScene);
			mainWindow.show(); 
		}
		else{
			System.out.println("Unable to proceed to the next page");
		}
	}
	
	@FXML
	private void cancelOption(ActionEvent event) throws IOException{ //CANCEL
		if (Values.cancelWarning()){
			Values.cancelForm();
			Parent introLayout = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Get the next layout
			Scene introScene = new Scene(introLayout, 870, 710); //Pass the layout to the next scene
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
			
			
			mainWindow.setScene(introScene);
			mainWindow.show();
		}
	}
	
	private boolean checkTimeEnd(String timeStart){
		try{
			if (timeStart.equals("")){ //Check if time is empty
				Alert timeAlert = new Alert(Alert.AlertType.INFORMATION);
				timeAlert.setTitle("Empty time value");
				timeAlert.setContentText("The End Time value is required.");
				timeAlert.setHeaderText(null);
				timeAlert.show();
				return false;
			}
			else if (Float.valueOf(timeStart) < 0){ //Check if time is an integer
				Alert timeAlert = new Alert(Alert.AlertType.INFORMATION);
				timeAlert.setTitle("Invalid time value");
				timeAlert.setContentText("The End Time value should be a real value. No negative numbers.");
				timeAlert.setHeaderText(null);
				timeAlert.show();
				return false;
			}
		}catch (Exception e){
				Alert timeAlert = new Alert(Alert.AlertType.INFORMATION);
				timeAlert.setTitle("Invalid time value");
				timeAlert.setContentText("The time values should be numerical. Please check again.");
				timeAlert.setHeaderText(null);
				timeAlert.show();
				return false;
			}
		return true;
	}
	
	private boolean checkTimeFloat(String value, int i){
		try{
			float timeFloat = Float.valueOf(value);
			if (timeFloat < 0){ //check negative float values
				Alert timeAlert = new Alert(Alert.AlertType.INFORMATION);
				timeAlert.setTitle("Invalid time value");
				timeAlert.setContentText("The time values should not have negative numbers. Please check again.");
				timeAlert.setHeaderText(null);
				timeAlert.show();
				return false;
			}
			else{
				Values.allStrings[i+2][0] = Float.toString(timeFloat);
				return true;
			}
		}catch(Exception e){
			Alert timeAlert = new Alert(Alert.AlertType.INFORMATION);
			timeAlert.setTitle("Invalid time value");
			timeAlert.setContentText("The time values should be numerical. Please check again.");
			timeAlert.setHeaderText(null);
			timeAlert.show();
			return false;
		}
	}
	
	protected void storeValues(){
		Values.allStrings[2][0] = endTimeText.getText();
		Values.allStrings[3][0] = beginTimeText.getText();
		Values.allStrings[4][0] = dtText.getText();
	}
	
	//To take values from Values and display them for the Time page
	protected void showInfo(){ 
		endTimeText.setText(Values.allStrings[2][0]);
		beginTimeText.setText(Values.allStrings[3][0]);
		dtText.setText(Values.allStrings[4][0]);
	}


}
