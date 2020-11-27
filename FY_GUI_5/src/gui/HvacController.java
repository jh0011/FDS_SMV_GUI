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
    
    boolean checkFloatPosHvac;
    boolean checkXb;
    
    static String typeSelection = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> typeList = FXCollections.observableArrayList("", "DUCT", "NODE", "FAN", "FILTER", "AIRCOIL", "LEAK");
		typeCombo.setItems(typeList);
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
    private void goToPres(ActionEvent event) throws SQLException, IOException { //PREVIOUS SCENE
    	doChecking();
    	
    	if (checkFloatPosHvac && checkXb) {
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
    
    @FXML
    private void typeSelect(ActionEvent event) {
    	typeSelection = typeCombo.getSelectionModel().getSelectedItem().toString();
    	typeCombo.setValue(typeSelection);
    }
    
    private void doChecking() {
    	doCheckingHvac();
    	doCheckingHole();
    }
    
    private void doCheckingHvac() {
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
    
    private void doCheckingHole() {
    	checkXb = true;
    	if(!xbText.getText().equals("")) {
    		checkXb = checkXb && checkXbFormat(xbText);
    	}
    }
    
    private boolean checkFloatPosValues(TextField tempField) { //check if the float is positive
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
    
    private boolean checkXbFormat(TextField tempField) { //check the XB format
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
    
    private void storeValues() throws SQLException { //store values into the database
    	storeValuesHvac();
    	storeValuesHole();
    }
    
    private void storeValuesHvac() throws SQLException { //store HVAC values into the database
    	String sqlHvac = "INSERT INTO hvac VALUES ('" + idText.getText() + "', '" + roughnessText.getText() + "', '" + devcIdText.getText() + "', '" +
    			lengthText.getText() + "', '" + fanIdText.getText() + "', '" + areaText.getText() + "', '" + typeSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlHvac);
    }
    
    private void storeValuesHole() throws SQLException { //store HOLE values into the database
    	String sqlHole = "INSERT INTO hole VALUES ('" + meshIdText.getText() + "', '" + multIdText.getText() + "', '" + holeDevcText.getText() + "', '" + ctrlIdText.getText() + 
    			"', '" + xbText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlHole);
    }
    
    protected void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoHvac();
    	showInfoHole();
    }

    protected void showInfoHvac() throws SQLException { //to show the info when the page is loaded
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
    
    protected void showInfoHole() throws SQLException { //to show the info when the page is loaded
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
}
