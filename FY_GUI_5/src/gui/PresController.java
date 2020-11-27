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

public class PresController implements Initializable{
	//pres
	@FXML TextField fishpakText; //integer (+) (3)
    @FXML ComboBox solverCombo;
    
    //comb
    @FXML TextField timeText; //float (+)
    @FXML ComboBox modelCombo;
    
    //tabl
    @FXML TextField tableIdText; //string
    @FXML TextField tableDataText; //5 int (+) & 1 float (>0 & <=1)
    
    //clip
    @FXML TextField maxDenText; //float (+)
    @FXML TextField maxTempText; //float (+ / -)
    @FXML  TextField minDenText; //float (+)
    @FXML TextField minTempText; //float (+ / -)

    @FXML Button addTablBtn;
    
    boolean checkFishpak;
    boolean checkTimePres;
    boolean checkTableData;
    boolean checkFloatPosClip;
    boolean checkFloatClip;
    
    static String solverSelection = "";
    static String modelSelection = "";
    static int mainTablId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip tablTooltip = new Tooltip("Click to add another TABL field.");
		addTablBtn.setTooltip(tablTooltip);
		
		ObservableList<String> solverList = FXCollections.observableArrayList("", "FFT", "UGLMAT", "GLMAT");
		solverCombo.setItems(solverList);
		
		ObservableList<String> modelList = FXCollections.observableArrayList("", "EXTINCTION 1", "EXTINCTION 2");
		modelCombo.setItems(modelList);
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
    private void goToMult(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	if(checkFishpak && checkTimePres && checkTableData && checkFloatPosClip && checkFloatClip) {
    		//store the values
        	storeValues();
        	
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("Mult.fxml"));
    		Parent root = loader.load();
    		
    		MultController multCont = loader.getController(); //Get the next page's controller
    		multCont.showInfo(); //Set the values of the page 
    		Scene multScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(multScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to go back to MULT page");
    	}
    }
    
    @FXML
    private void goToHvac(ActionEvent event) throws IOException, SQLException { //NEXT SCENE
    	doChecking();
    	
    	if(checkFishpak && checkTimePres && checkTableData && checkFloatPosClip && checkFloatClip) {
    		//store the values
        	storeValues();
        	
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("Hvac.fxml"));
    		Parent root = loader.load();
    		
    		HvacController hvacCont = loader.getController(); //Get the next page's controller
    		//hvacCont.showInfo(); //Set the values of the page 
    		Scene hvacScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(hvacScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to proceed to HVAC page");
    	}
    }
    
    @FXML
    private void newTablLine(ActionEvent event) throws SQLException { //ADD NEW TABL LINE
    	doCheckingTabl();
    	
    	if(checkTableData) {
    		//store the values
    		storeValuesTabl();
    		
    		mainTablId++;
        	String mainTablIdString = Integer.toString(mainTablId);
        	String sqlTabl = "INSERT INTO tabl (mainID, ID, TABLE_DATA) VALUES ('" + mainTablIdString + "', '', '');";
        	ConnectionClass connectionClass = new ConnectionClass();
    		Connection connection = connectionClass.getConnection();
    		Statement statement = connection.createStatement();
    		statement.executeUpdate(sqlTabl);
    		
    		showInfoTabl();
    	}
    	else {
    		System.out.println("Unable to add new TABL line");
    	}
    }

    @FXML
    private void solverSelect(ActionEvent event) {
    	solverSelection = solverCombo.getSelectionModel().getSelectedItem().toString();
    	solverCombo.setValue(solverSelection);
    }
    
    @FXML
    private void modelSelect(ActionEvent event) {
    	modelSelection = modelCombo.getSelectionModel().getSelectedItem().toString();
    	modelCombo.setValue(modelSelection);
    }
    
    private void doChecking() { 
    	doCheckingPres();
    	doCheckingComb();
    	doCheckingTabl();
    	doCheckingClip();
    }
    
    private void doCheckingPres() {
    	checkFishpak = true;
    	if(!fishpakText.getText().equals("")) {
    		checkFishpak = checkFishpak && checkFishpakFormat(fishpakText);
    	}
    }
    
    private void doCheckingComb() {
    	checkTimePres = true;
    	if(!timeText.getText().equals("")) {
    		checkTimePres = checkTimePres && checkFloatPosValues(timeText);
    	}
    }
    
    private void doCheckingTabl() {
    	checkTableData = true;
    	if(!tableDataText.getText().equals("")) {
    		checkTableData = checkTableData && checkTableDataFormat(tableDataText);
    	}
    }
    
