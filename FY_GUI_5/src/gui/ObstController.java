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

public class ObstController implements Initializable{
	//obst
    @FXML TextField bulkText; //float (+)
    @FXML TextField colourText; //string
    @FXML TextField surfIdText; //string
    @FXML TextField xbText; //float (+ / -)
    
    //misc
    @FXML ComboBox noiseCombo;
    @FXML ComboBox freezeCombo;
    @FXML TextField humidityText; //float (0 - 100)
    @FXML TextField co2Text; //float (+)
    @FXML TextField tmpaText; //float (+ / -)
    @FXML TextField gvecText; //3 float (+ / -)
    
    //radi
    @FXML ComboBox radiCombo;
   
    @FXML Button addObstBtn;
    
    boolean checkFloatPos;
    boolean checkXb;
    
    boolean checkFloatPosMisc;
    boolean checkGvec;
    boolean checkFloatMisc;
    boolean checkFloatPercent;
    
    static String noiseSelection = "";
    static String freezeSelection = "";
    static String radiSelection = "";
    static int mainObstId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip obstTooltip = new Tooltip("Click to add another OBST field.");
		addObstBtn.setTooltip(obstTooltip);
		
		ObservableList<String> noiseList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		noiseCombo.setItems(noiseList);
		
		ObservableList<String> freezeList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		freezeCombo.setItems(freezeList);
		
		ObservableList<String> radiList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		radiCombo.setItems(radiList);
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
    	
