package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class TimeController implements Initializable{
	
	private final int ARRAY_SIZE = 3; //3 parameters [2-4]
	TextField[] allTextFields = new TextField[ARRAY_SIZE];
	
	//time
	@FXML TextField endTimeText;
	@FXML TextField beginTimeText;
	@FXML TextField dtText;
	
	//catf
	@FXML TextArea filesText;
	
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
	private void goToBasic(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
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
	private void goToCatf(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		
		//check if time is numeric
		boolean checkFloat = true;
		
		//check if the required time field is filled
		boolean checkEndTime = checkTimeEnd(allTextFields[0].getText());
		
		//check if catf file format is correct
		boolean isCorrectFormat = true;
		
		
		//check the type values and store them
		//TIME
		for (int i=0; i<ARRAY_SIZE; i++){
			if (!(allTextFields[i].getText().equals(""))){
				checkFloat = checkTimeFloat(allTextFields[i].getText(), i);
			}
			if (!checkFloat){ //if either are false, break
				break;
			}
		}
		
		//CATF
		if (!filesText.getText().equals("")){
			isCorrectFormat = formatText(filesText.getText());
		}
		
		if (checkEndTime && checkFloat && isCorrectFormat){
			//store values
			storeValues();
			System.out.println("VALUES CATF: " + Values.concatFiles);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Init.fxml"));
			Parent root = loader.load();
			
			InitController initCont = loader.getController(); //Get the next page's controller
			initCont.showInfo(); //Set the values of the page
			Scene initScene = new Scene(root);
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			mainWindow.setScene(initScene);
			mainWindow.show(); 
		}
		else{
			System.out.println("Unable to proceed to the next page");
		}
	}
	
	@FXML
	private void cancelOption(ActionEvent event) throws IOException, SQLException{ //CANCEL
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
		allTextFields[0].setText(timeStart);
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
				//Values.allStrings[i+2][0] = Float.toString(timeFloat);
				allTextFields[i].setText(Float.toString(timeFloat));
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
	
	private boolean formatText(String text){
		System.out.println("TEXT: " + text);
		String[] files = text.split("\\n");
		Values.concatFiles = "";
		for (int i=0; i<files.length; i++){
			//check for file extension
			if (!files[i].contains(".")){
				Alert filesAlert = new Alert(Alert.AlertType.INFORMATION);
				filesAlert.setTitle("Files title format");
				filesAlert.setContentText("The files require a file extension.");
				filesAlert.setHeaderText(null);
				filesAlert.show();
				return false;
			}
			
			if (i <= files.length - 2){
				Values.concatFiles = Values.concatFiles + "'" + files[i] + "', ";
			}
			else{
				Values.concatFiles = Values.concatFiles + "'" + files[i] + "'";
			}
					
			
			///////////////////DO NOT DELETE///////////////////////////////////
//			if (files.length == 1){
//				concatFiles = concatFiles + files[i];
//			}
//			else if (i == 0){
//				concatFiles = concatFiles + files[i] + "', ";
//			}
//			else if (i <= files.length - 2){
//				concatFiles = concatFiles + "'" + files[i] + "', ";
//			}
//			else{
//				concatFiles = concatFiles + "'" + files[i];
//			}
			//////////////////////////////////////////////////////////////////////
		}
		filesText.setText(text);
		return true;
	}
	
	protected void storeValues() throws SQLException{
		String sqlTime = "INSERT INTO time (EndTime, StartTime, DT) VALUES ('" + allTextFields[0].getText() + "', '" + 
		allTextFields[1].getText() + "', '" + allTextFields[2].getText() + "');";
		String sqlCatf = "INSERT INTO catf VALUES ('" + filesText.getText() + "');";
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlTime);
		statement.executeUpdate(sqlCatf);
	}
	
	//To take values from database and display them for the Time page
	protected void showInfo() throws SQLException{ 
		String sqlTime = "SELECT * FROM time;";
		String sqlCatf = "SELECT * FROM catf;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlTime);
		while (rs.next()){
			endTimeText.setText(rs.getString(2));
			beginTimeText.setText(rs.getString(3));
			dtText.setText(rs.getString(4));
		}
		ResultSet rs2 = statement.executeQuery(sqlCatf);
		while (rs2.next()){
			filesText.setText(rs2.getString(1));
		}
	}


}
