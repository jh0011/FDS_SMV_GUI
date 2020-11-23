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
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class RampController implements Initializable{
	//ramp
	@FXML TextField fractionText; //float (+ / -)
    @FXML TextField timeText; //float (+)
    @FXML TextField rampIdText; //string
    
    //ctrl
    @FXML TextField inputIdText;
    @FXML TextField ctrlRampText;
    @FXML TextField ctrlIdText;
    @FXML ComboBox latchCombo;
    @FXML ComboBox functionCombo;

    @FXML Button addRampBtn;
    @FXML Button addCtrlBtn;
    
    boolean checkFloatPos;
    boolean checkFloat;
    
    static String latchSelection = "";
    static String functionSelection = "";
    static int mainRampId = 1;
    static int mainCtrlId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip rampTooltip = new Tooltip("Click to add another RAMP field.");
		addRampBtn.setTooltip(rampTooltip);
		Tooltip ctrlTooltip = new Tooltip("Click to add another CTRL field.");
		addCtrlBtn.setTooltip(ctrlTooltip);
		
		ObservableList<String> latchList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		latchCombo.setItems(latchList);
		
		ObservableList<String> functionList = FXCollections.observableArrayList("", "ANY", "ALL", "ONLY", "AT_LEAST", "TIME_DELAY", "CUSTOM", "DEADBAND", "KILL", "RESTART", "SUM", 
				"SUBTRACT", "MULTIPLY", "DIVIDE", "POWER", "EXP", "LOG", "COS", "SIN", "ACOS", "ASIN", "MAX", "MIN", "PID");
		functionCombo.setItems(functionList);
	}
	
	@FXML
    private void cancelOption(ActionEvent event) throws SQLException, IOException { //CANCEL
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
    private void goToSurf(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	if(checkFloatPos && checkFloat) {
    		//store the values
    		storeValues();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Surf.fxml"));
    		Parent root = loader.load();
    		
    		SurfController surfCont = loader.getController(); //Get the next page's controller
    		surfCont.showInfo(); //Set the values of the page 
    		Scene surfScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(surfScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to go back to SURF page");
    	}
    	
    }
    
    @FXML
    private void newRampLine(ActionEvent event) throws SQLException { //ADD NEW RAMP LINE
    	doCheckingRamp();
    	
    	if(checkFloatPos && checkFloat) {
    		//store the values 
    		storeValuesRamp();
    		
    		mainRampId++;
        	String mainRampIdString = Integer.toString(mainRampId);
        	String sqlRamp = "INSERT INTO ramp (mainID, FRACTION, TIME, ID) VALUES ('" + mainRampIdString + "', '', '', '');";
        	ConnectionClass connectionClass = new ConnectionClass();
    		Connection connection = connectionClass.getConnection();
    		Statement statement = connection.createStatement();
    		statement = connection.createStatement();
    		statement.executeUpdate(sqlRamp);
    		
    		showInfoRamp();
    	}
    	else {
    		System.out.println("Unable to add new RAMP line");
    	}
    	
    }
    
    @FXML
    private void newCtrlLine(ActionEvent event) throws SQLException { //ADD NEW CTRL LINE
    	//no checking required
    	
    	//store the values
    	storeValuesCtrl();
    	
    	mainCtrlId++;
    	String mainCtrlIdString = Integer.toString(mainCtrlId);
    	String sqlCtrl = "INSERT INTO ctrl (mainID, INPUT_ID, RAMP_ID, ID, LATCH, FUNCTION_TYPE) VALUES ('" + mainCtrlIdString + "', '', '', '', '', '');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement = connection.createStatement();
		statement.executeUpdate(sqlCtrl);
		
		showInfoCtrl();
    }
    
    @FXML
    private void latchSelect(ActionEvent event) {
    	latchSelection = latchCombo.getSelectionModel().getSelectedItem().toString();
    	latchCombo.setValue(latchSelection);
    }
    
    @FXML
    private void functionSelect(ActionEvent event) {
    	functionSelection = functionCombo.getSelectionModel().getSelectedItem().toString();
    	functionCombo.setValue(functionSelection);
    }
    
    private void doChecking() {
    	doCheckingRamp();
    }
    
    private void doCheckingRamp() {
    	checkFloatPos = true;
    	checkFloat = true;
    	
    	if(!timeText.getText().equals("")) {
    		checkFloatPos = checkFloatPos && checkFloatPosValues(timeText);
    	}
    	if(!fractionText.getText().equals("")) {
    		checkFloat = checkFloat && checkFloatValues(fractionText);
    	}
    }
    
    private boolean checkFloatPosValues(TextField tempField) { //check if float is positive
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert rampAlert = new Alert(Alert.AlertType.INFORMATION);
				rampAlert.setTitle("Invalid value");
				rampAlert.setContentText("Time should be a positive value. Please check again.");
				rampAlert.setHeaderText(null);
				rampAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert rampAlert = new Alert(Alert.AlertType.INFORMATION);
			rampAlert.setTitle("Invalid value");
			rampAlert.setContentText("Time should be a numerical value. Please check again.");
			rampAlert.setHeaderText(null);
			rampAlert.show();
			return false;
		}
    }
    
    private boolean checkFloatValues(TextField tempField) { //check if the value is a float
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert surfAlert = new Alert(Alert.AlertType.INFORMATION);
			surfAlert.setTitle("Invalid value");
			surfAlert.setContentText("Fraction should be numerical values. Please check again.");
			surfAlert.setHeaderText(null);
			surfAlert.show();
			return false;
		}
    }
    
    private void storeValues() throws SQLException { //store values into the database
    	storeValuesRamp();
    	storeValuesCtrl();
    }
    
    private void storeValuesRamp() throws SQLException { //store RAMP values into the database
    	String mainRampIdString = Integer.toString(mainRampId);
    	String sqlRamp = "INSERT INTO ramp VALUES ('" + mainRampIdString + "', '" + fractionText.getText() + "', '" + timeText.getText() + "', '" + rampIdText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlRamp);
    }
    
    private void storeValuesCtrl() throws SQLException { //store CTRL values into the database
    	String mainCtrlIdString = Integer.toString(mainCtrlId);
    	String sqlCtrl = "INSERT INTO ctrl VALUES ('" + mainCtrlIdString + "', '" + inputIdText.getText() + "', '" + ctrlRampText.getText() + "', '" + 
    			ctrlIdText.getText() + "', '" + latchSelection + "', '" + functionSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlCtrl);
    }
    
    protected void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoRamp();
    	showInfoCtrl();
    }
    
    protected void showInfoRamp() throws SQLException { //to show the info when the page is loaded
    	String sqlRamp = "SELECT * FROM ramp";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlRamp);
		while (rs.next()) {
			fractionText.setText(rs.getString(2));
			timeText.setText(rs.getString(3));
			rampIdText.setText(rs.getString(4));
		}
    }
    
    protected void showInfoCtrl() throws SQLException { //to show the info when the page is loaded
    	String sqlCtrl = "SELECT * FROM ctrl";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlCtrl);
		while (rs.next()) {
			inputIdText.setText(rs.getString(2));
			ctrlRampText.setText(rs.getString(3));
			ctrlIdText.setText(rs.getString(4));
			latchSelection = rs.getString(5);
			latchCombo.setValue(latchSelection);
			functionSelection = rs.getString(6);
			functionCombo.setValue(functionSelection);
		}
    }

}
