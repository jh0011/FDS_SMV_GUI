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

public class SurfController implements Initializable{
	//surf
    @FXML TextField idText; //string
    @FXML TextField partIdText; //string
    @FXML TextField matlIdText; //string
    @FXML TextField velText; //float (+ / -)
    @FXML TextField tmpFrontText; //float (+)
    @FXML ComboBox backingCombo;
    @FXML ComboBox defaultCombo;
    @FXML ComboBox geometryCombo;
    @FXML TextField colourText; //string
    @FXML TextField hrrpuaText; //float (+)
    
    @FXML Button addSurfBtn;
    
    boolean checkFloatPos;
    boolean checkFloat;
    boolean checkMatl;
    
    static String backingSelection = "";
    static String defaultSelection = "";
    static String geometrySelection = "";
    
    static int mainSurfId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip surfTooltip = new Tooltip("Click to add another SURF field.");
		addSurfBtn.setTooltip(surfTooltip);
		
		ObservableList<String> backingList = FXCollections.observableArrayList("", "INSULATED", "EXPOSED", "VOID");
		backingCombo.setItems(backingList);
		
		ObservableList<String> defaultList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		defaultCombo.setItems(defaultList);
		
		ObservableList<String> geometryList = FXCollections.observableArrayList("", "CYLINDRICAL", "SPHERICAL");
		geometryCombo.setItems(geometryList);
		
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
    private void goToDevc(ActionEvent event) throws SQLException, IOException { //PREVIOUS SCENE
    	doChecking();
    	
    	if(checkFloatPos && checkFloat & checkMatl) {
    		//store the values
    		storeValues();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Devc.fxml"));
    		Parent root = loader.load();
    		
    		DevcController devcCont = loader.getController(); //Get the next page's controller
    		devcCont.showInfo(); //Set the values of the page 
    		Scene devcScene = new Scene(root);
    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
    		mainWindow.setScene(devcScene);
    		mainWindow.show();
    	}
    	else {
    		System.out.println("Unable to go back to DEVC page");
    	}
    	
    }
    
    @FXML
    private void newSurfLine(ActionEvent event) throws SQLException { //ADD NEW SURF LINE
    	doCheckingSurf();
    	
    	if(checkFloatPos && checkFloat && checkMatl) {
    		//store the values
    		storeValues();
    		
	    	mainSurfId++;
	    	String mainSurfIdString = Integer.toString(mainSurfId);
	    	String sqlSurf = "INSERT INTO surf (mainID, ID, PART_ID, MATL_ID, VEL, TMP_FRONT, BACKING, DEFAULT_SURF, GEOMETRY, " + 
					"COLOR, HRRPUA) VALUES ('" + mainSurfIdString + "', '', '', '', '', '', '', '', '', '', '');";
	    	ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlSurf);
			
			showInfo();
    	}
    	else {
    		System.out.println("Unable to add new SURF line");
    	}
    }
    
    @FXML
    private void backingSelect(ActionEvent event) {
    	backingSelection = backingCombo.getSelectionModel().getSelectedItem().toString();
    	backingCombo.setValue(backingSelection);
    }
    
    @FXML
    private void defaultSelect(ActionEvent event) {
    	defaultSelection = defaultCombo.getSelectionModel().getSelectedItem().toString();
    	defaultCombo.setValue(defaultSelection);
    }

    @FXML
    private void geometrySelect(ActionEvent event) {
    	geometrySelection = geometryCombo.getSelectionModel().getSelectedItem().toString();
    	geometryCombo.setValue(geometrySelection);
    }
    
    private void doChecking() {
    	doCheckingSurf();
    }
    
    private void doCheckingSurf() {
    	checkFloatPos = true;
    	checkFloat = true;
    	checkMatl = true;
    	
    	if(!tmpFrontText.getText().equals("")) {
    		checkFloatPos = checkFloatPos && checkFloatPosValues(tmpFrontText);
    	}
    	if(!hrrpuaText.getText().equals("")) {
    		checkFloatPos = checkFloatPos && checkFloatPosValues(hrrpuaText);
    	}
    	if(!velText.getText().equals("")) {
    		checkFloat = checkFloat && checkFloatValues(velText);
    	}
    	if(!matlIdText.getText().equals("")) {
    		checkMatl = checkMatl && checkMatlValues(matlIdText);
    	}
    }
    
    private boolean checkFloatPosValues(TextField tempField) { //check if the float values are POSITIVE
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert surfAlert = new Alert(Alert.AlertType.INFORMATION);
				surfAlert.setTitle("Invalid value");
				surfAlert.setContentText("Tmp_front and HRRPUA should be positive values. Please check again.");
				surfAlert.setHeaderText(null);
				surfAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert surfAlert = new Alert(Alert.AlertType.INFORMATION);
			surfAlert.setTitle("Invalid value");
			surfAlert.setContentText("Tmp_front and HRRPUA should be numerical values. Please check again.");
			surfAlert.setHeaderText(null);
			surfAlert.show();
			return false;
		}
    }
    
    private boolean checkFloatValues(TextField tempField) { //check if values are a float
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert surfAlert = new Alert(Alert.AlertType.INFORMATION);
			surfAlert.setTitle("Invalid value");
			surfAlert.setContentText("Velocity should be numerical values. Please check again.");
			surfAlert.setHeaderText(null);
			surfAlert.show();
			return false;
		}
    }
    
    private boolean checkMatlValues(TextField tempField) {
    	String stringVal = tempField.getText();
    	if(stringVal.contains(",")) {
    		Alert surfAlert = new Alert(Alert.AlertType.INFORMATION);
			surfAlert.setTitle("Invalid value");
			surfAlert.setContentText("Matl_ID should only have one value. Please check again.");
			surfAlert.setHeaderText(null);
			surfAlert.show();
			return false;
    	}
    	tempField.setText(stringVal);
    	return true;
    }
    
    private void storeValues() throws SQLException { //store values into the database
    	String mainSurfIdString = Integer.toString(mainSurfId);
    	String sqlSurf = "INSERT INTO surf VALUES ('" + mainSurfIdString + "', '" + idText.getText() + "', '" + partIdText.getText() + "', '" + matlIdText.getText() + "', '" +
    			velText.getText() + "', '" + tmpFrontText.getText() + "', '" + backingSelection + "', '" + defaultSelection + "', '" + geometrySelection + "', '" + colourText.getText() +
    			"', '" + hrrpuaText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlSurf);
    }
    
    protected void showInfo() throws SQLException {
    	String sqlSurf = "SELECT * FROM surf;";
		ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlSurf);
		while (rs.next()) {
			idText.setText(rs.getString(2));
			partIdText.setText(rs.getString(3));
			matlIdText.setText(rs.getString(4));
			velText.setText(rs.getString(5));
			tmpFrontText.setText(rs.getString(6));
			backingSelection = rs.getString(7);
			backingCombo.setValue(backingSelection);
			defaultSelection = rs.getString(8);
			defaultCombo.setValue(defaultSelection);
			geometrySelection = rs.getString(9);
			geometryCombo.setValue(geometrySelection);
			colourText.setText(rs.getString(10));
			hrrpuaText.setText(rs.getString(11));
		}
    }

}
