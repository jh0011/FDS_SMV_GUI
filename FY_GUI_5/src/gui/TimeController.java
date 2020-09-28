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
	
	private final int ARRAY_SIZE = 13; //13 parameters [2-14]
	TextField[] allTextFields = new TextField[ARRAY_SIZE];
	
	@FXML TextField endTimeText;
	@FXML TextField beginTimeText;
	@FXML TextField dtText;
	@FXML TextField dtEndText;
	@FXML TextField dtEndMinText;
	@FXML TextField dtSteadyText;
	@FXML TextField dtFlowText;
	@FXML TextField dtRatioText;
	@FXML TextField lockTimeText;
	@FXML TextField restrictTimeText;
	@FXML TextField timeShrinkText;
	@FXML TextField wallIncText;
	@FXML TextField wallIncHt3dText;
	
	@FXML Button timeBackBtn;
	@FXML Button timeNextBtn;
	@FXML Button cancelBtn;
	
	
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		allTextFields[0] = endTimeText;
		allTextFields[1] = beginTimeText;
		allTextFields[2] = dtText;
		allTextFields[3] = dtEndText;
		allTextFields[4] = dtEndMinText;
		allTextFields[5] = dtSteadyText;
		allTextFields[6] = dtFlowText;
		allTextFields[7] = dtRatioText;
		allTextFields[8] = lockTimeText;
		allTextFields[9] = restrictTimeText;
		allTextFields[10] = timeShrinkText;
		allTextFields[11] = wallIncText;
		allTextFields[12] = wallIncHt3dText;
		/*allTextFields[0] = endTimeText;
		allTextFields[0] = endTimeText;*/
		
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
		storeValues();
		
		//check if time is numeric
		boolean checkAllTime = false;
		boolean checkBoolean = true;
		for (int i=0; i<ARRAY_SIZE; i++){
			
			if (!(allTextFields[i].getText().equals(""))){
				if (i==8 || i==9){
					//Check whether the values are boolean
					checkBoolean = checkBoolean(allTextFields[i].getText());
				}
				else{
					checkAllTime = checkTimeValue(allTextFields[i].getText());
				}
			}
			if (!checkAllTime || !checkBoolean){ //if either are false, break
				break;
			}
		}
		
		//check if Required time is filled
		boolean checkEndTime = checkTimeEnd(allTextFields[0].getText());
		
		if (checkEndTime && checkAllTime && checkBoolean){
			//Values.T_END = endTimeText.getText();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Catf.fxml"));
			Parent root = loader.load();
			
			CatfController catfCont = loader.getController(); //Get the next page's controller
			catfCont.showInfo(Values.files); //Set the values of the page
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
		if (timeStart.equals("")){ //Check if time is empty
			Alert timeAlert = new Alert(Alert.AlertType.INFORMATION);
			timeAlert.setTitle("Empty time value");
			timeAlert.setContentText("The End Time value is required.");
			timeAlert.setHeaderText(null);
			timeAlert.show();
			return false;
		}
		else if (Integer.parseInt(timeStart) < 0){ //Check if time is an integer
			Alert timeAlert = new Alert(Alert.AlertType.INFORMATION);
			timeAlert.setTitle("Invalid time value");
			timeAlert.setContentText("The End Time value should be a real value. No negative numbers.");
			timeAlert.setHeaderText(null);
			timeAlert.show();
			return false;
		}
		return true;
	}
	
	private boolean checkTimeValue(String time){ //Check if all time values are numbers
		try{
			float timeValue = Float.valueOf(time);
			if (timeValue >= 0){ //Check if time is a positive float
				return true;
			}
			else{ //Check if time is a negative float
				Alert timeAlert = new Alert(Alert.AlertType.INFORMATION);
				timeAlert.setTitle("Invalid time value");
				timeAlert.setContentText("The time values should not have negative numbers. Please check again.");
				timeAlert.setHeaderText(null);
				timeAlert.show();
				return false;
			}
		}catch(Exception e){ //Check if time is not a number
			Alert timeAlert = new Alert(Alert.AlertType.INFORMATION);
			timeAlert.setTitle("Invalid time value");
			timeAlert.setContentText("The time values should be numerical. Please check again.");
			timeAlert.setHeaderText(null);
			timeAlert.show();
			return false;
		}
	}
	
	private boolean checkBoolean(String value){
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")){
			return true;
		}
		else{
			Alert boolAlert = new Alert(Alert.AlertType.INFORMATION);
			boolAlert.setTitle("Invalid boolean value");
			boolAlert.setContentText("Both 'Lock Time Step' and 'Restrict Time Step' should be logical values of True/False. Please check again.");
			boolAlert.setHeaderText(null);
			boolAlert.show();
			return false;
		}
	}
	
	private void storeValues(){
		for (int i=0; i<ARRAY_SIZE; i++){
			Values.allStrings[i+2][0] = allTextFields[i].getText();
		}
	}
	
	//To take values from Values and display them for the Time page
	protected void showInfo(){ //String endTime, String beginTime, String dt
		endTimeText.setText(Values.allStrings[2][0]);
		beginTimeText.setText(Values.allStrings[3][0]);
		dtText.setText(Values.allStrings[4][0]);
		dtEndText.setText(Values.allStrings[5][0]);
		dtEndMinText.setText(Values.allStrings[6][0]);
		dtSteadyText.setText(Values.allStrings[7][0]);
		dtFlowText.setText(Values.allStrings[8][0]);
		dtRatioText.setText(Values.allStrings[9][0]);
		lockTimeText.setText(Values.allStrings[10][0]);
		restrictTimeText.setText(Values.allStrings[11][0]);
		timeShrinkText.setText(Values.allStrings[12][0]);
		wallIncText.setText(Values.allStrings[13][0]);
		wallIncHt3dText.setText(Values.allStrings[14][0]);
	}


}
