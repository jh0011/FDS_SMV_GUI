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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class TimeController implements Initializable{
	/**
	 * Controller class for Time.fxml
	 * @author 
	 */
	
	//time
	@FXML TextField endTimeText;
	@FXML TextField beginTimeText;
	@FXML TextField dtText;
	
	//catf
	@FXML TextArea filesText;
	
	@FXML Button timeBackBtn;
	@FXML Button timeNextBtn;
	@FXML Button cancelBtn;
	
	boolean checkFloat;
	boolean checkEndTime;
	boolean isCorrectFormat;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * When the Cancel button is clicked to cancel creation of .fds file
	 * @param event Cancel button is clicked
	 * @throws SQLException
	 * @throws IOException
	 */
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
	
	/**
	 * Go to the previous page (BASIC) + input validation
	 * @param event Back button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void goToBasic(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		doChecking();
		
		try {
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
		}catch(Exception e) {
			Values.showError();
		}
		
	}
	
	/**
	 * Go to the next page (INIT) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void goToInit(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		doChecking();
		
		try {
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
		}catch(Exception e) {
			Values.showError();
		}
	}
	
	/**
	 * Description of TIME namelist
	 * @param event Open the description label
	 */
	@FXML
    public void openTimeDesc(MouseEvent event) {
		String content = "The TIME namelist defines the time duration of the simulation and the initial time "
				+ "step used to advance the solution of the discretized equations. \n\nEnd Time: A required value. It states the duration in"
				+ " seconds that the simulation will run for. \n\nStart Time: An optional value. The default value is 0. Please specify if otherwise."
				+ "\n\nDT: The initial time step size.";
		String namelist = "TIME";
		Values.openDesc(namelist, content);
    }
	
	/**
	 * Description of CATF namelist
	 * @param event Open the description label
	 */
	@FXML
    public void openCatfDesc(MouseEvent event) {
		String content = "The CATF namelist allows for the inclusion of input information from different files into a simulation. \n\n"
				+ "Other files: Write the file names, delimited by a new line.";
		String namelist = "CATF";
		Values.openDesc(namelist, content);
    }
	
	/**
	 * Call the checking methods for the different namelists
	 */
	public void doChecking() {
		doCheckingTime();
		doCheckingCatf();
	}
	
	/**
	 * Check the input fields for TIME
	 */
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
	
	/**
	 * Check the input fields for CATF
	 */
	public void doCheckingCatf() {
		isCorrectFormat = true;
		
		if (!filesText.getText().equals("")){
			isCorrectFormat = formatText(filesText.getText());
		}
	}
	
	/**
	 * End Time input validation: <br>
	 * - If it is empty
	 * @param timeStart The Start Time input from user
	 * @return Boolean on whether the check was successful
	 */
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
	
	/**
	 * Check if the float is positive
	 * @param tempField TextField for the time values
	 * @returnBoolean on whether the check was successful
	 */
	public boolean checkTimeFloat(TextField tempField){
		String param = "The time values";
    	return Values.checkPosFloatValues(param, tempField);
		
	}
	
	/**
	 * Check if the Files is formatted correctly
	 * @param text Input value from user
	 * @return Boolean on whether the check was successful
	 */
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
	
	/**
	 * Store the values into the database after input validation
	 * @throws SQLException
	 */
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
	
	/**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException
	 */
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
