package gui;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PartController {
	@FXML TextField surfIdText; //string
	@FXML TextField specIdText; //string
	@FXML TextField propIdText; //string
	@FXML TextField qtyPartText; //string
	@FXML TextField staticText; //boolean
	@FXML TextField masslessText; //boolean
	@FXML TextField sampleText; //integer
	@FXML TextField diameterText; //float
	@FXML TextField idText; //string
	@FXML TextField qtyBndfText; //string
	
	boolean booleanCheck;
	boolean intCheck;
	boolean floatCheck;
	
	static int mainPartId = 1;
	static int mainBndfId = 1;
	
	
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
	
	@FXML
	private void goToInit(ActionEvent event) throws IOException, SQLException{ //PREVIOUS SCENE
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Init.fxml"));
		Parent root = loader.load();
		
		InitController initCont = loader.getController(); //Get the next page's controller
		initCont.showInfo(); //Set the values of the page 
		Scene initScene = new Scene(root);
		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
		mainWindow.setScene(initScene);
		mainWindow.show();
	}
	
	@FXML
	private void goToTemp(ActionEvent event) throws IOException, SQLException{ //NEXT SCENE
		doChecking();
		
		if (booleanCheck && intCheck && floatCheck){
			//store the values
			storeValues();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("temp.fxml"));
			Parent root = loader.load();
			
			tempController tempCont = loader.getController(); //Get the next page's controller
			//initCont.showInfo(); //Set the values of the page 
			Scene tempScene = new Scene(root);
			Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
			mainWindow.setScene(tempScene);
			mainWindow.show();
		}
		else{
			System.out.println("Unable to load the temp page");
		}
		
	}
	
	@FXML
	private void newPartLine(ActionEvent event) throws IOException, SQLException{
		mainPartId++;
		String mainPartIdString = Integer.toString(mainPartId);
		String sqlPart = "INSERT INTO part (mainID, SURF_ID, SPEC_ID, PROP_ID, QUANTITIES, STATIC" + 
				", MASSLESS, SAMPLING_FACTOR, DIAMETER, ID) VALUES ('" + mainPartIdString + "', '', '', '', '', '', '', '', '', '');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement = connection.createStatement();
		statement.executeUpdate(sqlPart);
		
		showInfo();
		
//		String sqlShowPart = "SELECT * FROM init";
//		ResultSet rs = statement.executeQuery(sqlShowPart);
//		while (rs.next()){
//			surfIdText.setText(rs.getString(2));
//			specIdText.setText(rs.getString(3));
//			propIdText.setText(rs.getString(4));
//			qtyPartText.setText(rs.getString(5));
//			staticText.setText(rs.getString(6));
//			masslessText.setText(rs.getString(7));
//			sampleText.setText(rs.getString(8));
//			diameterText.setText(rs.getString(9));
//			idText.setText(rs.getString(10));
//		}
	}
	
	@FXML
	private void newBndfLine(ActionEvent event) throws IOException, SQLException{
		mainBndfId++;
		String mainBndfIdString = Integer.toString(mainBndfId);
		String sqlBndf = "INSERT INTO bndf (mainID, QUANTITY) VALUES ('" + mainBndfIdString + "', '');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement = connection.createStatement();
		statement.executeUpdate(sqlBndf);
		showInfo();
	}
	
	private void doChecking(){
		booleanCheck = true;
		intCheck = true;
		floatCheck = true;
		
		//check the boolean values
		if (!staticText.getText().equals("")){
			booleanCheck = booleanCheck && checkBooleanValues(staticText);
		}
		if (!masslessText.getText().equals("")){
			booleanCheck = booleanCheck && checkBooleanValues(masslessText);
		}
		
		//check the integer value
		if (!sampleText.getText().equals("")){
			intCheck = intCheck && checkIntegerValues(sampleText);
		}
		
		//check the float value
		if (!diameterText.getText().equals("")){
			floatCheck = floatCheck && checkFloatValues(diameterText);
		}
	}
	
	private boolean checkBooleanValues(TextField tempField){
		if (tempField.getText().equalsIgnoreCase("true")){
			tempField.setText("TRUE");
			return true;
		}
		else if (tempField.getText().equalsIgnoreCase("false")){
			tempField.setText("FALSE");
			return true;
		}
		else{
			Alert partAlert = new Alert(Alert.AlertType.INFORMATION);
			partAlert.setTitle("Invalid logical value");
			partAlert.setContentText("Static and Massless should be logical values. Please check again.");
			partAlert.setHeaderText(null);
			partAlert.show();
			return false;
		}
	}
	
	private boolean checkIntegerValues(TextField tempField){
		try{ 
			String stringVal = tempField.getText();
			int intVal = Integer.parseInt(stringVal);
			if (intVal <= 0){ //if it is not a positive integer
				Alert partAlert = new Alert(Alert.AlertType.INFORMATION);
				partAlert.setTitle("Invalid integer value");
				partAlert.setContentText("Sampling factor should be a positive integer. Please check again.");
				partAlert.setHeaderText(null);
				partAlert.show();
				return false;
			}
			tempField.setText(stringVal);
			return true;
		}
		catch(Exception e){ //if it is not integer
			Alert partAlert = new Alert(Alert.AlertType.INFORMATION);
			partAlert.setTitle("Invalid integer value");
			partAlert.setContentText("Sampling factor should be an integer. Please check again.");
			partAlert.setHeaderText(null);
			partAlert.show();
			return false;
		}
	}
	
	private boolean checkFloatValues(TextField tempField){
		try{
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive value
				Alert partAlert = new Alert(Alert.AlertType.INFORMATION);
				partAlert.setTitle("Invalid value");
				partAlert.setContentText("Diameter should be a positive value. Please check again.");
				partAlert.setHeaderText(null);
				partAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch(Exception e){ //if it is not a float
			Alert partAlert = new Alert(Alert.AlertType.INFORMATION);
			partAlert.setTitle("Invalid value");
			partAlert.setContentText("Diameter should be a numerical value. Please check again.");
			partAlert.setHeaderText(null);
			partAlert.show();
			return false;
		}
		
	}
	
	private void storeValues() throws SQLException{
		String mainPartIdString = Integer.toString(mainPartId);
		String mainBndfIdString = Integer.toString(mainBndfId);
		String sqlPart = "INSERT INTO part VALUES('" + mainPartIdString + "', '" + surfIdText.getText() + "', '" 
				+ specIdText.getText() + "', '" + propIdText.getText() + "', '" + qtyPartText.getText() + "', '"
				+ staticText.getText() + "', '" + masslessText.getText() + "', '" + sampleText.getText() +
				"', '" + diameterText.getText() + "', '" + idText.getText() + "');";
		String sqlBndf = "INSERT INTO bndf VALUES('" + mainBndfIdString + "', '" + qtyBndfText.getText() + "');";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlPart);
		statement.executeUpdate(sqlBndf);
	}
	
	public void showInfo() throws SQLException{
		String sqlPart = "SELECT * FROM part;";
		String sqlBndf = "SELECT * FROM bndf;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlPart);
		while (rs.next()){
			surfIdText.setText(rs.getString(2));
			specIdText.setText(rs.getString(3));
			propIdText.setText(rs.getString(4));
			qtyPartText.setText(rs.getString(5));
			staticText.setText(rs.getString(6));
			masslessText.setText(rs.getString(7));
			sampleText.setText(rs.getString(8));
			diameterText.setText(rs.getString(9));
			idText.setText(rs.getString(10));
		}
		
		ResultSet rs2 = statement.executeQuery(sqlBndf);
		while (rs2.next()){
			qtyBndfText.setText(rs2.getString(2));
		}
	}

}
