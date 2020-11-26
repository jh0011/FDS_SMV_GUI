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

public class PresController implements Initializable{
	//pres
	@FXML TextField fishpakText; //integer (+) (3)
    @FXML ComboBox solverCombo;
    
    //comb
    @FXML TextField timeText; //float (+)
    @FXML ComboBox modelCombo;
    
    boolean checkFishpak;
    
    boolean checkTimePres;
    
    static String solverSelection = "";
    static String modelSelection = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
    	
    	if(checkFishpak && checkTimePres) {
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
				combAlert.setContentText("Fixed_mix_time should be a positive value. Please check again.");
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
			combAlert.setContentText("Fixed_mix_time should be a numerical value. Please check again.");
			combAlert.setHeaderText(null);
			combAlert.show();
			return false;
		}
    }
    
    private void storeValues() throws SQLException { //store values into the database
    	storeValuesPres();
    	storeValuesComb();
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
    
    protected void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoPres();
    	showInfoComb();
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

}
