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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HvacController implements Initializable{
	/**
	 * Controller class for Hvac.fxml
	 * @author 
	 */
	
	//hvac
	@FXML TextField idText; //string
    @FXML TextField roughnessText; //float (+)
    @FXML TextField devcIdText; //string
    @FXML TextField lengthText; //float (+)
    @FXML TextField fanIdText; //string
    @FXML TextField areaText; //float (+)
    @FXML ComboBox typeCombo;
    
    //hole
    @FXML TextField meshIdText; //string
    @FXML TextField multIdText; //string
    @FXML TextField holeDevcText; //string
    @FXML TextField ctrlIdText; //string
    @FXML TextField xbText; //xb float (6) (+)
    
    //isof
    @FXML TextField qtyText; //string
    @FXML TextField val1Text; //float (+ / -)
    @FXML TextField val2Text; //float (+ / -)
    @FXML TextField val3Text; //float (+ / -)
    
    boolean checkFloatPosHvac;
    boolean checkXb;
    boolean checkFloat;
    
    static String typeSelection = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> typeList = FXCollections.observableArrayList("", "DUCT", "NODE", "FAN", "FILTER", "AIRCOIL", "LEAK");
		typeCombo.setItems(typeList);
	}
	
	/**
	 * When the Cancel button is clicked to cancel creation of .fds file
	 * @param event Cancel button is clicked
	 * @throws SQLException
	 * @throws IOException
	 */
	@FXML
	public void cancelOption(ActionEvent event) throws SQLException, IOException { //CANCEL
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
	 * Go to the previous page (PRES) + input validation
	 * @param event Back button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToPres(ActionEvent event) throws SQLException, IOException { //PREVIOUS SCENE
    	doChecking();
    	
    	if (checkFloatPosHvac && checkXb && checkFloat) {
    		//store the values
    		storeValues();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Pres.fxml"));
    		Parent root = loader.load();
    		
    		PresController presCont = loader.getController(); //Get the next page's controller
    		presCont.showInfo(); //Set the values of the page 
    		Scene presScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(presScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to go back to PRES page");
    	}
    }
    
    /**
	 * Go to the next page (MOVE) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToMove(ActionEvent event) throws SQLException, IOException { //NEXT SCENE
    	doChecking();
    	
    	if (checkFloatPosHvac && checkXb && checkFloat) {
    		//store the values
    		storeValues();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Move.fxml"));
    		Parent root = loader.load();
    		
    		MoveController moveCont = loader.getController(); //Get the next page's controller
    		moveCont.showInfo(); //Set the values of the page 
    		Scene moveScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(moveScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to proceed to MOVE page");
    	}
    }
    
    @FXML
    public void typeSelect(ActionEvent event) {
    	typeSelection = typeCombo.getSelectionModel().getSelectedItem().toString();
    	typeCombo.setValue(typeSelection);
    }
    
    /**
	 * Call the checking methods for the different namelists
	 */
    public void doChecking() {
    	doCheckingHvac();
    	doCheckingHole();
    	doCheckingIsof();
    }
    
    public void doCheckingHvac() {
    	checkFloatPosHvac = true;
    	if (!roughnessText.getText().equals("")) {
    		checkFloatPosHvac = checkFloatPosHvac && checkFloatPosValues(roughnessText);
    	}
    	if (!lengthText.getText().equals("")) {
    		checkFloatPosHvac = checkFloatPosHvac && checkFloatPosValues(lengthText);
    	}
    	if (!areaText.getText().equals("")) {
    		checkFloatPosHvac = checkFloatPosHvac && checkFloatPosValues(areaText);
    	}
    }
    
    public void doCheckingHole() {
    	checkXb = true;
    	if(!xbText.getText().equals("")) {
    		checkXb = checkXb && checkXbFormat(xbText);
    	}
    }
    public void doCheckingIsof() {
    	checkFloat = true;
    	if(!val1Text.getText().equals("")) {
    		checkFloat = checkFloat && checkFloatValues(val1Text);
    	}
    	if(!val2Text.getText().equals("")) {
    		checkFloat = checkFloat && checkFloatValues(val2Text);
    	}
    	if(!val3Text.getText().equals("")) {
    		checkFloat = checkFloat && checkFloatValues(val3Text);
    	}
    }
    
    /**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatPosValues(TextField tempField) { //check if the float is positive
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert hvacAlert = new Alert(Alert.AlertType.INFORMATION);
				hvacAlert.setTitle("Invalid value");
				hvacAlert.setContentText("Roughness, length and area should be positive values. Please check again.");
				hvacAlert.setHeaderText(null);
				hvacAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert hvacAlert = new Alert(Alert.AlertType.INFORMATION);
			hvacAlert.setTitle("Invalid value");
			hvacAlert.setContentText("Roughness, length and area should be numerical values. Please check again.");
			hvacAlert.setHeaderText(null);
			hvacAlert.show();
			return false;
		}
    }
    
    /**
     * Check the XB format: <br>
     * - No white spaces <br>
     * - 6 values <br>
     * - Positive float 
     * @param tempField TextField for user input
     * @return Boolean on whether the check was successful
     */
    public boolean checkXbFormat(TextField tempField) { //check the XB format
    	if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert holeAlert = new Alert(Alert.AlertType.INFORMATION);
			holeAlert.setTitle("Incorrect XB format");
			holeAlert.setContentText("There should not be any whitespaces.");
			holeAlert.setHeaderText(null);
			holeAlert.show();
			return false;
		}
		String[] xbValues = tempField.getText().split(",");
		String concatXB = "";
		
		if (xbValues.length != 6){
			Alert holeAlert = new Alert(Alert.AlertType.INFORMATION);
			holeAlert.setTitle("Incorrect XB format");
			holeAlert.setContentText("There should be 6 real values.");
			holeAlert.setHeaderText(null);
			holeAlert.show();
			return false;
		}
		
		for (int i=0; i<6; i++){ 
			try{
				float floatVal = Float.valueOf(xbValues[i]);
				if (floatVal < 0) { //check if the float is negative
					Alert holeAlert = new Alert(Alert.AlertType.INFORMATION);
					holeAlert.setTitle("Invalid XB value");
					holeAlert.setContentText("The values should not have negative numbers. Please check again.");
					holeAlert.setHeaderText(null);
					holeAlert.show();
					return false;
				}
				if (i==5){
					concatXB = concatXB + Float.toString(floatVal);
				}
				else{
					concatXB = concatXB + Float.toString(floatVal) + ","; //convert to string
				}
			}
			catch(Exception e){//check if each value is real
			
				Alert holeAlert = new Alert(Alert.AlertType.INFORMATION);
				holeAlert.setTitle("Incorrect XB format");
				holeAlert.setContentText("The XB value is not in the correct format. There should be 6 real "
						+ "values, comma-separated. Please check again.");
				holeAlert.setHeaderText(null);
				holeAlert.show();
				return false;
			}
		}
		tempField.setText(concatXB);
		return true;
    }
    
    /**
	 * Check if the value is a float 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatValues(TextField tempField) { //check if value is a float
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert miscAlert = new Alert(Alert.AlertType.INFORMATION);
			miscAlert.setTitle("Invalid value");
			miscAlert.setContentText("Value(1), Value(2) and Value(3) should be numerical values. Please check again.");
			miscAlert.setHeaderText(null);
			miscAlert.show();
			return false;
		}
    }
    
    /**
	 * Store the values into the database after input validation
	 * @throws SQLException
	 */
    public void storeValues() throws SQLException { //store values into the database
    	storeValuesHvac();
    	storeValuesHole();
    	storeValuesIsof();
    }
    
    public void storeValuesHvac() throws SQLException { //store HVAC values into the database
    	String sqlHvac = "INSERT INTO hvac VALUES ('" + idText.getText() + "', '" + roughnessText.getText() + "', '" + devcIdText.getText() + "', '" +
    			lengthText.getText() + "', '" + fanIdText.getText() + "', '" + areaText.getText() + "', '" + typeSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlHvac);
    }
    
    public void storeValuesHole() throws SQLException { //store HOLE values into the database
    	String sqlHole = "INSERT INTO hole VALUES ('" + meshIdText.getText() + "', '" + multIdText.getText() + "', '" + holeDevcText.getText() + "', '" + ctrlIdText.getText() + 
    			"', '" + xbText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlHole);
    }
    
    public void storeValuesIsof() throws SQLException { //store ISOF values into the database
    	String sqlIsof = "INSERT INTO isof VALUES ('" + qtyText.getText() + "', '" + val1Text.getText() + "', '" + val2Text.getText() + "', '" + val3Text.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlIsof);
    }
    
    /**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException
	 */
    public void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoHvac();
    	showInfoHole();
    	showInfoIsof();
    }

    public void showInfoHvac() throws SQLException { //to show the info when the page is loaded
    	String sqlHvac = "SELECT * FROM hvac";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlHvac);
		while (rs.next()) {
			idText.setText(rs.getString(1));
			roughnessText.setText(rs.getString(2));
			devcIdText.setText(rs.getString(3));
			lengthText.setText(rs.getString(4));
			fanIdText.setText(rs.getString(5));
			areaText.setText(rs.getString(6));
			typeSelection = rs.getString(7);
			typeCombo.setValue(typeSelection);
		}
    }
    
    public void showInfoHole() throws SQLException { //to show the info when the page is loaded
    	String sqlHole = "SELECT * FROM hole";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlHole);
		while (rs.next()) {
			meshIdText.setText(rs.getString(1));
			multIdText.setText(rs.getString(2));
			holeDevcText.setText(rs.getString(3));
			ctrlIdText.setText(rs.getString(4));
			xbText.setText(rs.getString(5));
		}
    }
    
    public void showInfoIsof() throws SQLException { //to show the info when the page is loaded
    	String sqlIsof = "SELECT * FROM isof";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlIsof);
		while (rs.next()) {
			qtyText.setText(rs.getString(1));
			val1Text.setText(rs.getString(2));
			val2Text.setText(rs.getString(3));
			val3Text.setText(rs.getString(4));
		}
    }
}
