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
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class ObstController implements Initializable{
	//obst
    @FXML TextField bulkText; //float (+)
    @FXML TextField colourText; //string
    @FXML TextField surfIdText; //string
    @FXML TextField xbText; //float (+ / -)
    
    @FXML Button addObstBtn;
    
    boolean checkFloatPos;
    boolean checkXb;
    
    static int mainObstId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip obstTooltip = new Tooltip("Click to add another OBST field.");
		addObstBtn.setTooltip(obstTooltip);
		
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
    private void goToRamp(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	if(checkFloatPos && checkXb) {
    		//store the values
    		storeValues();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Ramp.fxml"));
    		Parent root = loader.load();
    		
    		RampController rampCont = loader.getController(); //Get the next page's controller
    		rampCont.showInfo(); //Set the values of the page 
    		Scene rampScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(rampScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to go back to RAMP page");
    	}
    	
    }
    
    @FXML
    private void newObstLine(ActionEvent event) throws SQLException { //ADD NEW OBST LINE
    	doCheckingObst();
    	
    	if(checkFloatPos && checkXb) {
    		//store the values
    		storeValuesObst();
    		
	    	mainObstId++;
	    	String mainObstIdString = Integer.toString(mainObstId);
	    	String sqlObst = "INSERT INTO obst (mainID, BULK_DENSITY, COLOR, SURF_ID, XB) VALUES ('" + mainObstIdString + "', '', '', '', '');";
	    	ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlObst);
			
			showInfoObst();
    	}
    	else {
    		System.out.println("Unable to add new OBST line");
    	}
    }
    
    private void doChecking() {
    	doCheckingObst();
    }
    
    private void doCheckingObst() {
    	checkFloatPos = true;
    	checkXb = true;
    	if (!bulkText.getText().equals("")) {
    		checkFloatPos = checkFloatPos && checkFloatPosValues(bulkText);
    	}
    	if(!xbText.getText().equals("")) {
    		checkXb = checkXb && checkXbFormat(xbText);
    	}
    }
    
    private boolean checkFloatPosValues(TextField tempField) { //check if float is positive
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert rampAlert = new Alert(Alert.AlertType.INFORMATION);
				rampAlert.setTitle("Invalid value");
				rampAlert.setContentText("Bulk density should be a positive value. Please check again.");
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
			rampAlert.setContentText("Bulk density should be a numerical value. Please check again.");
			rampAlert.setHeaderText(null);
			rampAlert.show();
			return false;
		}
    }
    
    private boolean checkXbFormat(TextField tempField) {
    	if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect XB format");
			devcAlert.setContentText("There should not be any whitespaces.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			return false;
		}
		String[] xbValues = tempField.getText().split(",");
		float[] xbFloatValues = new float[6];
		String concatXB = "";
		
		if (xbValues.length != 6){ //check if it is the correct length
			Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
			devcAlert.setTitle("Incorrect XB format");
			devcAlert.setContentText("There should be 6 real values.");
			devcAlert.setHeaderText(null);
			devcAlert.show();
			return false;
		}
		
		for (int i=0; i<6; i++){ 
			try{
				Float.valueOf(xbValues[i]);
			}
			catch(Exception e){//check if each value is real
			
				Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
				devcAlert.setTitle("Incorrect XB format");
				devcAlert.setContentText("The XB value is not in the correct format. There should be 6 real "
						+ "values, comma-separated. Please check again.");
				devcAlert.setHeaderText(null);
				devcAlert.show();
				return false;
			}
			
			xbFloatValues[i] = Float.valueOf(xbValues[i]); //convert to float
			if (i==5){
				concatXB = concatXB + Float.toString(xbFloatValues[i]);
			}
			else{
				concatXB = concatXB + Float.toString(xbFloatValues[i]) + ","; //convert to string
			}
		}
		tempField.setText(concatXB);
		return true;
    }
    
    private void storeValues() throws SQLException { //store values into the database
    	storeValuesObst();
    }
    
    private void storeValuesObst() throws SQLException { //store OBST values into the database
    	String mainObstIdString = Integer.toString(mainObstId);
    	String sqlObst = "INSERT INTO obst VALUES ('" + mainObstIdString + "', '" + bulkText.getText() + "', '" + colourText.getText() + "', '" + surfIdText.getText() +
    			"', '" + xbText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlObst);
    }
    
    protected void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoObst();
    }
    
    protected void showInfoObst() throws SQLException { //to show the info when the page is loaded
    	String sqlObst = "SELECT * FROM obst";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlObst);
		while (rs.next()) {
			bulkText.setText(rs.getString(2));
			colourText.setText(rs.getString(3));
			surfIdText.setText(rs.getString(4));
			xbText.setText(rs.getString(5));
		}
    }

}