    private void doCheckingClip() {
    	checkFloatPosClip = true;
    	checkFloatClip = true;
    	if(!maxDenText.getText().equals("")) {
    		checkFloatPosClip = checkFloatPosClip && checkFloatPosValues(maxDenText);
    	}
		if(!minDenText.getText().equals("")) {
			checkFloatPosClip = checkFloatPosClip && checkFloatPosValues(minDenText);		
    	}
		if(!maxTempText.getText().equals("")) {
			checkFloatClip = checkFloatClip && checkFloatValues(maxTempText);
		}
		if(!minTempText.getText().equals("")) {
			checkFloatClip = checkFloatClip && checkFloatValues(minTempText);
		}
    }
    
    private boolean checkFishpakFormat(TextField tempField) { //check the fishpak format
		if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert presAlert = new Alert(Alert.AlertType.INFORMATION);
			presAlert.setTitle("Incorrect Fishpak format");
			presAlert.setContentText("There should not be any whitespaces.");
			presAlert.setHeaderText(null);
			presAlert.show();
			return false;
		}
		
		String[] ijkValues = tempField.getText().split(",");
		String concatFishpak = "";
		if (ijkValues.length != 3){ //check if Fishpak is the correct length
			Alert presAlert = new Alert(Alert.AlertType.INFORMATION);
			presAlert.setTitle("Incorrect Fishpak format");
			presAlert.setContentText("There should be 3 integer values, comma-separated.");
			presAlert.setHeaderText(null);
			presAlert.show();
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				int ijkInt = Integer.parseInt(ijkValues[i]);
				if (ijkInt <= 0){ //check if Fishpak is negative or zero
					Alert presAlert = new Alert(Alert.AlertType.INFORMATION);
					presAlert.setTitle("Incorrect Fishpak format");
					presAlert.setContentText("The Fishpak value should be more than zero.");
					presAlert.setHeaderText(null);
					presAlert.show();
					return false;
				}
				if (i==0 || i==1){ //concatenate to format the Fishpak string
					concatFishpak = concatFishpak + String.valueOf(ijkInt) + ",";
				}
				else{
					concatFishpak = concatFishpak + String.valueOf(ijkInt);
				}
			}
			tempField.setText(concatFishpak);
			return true;
		}
		catch(Exception e){ //check if Fishpak is an integer
			Alert presAlert = new Alert(Alert.AlertType.INFORMATION);
			presAlert.setTitle("Incorrect Fishpak format");
			presAlert.setContentText("There should be 3 integer values.");
			presAlert.setHeaderText(null);
			presAlert.show();
			return false;
		}
    }
    
    private boolean checkFloatPosValues(TextField tempField) { //check if the float is positive
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert combAlert = new Alert(Alert.AlertType.INFORMATION);
				combAlert.setTitle("Invalid value");
				combAlert.setContentText("Fixed_mix_time, Max. density and Min. density should be positive values. Please check again.");
				combAlert.setHeaderText(null);
				combAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert combAlert = new Alert(Alert.AlertType.INFORMATION);
			combAlert.setTitle("Invalid value");
			combAlert.setContentText("Fixed_mix_time, Max. density and Min. density should be numerical values. Please check again.");
			combAlert.setHeaderText(null);
			combAlert.show();
			return false;
		}
    }
    
    private boolean checkTableDataFormat(TextField tempField) { //check if 5 integer degree values & 1 fraction
    	if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert tablAlert = new Alert(Alert.AlertType.INFORMATION);
			tablAlert.setTitle("Incorrect Table_data format");
			tablAlert.setContentText("There should not be any whitespaces.");
			tablAlert.setHeaderText(null);
			tablAlert.show();
			return false;
		}
		
		String[] tableDataValues = tempField.getText().split(",");
		String concatTableData = "";
		if (tableDataValues.length != 6){ //check if table data is the correct length
			Alert tablAlert = new Alert(Alert.AlertType.INFORMATION);
			tablAlert.setTitle("Incorrect Table_data format");
			tablAlert.setContentText("There should be 6 numerical values, comma-separated. The first 5 values are degrees integer values and the last value is a fraction in decimal.");
			tablAlert.setHeaderText(null);
			tablAlert.show();
			return false;
		}
		
		try{
			for (int i=0; i<5; i++){
				int tableDataInt = Integer.parseInt(tableDataValues[i]);
				if (tableDataInt < 0 || tableDataInt > 360){ //check if degree is negative or more than 360
					Alert tablAlert = new Alert(Alert.AlertType.INFORMATION);
					tablAlert.setTitle("Incorrect Table_data format");
					tablAlert.setContentText("The first 5 values of Table_data should be between 0 and 360.");
					tablAlert.setHeaderText(null);
					tablAlert.show();
					return false;
				}
				
				concatTableData = concatTableData + String.valueOf(tableDataInt) + ","; //concatenate to format the table data string
			}
			try { //check if the 6th value is a float
				String stringVal = tableDataValues[5];
				float floatVal = Float.valueOf(stringVal);
				if (floatVal <= 0 || floatVal > 1){ //if it is not between 0 and 1
					Alert tablAlert = new Alert(Alert.AlertType.INFORMATION);
					tablAlert.setTitle("Invalid value");
					tablAlert.setContentText("6th value of Table_data should be a positive value between 0 and 1. Please check again.");
					tablAlert.setHeaderText(null);
					tablAlert.show();
					return false;
				}
				concatTableData = concatTableData + Float.toString(floatVal);
				tempField.setText(concatTableData);
				return true;
			}
			catch(Exception e) { //if the 6th value is not a float
				Alert tablAlert = new Alert(Alert.AlertType.INFORMATION);
				tablAlert.setTitle("Invalid value");
				tablAlert.setContentText("6th value of Table_data should be a numerical value. Please check again.");
				tablAlert.setHeaderText(null);
				tablAlert.show();
				return false;
			}
			
			
		}
		catch(Exception e){ //check if first 5 values is an integer
			Alert tablAlert = new Alert(Alert.AlertType.INFORMATION);
			tablAlert.setTitle("Incorrect Table_data format");
			tablAlert.setContentText("The first 5 values should be integers between 0 and 360. Please check again.");
			tablAlert.setHeaderText(null);
			tablAlert.show();
			return false;
		}
    }
    
    private boolean checkFloatValues(TextField tempField) { //check if value is a float
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert clipAlert = new Alert(Alert.AlertType.INFORMATION);
			clipAlert.setTitle("Invalid value");
			clipAlert.setContentText("Max. temp and Min. temp should be a numerical value. Please check again.");
			clipAlert.setHeaderText(null);
			clipAlert.show();
			return false;
		}
    }
    
    private void storeValues() throws SQLException { //store values into the database
    	storeValuesPres();
    	storeValuesComb();
    	storeValuesTabl();
    	storeValuesClip();
    }
    
    private void storeValuesPres() throws SQLException { //store PRES values into the database
    	String sqlPres = "INSERT INTO pres VALUES ('" + fishpakText.getText() + "', '" + solverSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlPres);
    }
    
    private void storeValuesComb() throws SQLException { //store COMB values into the database
    	String sqlComb = "INSERT INTO comb VALUES ('" + timeText.getText() + "', '" + modelSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlComb);
    }
    
    private void storeValuesTabl() throws SQLException { //store TABL values into the database
    	String mainTablIdString = Integer.toString(mainTablId);
    	String sqlTabl = "INSERT INTO tabl VALUES ('" + mainTablIdString + "', '" + tableIdText.getText() + "', '" + tableDataText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlTabl);
    }
    
    private void storeValuesClip() throws SQLException { //store CLIP values into the database
    	String sqlClip = "INSERT INTO clip VALUES ('" + maxDenText.getText() + "', '" + maxTempText.getText() + "', '" + minDenText.getText() + "', '" + minTempText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlClip);
    }
    
    protected void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoPres();
    	showInfoComb();
    	showInfoTabl();
    	showInfoClip();
    }
    
    protected void showInfoPres() throws SQLException { //to show the info when the page is loaded
    	String sqlPres = "SELECT * FROM pres;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlPres);
		while (rs.next()) {
			fishpakText.setText(rs.getString(1));
			solverSelection = rs.getString(2);
			solverCombo.setValue(solverSelection);
		}
    }
    
    protected void showInfoComb() throws SQLException { //to show the info when the page is loaded
    	String sqlComb = "SELECT * FROM comb;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlComb);
		while (rs.next()) {
			timeText.setText(rs.getString(1));
			modelSelection = rs.getString(2);
			modelCombo.setValue(modelSelection);
		}
    }
    
    protected void showInfoTabl() throws SQLException { //to show the info when the page is loaded
    	String sqlTabl = "SELECT * FROM tabl;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlTabl);
		while (rs.next()) {
			tableIdText.setText(rs.getString(2));
			tableDataText.setText(rs.getString(3));
		}
    }
    
    protected void showInfoClip() throws SQLException { //to show the info when the page is loaded
    	String sqlClip = "SELECT * FROM clip;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlClip);
		while (rs.next()) {
			maxDenText.setText(rs.getString(1));
			maxTempText.setText(rs.getString(2));
			minDenText.setText(rs.getString(3));
			minTempText.setText(rs.getString(4));
		}
    }

}