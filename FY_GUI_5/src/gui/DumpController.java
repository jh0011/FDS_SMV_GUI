package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import connectivity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DumpController implements Initializable{
	//dump
	@FXML ComboBox massCombo;
    @FXML ComboBox smokeCombo;
    @FXML TextField framesText; //int (+)
    @FXML TextField dtDevcText; //float (+)
    
    //matl
    @FXML TextField specificText; //float (+)
    @FXML TextField reactionText; //float (+)
    @FXML TextField specIdText; //string
    @FXML TextField idText; //string
    @FXML TextField referenceText; //float (+)
    @FXML TextField reactionsText; //int (+)
    @FXML TextField densityText; //float (+)
    @FXML TextField conductivityText; //float (+)

    @FXML Button addMatlBtn;
    
    boolean checkIntPos;
    boolean checkFloatPos;
    
    boolean checkIntPosMatl;
    boolean checkFloatPosMatl;
    
    static String massSelection = "";
    static String smokeSelection = "";
    static int mainMatlId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> massList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		massCombo.setItems(massList);
		
		ObservableList<String> smokeList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		smokeCombo.setItems(smokeList);
	}
	
	@FXML
    private void cancelOption(ActionEvent event) throws IOException, SQLException { //CANCEL
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
    private void goToObst(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	if(checkIntPos && checkFloatPos && checkIntPosMatl && checkFloatPosMatl) {
    		//store the values
    		storeValues();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Obst.fxml"));
    		Parent root = loader.load();
    		
    		ObstController obstCont = loader.getController(); //Get the next page's controller
    		obstCont.showInfo(); //Set the values of the page 
    		Scene obstScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(obstScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to go back to OBST page");
    	}
    	
    }
    
    @FXML
    private void newMatlLine(ActionEvent event) throws SQLException { //ADD NEW MATL LINE
    	doCheckingMatl();
    	
    	if(checkIntPosMatl && checkFloatPosMatl) {
    		//store the values
    		storeValuesMatl();
    		
    		mainMatlId++;
        	String mainMatlIdString = Integer.toString(mainMatlId);
        	String sqlMatl = "INSERT INTO matl (mainID, SPECIFIC_HEAT, HEAT_OF_REACTION, SPEC_ID, ID, REFERENCE_TEMPERATURE, N_REACTIONS, DENSITY, CONDUCTIVITY) "
    				+ "VALUES ('" + mainMatlIdString + "', '', '', '', '', '', '', '', '')";
        	ConnectionClass connectionClass = new ConnectionClass();
    		Connection connection = connectionClass.getConnection();
    		Statement statement = connection.createStatement();
    		statement = connection.createStatement();
    		statement.executeUpdate(sqlMatl);
    		
    		showInfoMatl();
    	}
    	else {
    		System.out.println("Unable to add new MATL line");
    	}
    	
    }
    
    @FXML
    private void massSelect(ActionEvent event) {
    	massSelection = massCombo.getSelectionModel().getSelectedItem().toString();
    	massCombo.setValue(massSelection);
    }

    @FXML
    private void smokeSelect(ActionEvent event) {
    	smokeSelection = smokeCombo.getSelectionModel().getSelectedItem().toString();
    	smokeCombo.setValue(smokeSelection);
    }
    
    private void doChecking() {
    	doCheckingDump();
    	doCheckingMatl();
    }
    
    private void doCheckingDump() {
    	checkIntPos = true;
    	checkFloatPos = true;
    	if(!framesText.getText().equals("")) {
    		checkIntPos = checkIntPos && checkPosIntValues(framesText);
    	}
    	if(!dtDevcText.getText().equals("")) {
    		checkFloatPos = checkFloatPos && checkPosFloatValues(dtDevcText);
    	}
    }
    
    private void doCheckingMatl() {
    	checkIntPosMatl = true;
    	checkFloatPosMatl = true;
    	if(!reactionsText.getText().equals("")) {
    		checkIntPosMatl = checkIntPosMatl && checkPosIntValues(reactionsText);
    	}
    	if(!specificText.getText().equals("")) {
    		checkFloatPosMatl = checkFloatPosMatl && checkPosFloatValues(specificText);
    	}
		if(!reactionText.getText().equals("")) {
			checkFloatPosMatl = checkFloatPosMatl && checkPosFloatValues(reactionText);		
    	}
		if(!referenceText.getText().equals("")) {
			checkFloatPosMatl = checkFloatPosMatl && checkPosFloatValues(referenceText);
		}
		if(!densityText.getText().equals("")) {
			checkFloatPosMatl = checkFloatPosMatl && checkPosFloatValues(densityText);
		}
		if(!conductivityText.getText().equals("")) {
			checkFloatPosMatl = checkFloatPosMatl && checkPosFloatValues(conductivityText);
		}
    }
    
    private boolean checkPosIntValues(TextField tempField) { //check if integer is positive
    	try{ 
			String stringVal = tempField.getText();
			int intVal = Integer.parseInt(stringVal);
			if (intVal <= 0){ //if it is not a positive integer
				Alert dumpAlert = new Alert(Alert.AlertType.INFORMATION);
				dumpAlert.setTitle("Invalid integer value");
				dumpAlert.setContentText("Nframes and N_reactions should be positive integers. Please check again.");
				dumpAlert.setHeaderText(null);
				dumpAlert.show();
				return false;
			}
			tempField.setText(stringVal);
			return true;
		}
		catch(Exception e){ //if it is not integer
			Alert dumpAlert = new Alert(Alert.AlertType.INFORMATION);
			dumpAlert.setTitle("Invalid integer value");
			dumpAlert.setContentText("Nframes and N_reactions should be integers. Please check again.");
			dumpAlert.setHeaderText(null);
			dumpAlert.show();
			return false;
		}
    }
    
    private boolean checkPosFloatValues(TextField tempField) { //check if float is positive
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert dumpAlert = new Alert(Alert.AlertType.INFORMATION);
				dumpAlert.setTitle("Invalid value");
				dumpAlert.setContentText("Dt_devc, Specific_heat, heat of reaction, reference tmp., density and conductivity should be positive values. Please check again.");
				dumpAlert.setHeaderText(null);
				dumpAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert dumpAlert = new Alert(Alert.AlertType.INFORMATION);
			dumpAlert.setTitle("Invalid value");
			dumpAlert.setContentText("Dt_devc, Specific_heat, heat of reaction, reference tmp., density and conductivity should be numerical values. Please check again.");
			dumpAlert.setHeaderText(null);
			dumpAlert.show();
			return false;
		}
    }
    
    private void storeValues() throws SQLException { //store values into the database
    	storeValuesDump();
    	storeValuesMatl();
    }

    private void storeValuesDump() throws SQLException{ //store DUMP values into the database
    	String sqlDump = "INSERT INTO dump VALUES ('" + massSelection + "', '" + smokeSelection + "', '" + framesText.getText() + "', '" + dtDevcText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlDump);
    }
    
    private void storeValuesMatl() throws SQLException{ //store MATL values into the database
    	String mainMatlIdString = Integer.toString(mainMatlId);
    	String sqlMatl = "INSERT INTO matl VALUES ('" + mainMatlIdString + "', '" + specificText.getText() + "', '" + reactionText.getText() + "', '" + specIdText.getText() + "', '" +
    			idText.getText() + "', '" + referenceText.getText() + "', '" + reactionsText.getText() + "', '" + densityText.getText() + "', '" + conductivityText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlMatl);
    }
    
    protected void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoDump();
    	showInfoMatl();
    }
    
    protected void showInfoDump() throws SQLException { //to show the info when the page is loaded
    	String sqlDump = "SELECT * FROM dump;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlDump);
		while (rs.next()) {
			massSelection = rs.getString(1);
			massCombo.setValue(massSelection);
			smokeSelection = rs.getString(2);
			smokeCombo.setValue(smokeSelection);
			framesText.setText(rs.getString(3));
			dtDevcText.setText(rs.getString(4));
		}
    }
    
    protected void showInfoMatl() throws SQLException { //to show the info when the page is loaded
    	String sqlMatl = "SELECT * FROM matl;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlMatl);
		while (rs.next()) {
			specificText.setText(rs.getString(2));
			reactionText.setText(rs.getString(3));
			specIdText.setText(rs.getString(4));
			idText.setText(rs.getString(5));
			referenceText.setText(rs.getString(6));
			reactionsText.setText(rs.getString(7));
			densityText.setText(rs.getString(8));
			conductivityText.setText(rs.getString(9));
		}
    }
}