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
	
	//time
	@FXML TextField endTimeText;
	@FXML TextField beginTimeText;
	@FXML TextField dtText;
	
	//catf
	@FXML TextArea filesText;
	
	@FXML Button timeBackBtn;
	@FXML Button timeNextBtn;
	@FXML Button cancelBtn;
	
	boolean checkFloat;;
	boolean checkEndTime;
	boolean isCorrectFormat;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	public void goToBasic(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		doChecking();
		
		if (checkEndTime && checkFloat && isCorrectFormat){
			//store the values
			storeValues();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Basic.fxml"));
			Parent root = loader.load();
			BasicController newBasic = loader.getController(); //Get the previous page's controller
			
			newBasic.showInfo(); //Set the values of the page 
			Scene basicScene = new Scene(root);
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			mainWindow.setScene(basicScene);
			mainWindow.show();
		}
		else {
			System.out.println("Unable to go back to Basic page");
		}
		
	}
	
	@FXML
	public void goToInit(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		
		doChecking();
		
		if (checkEndTime && checkFloat && isCorrectFormat){
			//store the values
			storeValues();
			
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
			System.out.println("Unable to proceed to INIT page");
		}
	}
	
	@FXML
	public void cancelOption(ActionEvent event) throws IOException, SQLException{ //CANCEL
		if (Values.cancelWarning()){
			Values.cancelForm();
			Parent introLayout = FXMLLoader.load(getClass().getResource("Intro.fxml")); //Get the next layout
			Scene introScene = new Scene(introLayout, 870, 710); //Pass the layout to the next scene
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow(); //Get the parent window
			
			
			mainWindow.setScene(introScene);
			mainWindow.show();
		}
	}
	
	public void doChecking() {
		doCheckingTime();
		doCheckingCatf();
	}
	
	public void doCheckingTime() {
		checkFloat = true;
		checkEndTime = true;
		
		checkEndTime = checkTimeEnd(endTimeText.getText());
		if (!(endTimeText.getText().equals(""))){
			checkFloat = checkFloat && checkTimeFloat(endTimeText);
		}
		if (!(beginTimeText.getText().equals(""))){
			checkFloat = checkFloat && checkTimeFloat(beginTimeText);
		}
		if (!(dtText.getText().equals(""))){
			checkFloat = checkFloat && checkTimeFloat(dtText);
		}
	}
	
	public void doCheckingCatf() {
		isCorrectFormat = true;
		
		if (!filesText.getText().equals("")){
			isCorrectFormat = formatText(filesText.getText());
		}
	}
	
	public boolean checkTimeEnd(String timeStart){
		if (timeStart.equals("")){ //Check if end time is empty
			Alert timeAlert = new Alert(Alert.AlertType.INFORMATION);
			timeAlert.setTitle("Empty time value");
			timeAlert.setContentText("The End Time value is required.");
			timeAlert.setHeaderText(null);
			timeAlert.show();
			return false;
		}
		endTimeText.setText(timeStart);
		return true;
	}
	
	public boolean checkTimeFloat(TextField tempField){
		try{
			String value = tempField.getText();
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
				tempField.setText(Float.toString(timeFloat));
				return true;
			}
		}catch(Exception e){ //check if values are numerical
			Alert timeAlert = new Alert(Alert.AlertType.INFORMATION);
			timeAlert.setTitle("Invalid time value");
			timeAlert.setContentText("The time values should be numerical. Please check again.");
			timeAlert.setHeaderText(null);
			timeAlert.show();
			return false;
		}
	}
	
	public boolean formatText(String text){
		if (text.contains(" ") || text.contains(",") || text.contains(";") || countNumChar(text, '.') > 1){ //check for file invalid delimiter
			Alert filesAlert = new Alert(Alert.AlertType.INFORMATION);
			filesAlert.setTitle("Files title format");
			filesAlert.setContentText("The files have invalid delimiters. Files should be delimited by a new line.");
			filesAlert.setHeaderText(null);
			filesAlert.show();
			return false;
		}
		
		String[] files = text.split("\\n");
		Values.concatFiles = "";
		for (int i=0; i<files.length; i++){
			if (!files[i].contains(".")){ //check for file extension
				Alert filesAlert = new Alert(Alert.AlertType.INFORMATION);
				filesAlert.setTitle("Files title format");
				filesAlert.setContentText("The files require a file extension.");
				filesAlert.setHeaderText(null);
				filesAlert.show();
				return false;
			}
			
			if (files[i].contains("|") || files[i].contains("/") || files[i].contains("\\") || files[i].contains(":") ||
					files[i].contains("?") || files[i].contains("*") || files[i].contains("<") || files[i].contains(">") 
					|| files[i].contains("\"")){ //check for file invalid symbols
				Alert filesAlert = new Alert(Alert.AlertType.INFORMATION);
				filesAlert.setTitle("Files title format");
				filesAlert.setContentText("The files have invalid symbols");
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
	
	public int countNumChar(String value, char charValue) {
		int count = 0;
		for (int i=0; i<value.length(); i++) {
			if (value.charAt(i) == charValue) {
				count++;
			}
		}
		return count;
	}
	
	
	public void storeValues() throws SQLException{ //store values into the database
		storeValuesTime();
		storeValuesCatf();
	}
	
	public void storeValuesTime() throws SQLException{ //store TIME values into the database
		String sqlTime = "INSERT INTO time (EndTime, StartTime, DT) VALUES ('" + endTimeText.getText() + "', '" + 
				beginTimeText.getText() + "', '" + dtText.getText() + "');";
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlTime);
	}
	
	public void storeValuesCatf() throws SQLException{ //store CATF values into the database
		String sqlCatf = "INSERT INTO catf VALUES ('" + filesText.getText() + "');";
		
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlCatf);
	}
	
	public void showInfo() throws SQLException{ //to show the info when the page is loaded
		showInfoTime();
		showInfoCatf();
	}
	
	public void showInfoTime() throws SQLException{ //to show the info when the page is loaded
		String sqlTime = "SELECT * FROM time;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlTime);
		while (rs.next()){
			endTimeText.setText(rs.getString(1));
			beginTimeText.setText(rs.getString(2));
			dtText.setText(rs.getString(3));
		}
	}
	
	public void showInfoCatf() throws SQLException{ //to show the info when the page is loaded
		String sqlCatf = "SELECT * FROM catf;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs2 = statement.executeQuery(sqlCatf);
		while (rs2.next()){
			filesText.setText(rs2.getString(1));
		}
	}
}
