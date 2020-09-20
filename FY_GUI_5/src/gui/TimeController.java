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
	
	private final int ARRAY_SIZE = 3;
	
	@FXML TextField endTimeText;
	@FXML TextField beginTimeText;
	@FXML TextField dtText;
	@FXML Button timeBackBtn;
	@FXML Button timeNextBtn;
	@FXML Button cancelBtn;
	
	TextField[] allTextFields = new TextField[ARRAY_SIZE];
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		allTextFields[0] = endTimeText;
		allTextFields[1] = beginTimeText;
		allTextFields[2] = dtText;
		/*allTextFields[0] = endTimeText;
		allTextFields[0] = endTimeText;
		allTextFields[0] = endTimeText;
		allTextFields[0] = endTimeText;*/
		
	}
	
	@FXML
	private void goToBasic(ActionEvent event) throws IOException{ //PREVIOUS SCENE
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Basic.fxml"));
		Parent root = loader.load();
		BasicController newBasic = loader.getController(); //Get the previous page's controller
		
		newBasic.showInfo(Values.chid, Values.title); //Set the values of the page
		Scene basicScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(basicScene);
		mainWindow.show();
		
	}
	
	@FXML
	private void goToCatf(ActionEvent event) throws IOException{ //NEXT SCENE
		
		Values.DT = dtText.getText();
		Values.T_BEGIN = beginTimeText.getText();
		//check the endTimeVal
		boolean checkAllTime = false;
		for (int i=0; i<ARRAY_SIZE; i++){
			if (!allTextFields[i].getText().equals("")){
				checkAllTime = checkTimeValue(allTextFields[i].getText());
				if (!checkAllTime){
					break;
				}
			}
		}
		boolean checkEndTime = checkTimeEnd(allTextFields[0].getText());
		
		if (checkEndTime && checkAllTime){
			Values.T_END = endTimeText.getText();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Catf.fxml"));
			Parent root = loader.load();
			
			CatfController catfCont = loader.getController(); //Get the next page's controller
			catfCont.showInfo(Values.files); //Set the values of the page
			Scene catfScene = new Scene(root);
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			mainWindow.setScene(catfScene);
			mainWindow.show(); 
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
			System.out.println("timeValue: "+timeValue);
			if (timeValue > 0){ //Check if time is a positive float
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
	
	
	protected void showInfo(String endTime, String beginTime, String dt){
		endTimeText.setText(endTime);
		beginTimeText.setText(beginTime);
		dtText.setText(dt);
	}
	

}