    	if(checkFloatPos && checkXb && checkFloatPosMisc && checkGvec && checkFloatMisc && checkFloatPercent) {
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
    private void goToDump(ActionEvent event) throws IOException, SQLException { //NEXT SCENE
    	doChecking();
    	
    	if(checkFloatPos && checkXb && checkFloatPosMisc && checkGvec && checkFloatMisc && checkFloatPercent) {
    		//store the values
    		storeValues();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Dump.fxml"));
    		Parent root = loader.load();
    		
    		DumpController dumpCont = loader.getController(); //Get the next page's controller
    		dumpCont.showInfo(); //Set the values of the page 
    		Scene dumpScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(dumpScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to proceed to DUMP page");
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
    
    @FXML
    private void noiseSelect(ActionEvent event) {
    	noiseSelection = noiseCombo.getSelectionModel().getSelectedItem().toString();
    	noiseCombo.setValue(noiseSelection);
    }
    
    @FXML
    private void freezeSelect(ActionEvent event) {
    	freezeSelection = freezeCombo.getSelectionModel().getSelectedItem().toString();
    	freezeCombo.setValue(freezeSelection);
    }
    
    @FXML
    private void radiSelect(ActionEvent event) {
    	radiSelection = radiCombo.getSelectionModel().getSelectedItem().toString();
    	radiCombo.setValue(radiSelection);
    }
    
    private void doChecking() {
    	doCheckingObst();
    	doCheckingMisc();
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
    
    private void doCheckingMisc() {
    	checkFloatPosMisc = true;
        checkGvec = true;
        checkFloatMisc = true;
        checkFloatPercent = true;
        if(!co2Text.getText().equals("")) {
        	checkFloatPosMisc = checkFloatPosMisc && checkFloatPosValues(co2Text);
        }
        if(!gvecText.getText().equals("")) {
        	checkGvec = checkGvec && checkGvecFormat(gvecText);
        }
        if(!tmpaText.getText().equals("")) {
        	checkFloatMisc = checkFloatMisc && checkFloatValues(tmpaText);
        }
        if(!humidityText.getText().equals("")) {
        	checkFloatPercent = checkFloatPercent && checkPercentValues(humidityText);
        }
    }
    
    private boolean checkFloatPosValues(TextField tempField) { //check if float is positive
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert rampAlert = new Alert(Alert.AlertType.INFORMATION);
				rampAlert.setTitle("Invalid value");
				rampAlert.setContentText("Bulk density and Y_CO2_Infty should be positive values. Please check again.");
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
			rampAlert.setContentText("Bulk density and Y_CO2_Infty should be numerical values. Please check again.");
			rampAlert.setHeaderText(null);
			rampAlert.show();
			return false;
		}
    }
    
    private boolean checkXbFormat(TextField tempField) { //check the XB format
    	if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Incorrect XB format");
			initAlert.setContentText("There should not be any whitespaces.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
		String[] xbValues = tempField.getText().split(",");
		String concatXB = "";
		
		if (xbValues.length != 6){
			Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
			initAlert.setTitle("Incorrect XB format");
			initAlert.setContentText("There should be 6 real values.");
			initAlert.setHeaderText(null);
			initAlert.show();
			return false;
		}
		
		for (int i=0; i<6; i++){ 
			try{
				float floatVal = Float.valueOf(xbValues[i]);
				if (floatVal < 0) { //check if the float is negative
					Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
					initAlert.setTitle("Invalid XB value");
					initAlert.setContentText("The values should not have negative numbers. Please check again.");
					initAlert.setHeaderText(null);
					initAlert.show();
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
			
				Alert initAlert = new Alert(Alert.AlertType.INFORMATION);
				initAlert.setTitle("Incorrect XB format");
				initAlert.setContentText("The XB value is not in the correct format. There should be 6 real "
						+ "values, comma-separated. Please check again.");
				initAlert.setHeaderText(null);
				initAlert.show();
				return false;
			}
		}
		tempField.setText(concatXB);
		return true;
    }
    
    private boolean checkGvecFormat(TextField tempField) {
    	if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert miscAlert = new Alert(Alert.AlertType.INFORMATION);
			miscAlert.setTitle("Incorrect Gvec format");
			miscAlert.setContentText("There should not be any whitespaces.");
			miscAlert.setHeaderText(null);
			miscAlert.show();
			return false;
		}
		
		String[] gvecValues = tempField.getText().split(",");
		String concatGvec = "";
		if (gvecValues.length != 3){ //check if gvec is the correct length
			Alert miscAlert = new Alert(Alert.AlertType.INFORMATION);
			miscAlert.setTitle("Incorrect Gvec format");
			miscAlert.setContentText("There should be 3 real values, comma-separated.");
			miscAlert.setHeaderText(null);
			miscAlert.show();
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				float gvecFloat = Float.valueOf(gvecValues[i]);
//				if (xyzFloat < 0){ //check if xyz is negative or zero
//					Alert devcAlert = new Alert(Alert.AlertType.INFORMATION);
//					devcAlert.setTitle("Incorrect XYZ format");
//					devcAlert.setContentText("The XYZ values should be positive real values.");
//					devcAlert.setHeaderText(null);
//					devcAlert.show();
//					return false;
//				}
				if (i==0 || i==1){ //concatenate to format the xyz string
					concatGvec = concatGvec + Float.toString(gvecFloat) + ",";
				}
				else{
					concatGvec = concatGvec + Float.toString(gvecFloat);
				}
			}
			tempField.setText(concatGvec);
			return true;
		}
		catch(Exception e){ //check if xyz is a number
			Alert miscAlert = new Alert(Alert.AlertType.INFORMATION);
			miscAlert.setTitle("Incorrect Gvec format");
			miscAlert.setContentText("There should be 3 real values.");
			miscAlert.setHeaderText(null);
			miscAlert.show();
			e.printStackTrace();
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
			Alert miscAlert = new Alert(Alert.AlertType.INFORMATION);
			miscAlert.setTitle("Invalid value");
			miscAlert.setContentText("Tmpa should be a numerical value. Please check again.");
			miscAlert.setHeaderText(null);
			miscAlert.show();
			return false;
		}
    }
    
    private boolean checkPercentValues(TextField tempField) { //check if float is a percentage
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal < 0 || floatVal > 100){ //if it is not a percentage
				Alert miscAlert = new Alert(Alert.AlertType.INFORMATION);
				miscAlert.setTitle("Invalid value");
				miscAlert.setContentText("Humidity should be percentage (between 0 and 100). Please check again.");
				miscAlert.setHeaderText(null);
				miscAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert miscAlert = new Alert(Alert.AlertType.INFORMATION);
			miscAlert.setTitle("Invalid value");
			miscAlert.setContentText("Humidity should be a numerical value. Please check again.");
			miscAlert.setHeaderText(null);
			miscAlert.show();
			return false;
		}
    }
    
    private void storeValues() throws SQLException { //store values into the database
    	storeValuesObst();
    	storeValuesMisc();
    	storeValuesRadi();
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
    
    private void storeValuesMisc() throws SQLException { //store MISC values into the database
    	String sqlMisc = "INSERT INTO misc VALUES ('" + noiseSelection + "', '" + freezeSelection + "', '" + humidityText.getText() + "', '" + co2Text.getText() +
    			"', '" + tmpaText.getText() + "', '" + gvecText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlMisc);
    }
    
    private void storeValuesRadi() throws SQLException { //store RADI values into the database
    	String sqlRadi = "INSERT INTO radi VALUES ('" + radiSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlRadi);
    }
    
    protected void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoObst();
    	showInfoMisc();
    	showInfoRadi();
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
    
    protected void showInfoMisc() throws SQLException { //to show the info when the page is loaded
    	String sqlMisc = "SELECT * FROM misc;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlMisc);
		while (rs.next()) {
			noiseSelection = rs.getString(1);
			noiseCombo.setValue(noiseSelection);
			freezeSelection = rs.getString(2);
			freezeCombo.setValue(freezeSelection);
			humidityText.setText(rs.getString(3));
			co2Text.setText(rs.getString(4));
			tmpaText.setText(rs.getString(5));
			gvecText.setText(rs.getString(6));
			
		}
    }
    
    protected void showInfoRadi() throws SQLException { //to show the info when the page is loaded
    	String sqlRadi = "SELECT * FROM radi;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlRadi);
		while (rs.next()) {
			radiSelection = rs.getString(1);
			radiCombo.setValue(radiSelection);
		}
    }

}
