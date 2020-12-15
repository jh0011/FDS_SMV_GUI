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

public class TrnxController implements Initializable{
	/**
	 * Controller class for Trnx.fxml
	 * @author 
	 */
	
	//trnx
	@FXML TextField idText; //string
    @FXML TextField meshText; //integer (+)
    @FXML TextField ccText; //float (+)
    @FXML TextField pcText; //float (+)
    
    //zone
    @FXML TextField xyzText; //float (3) (+)

    @FXML Button addTrnxBtn;
   	@FXML Button addZoneBtn;
    
    boolean checkPosInt;
    boolean checkPosFloat;
    boolean checkXyz;
    
    static int mainTrnxId = 1;
    static int mainZoneId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip trnxTooltip = new Tooltip("Click to add another TRNX field.");
		addTrnxBtn.setTooltip(trnxTooltip);
		
		Tooltip zoneTooltip = new Tooltip("Click to add another ZONE field.");
		addZoneBtn.setTooltip(zoneTooltip);
	}
	
	/**
	 * When the Cancel button is clicked to cancel creation of .fds file
	 * @param event Cancel button is clicked
	 * @throws SQLException
	 * @throws IOException
	 */
	@FXML
    public void cancelOption(ActionEvent event) throws IOException, SQLException { //CANCEL
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
	 * Go to the previous page (MOVE) + input validation
	 * @param event Back button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToMove(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	try {
	    	if(checkPosInt && checkPosFloat && checkXyz) {
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
    	}catch(Exception e) {
			Values.showError();
		}
    }
    
    /**
	 * Go to the next page (Editor) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToEditor(ActionEvent event) throws IOException, SQLException { //NEXT SCENE
    	doChecking();
    	
    	try {
	    	if(checkPosInt && checkPosFloat && checkXyz) {
	    		//store the values
	    		storeValues();
	    		
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Editor.fxml"));
	    		Parent root = loader.load();
	    		
	    		EditorController editorCont = loader.getController(); //Get the next page's controller
	    		//editorCont.showInfo(); //Set the values of the page 
	    		Scene editorScene = new Scene(root);
	    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
	    		mainWindow.setScene(editorScene);
	    		mainWindow.show();
	    	}
    	}catch(Exception e) {
			Values.showError();
		}
    }
    
    /**
	 * Add a new line for TRNX namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void newTrnxLine(ActionEvent event) throws SQLException { //ADD NEW TRNX LINE
    	doCheckingTrnx();
    	
    	if (checkPosInt && checkPosFloat) {
    		//store values
    		storeValuesTrnx();
    		
    		mainTrnxId++;
    		String mainTrnxIdString = Integer.toString(mainTrnxId);
    		String sqlTrnx = "INSERT INTO trnx (mainID, ID, MESH_NUMBER, CC, PC) VALUES ('" + mainTrnxIdString + "', '', '', '', '');";
    		ConnectionClass connectionClass = new ConnectionClass();
    		Connection connection = connectionClass.getConnection();
    		Statement statement = connection.createStatement();
    		statement.executeUpdate(sqlTrnx);
    		
    		showInfoTrnx();
    	}
    	else {
    		System.out.println("Unable to add new TRNX line");
    	}
    }
    
    /**
	 * Add a new line for ZONE namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void newZoneLine(ActionEvent event) throws SQLException { //ADD NEW ZONE LINE
    	doCheckingZone();
    	
    	if(checkXyz) {
    		//store values
    		storeValuesZone();
    		
    		mainZoneId++;
        	String mainZoneIdString = Integer.toString(mainZoneId);
        	String sqlZone = "INSERT INTO zone (mainID, XYZ) VALUES ('" + mainZoneIdString + "', '');";
        	ConnectionClass connectionClass = new ConnectionClass();
    		Connection connection = connectionClass.getConnection();
    		Statement statement = connection.createStatement();
    		statement.executeUpdate(sqlZone);
    		
    		showInfoZone();
    	}
    	else {
    		System.out.println("Unable to add new ZONE line");
    	}
    }
    
    /**
	 * Call the checking methods for the different namelists
	 */
    public void doChecking() {
    	doCheckingTrnx();
    	doCheckingZone();
    }
    
    public void doCheckingTrnx() {
    	checkPosInt = true;
    	checkPosFloat = true;
    	if(!meshText.getText().equals("")) {
    		checkPosInt = checkPosInt && checkPosIntValues(meshText);
    	}
    	if(!ccText.getText().equals("")) {
    		checkPosFloat = checkPosFloat && checkPosFloatValues(ccText);
    	}
    	if(!pcText.getText().equals("")) {
    		checkPosFloat = checkPosFloat && checkPosFloatValues(pcText);
    	}
    }
    
    public void doCheckingZone() {
    	checkXyz = true;
    	if (!xyzText.getText().equals("")) {
    		checkXyz = checkXyz && checkXyzFormat(xyzText);
    	}
    }
    
    /**
	 * Check if the integer is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkPosIntValues(TextField tempField) { //check if integer is positive
    	try{ 
			String stringVal = tempField.getText();
			int intVal = Integer.parseInt(stringVal);
			if (intVal <= 0){ //if it is not a positive integer
				Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
				trnxAlert.setTitle("Invalid integer value");
				trnxAlert.setContentText("Mesh_number should be a positive integer. Please check again.");
				trnxAlert.setHeaderText(null);
				trnxAlert.show();
				return false;
			}
			tempField.setText(stringVal);
			return true;
		}
		catch(Exception e){ //if it is not integer
			Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
			trnxAlert.setTitle("Invalid integer value");
			trnxAlert.setContentText("Mesh_number should be an integer. Please check again.");
			trnxAlert.setHeaderText(null);
			trnxAlert.show();
			return false;
		}
    }
    
    /**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkPosFloatValues(TextField tempField) { //check if float is positive
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
				trnxAlert.setTitle("Invalid value");
				trnxAlert.setContentText("CC and PC should be positive values. Please check again.");
				trnxAlert.setHeaderText(null);
				trnxAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert trnxAlert = new Alert(Alert.AlertType.INFORMATION);
			trnxAlert.setTitle("Invalid value");
			trnxAlert.setContentText("CC and PC should be numerical values. Please check again.");
			trnxAlert.setHeaderText(null);
			trnxAlert.show();
			return false;
		}
    }
    
    /**
     * Check the XYZ format: <br>
     * - No white spaces <br>
     * - 3 values <br>
     * - Positive float 
     * @param tempField TextField for user input
     * @return Boolean on whether the check was successful
     */
    public boolean checkXyzFormat(TextField tempField) { //check if xyz is correct
    	if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert zoneAlert = new Alert(Alert.AlertType.INFORMATION);
			zoneAlert.setTitle("Incorrect XYZ format");
			zoneAlert.setContentText("There should not be any whitespaces.");
			zoneAlert.setHeaderText(null);
			zoneAlert.show();
			return false;
		}
		
		String[] xyzValues = tempField.getText().split(",");
		String concatXyz = "";
		if (xyzValues.length != 3){ //check if XYZ is the correct length
			Alert zoneAlert = new Alert(Alert.AlertType.INFORMATION);
			zoneAlert.setTitle("Incorrect XYZ format");
			zoneAlert.setContentText("There should be 3 real values, comma-separated.");
			zoneAlert.setHeaderText(null);
			zoneAlert.show();
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				float xyzFloat = Float.valueOf(xyzValues[i]);
				if (xyzFloat < 0){ //check if xyz is negative or zero
					Alert zoneAlert = new Alert(Alert.AlertType.INFORMATION);
					zoneAlert.setTitle("Incorrect XYZ format");
					zoneAlert.setContentText("The XYZ values should be positive real values.");
					zoneAlert.setHeaderText(null);
					zoneAlert.show();
					return false;
				}
				if (i==0 || i==1){ //concatenate to format the xyz string
					concatXyz = concatXyz + Float.toString(xyzFloat) + ",";
				}
				else{
					concatXyz = concatXyz + Float.toString(xyzFloat);
				}
			}
			tempField.setText(concatXyz);
			return true;
		}
		catch(Exception e){ //check if xyz is a number
			Alert miscAlert = new Alert(Alert.AlertType.INFORMATION);
			miscAlert.setTitle("Incorrect XYZ format");
			miscAlert.setContentText("There should be 3 real values.");
			miscAlert.setHeaderText(null);
			miscAlert.show();
			e.printStackTrace();
			return false;
		}
    }
    
    /**
	 * Store the values into the database after input validation
	 * @throws SQLException
	 */
    public void storeValues() throws SQLException { //store values into the database
    	storeValuesTrnx();
    	storeValuesZone();
    }
    
    public void storeValuesTrnx() throws SQLException { //store TRNX values into the database
    	String mainTrnxIdString = Integer.toString(mainTrnxId);
    	String sqlTrnx = "INSERT INTO trnx VALUES ('" + mainTrnxIdString + "', '" + idText.getText() + "', '" + meshText.getText() + "', '" + ccText.getText() + "', '" + pcText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlTrnx);
    }
    
    public void storeValuesZone() throws SQLException { //store ZONE values into the database
    	String mainZoneIdString = Integer.toString(mainZoneId);
    	String sqlZone = "INSERT INTO zone VALUES ('" + mainZoneIdString + "', '" + xyzText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlZone);
    }
    
    /**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException
	 */
    public void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoTrnx();
    	showInfoZone();
    }
    
    public void showInfoTrnx() throws SQLException { //to show the info when the page is loaded
    	String sqlTrx = "SELECT * FROM trnx";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlTrx);
		while (rs.next()) {
			idText.setText(rs.getString(2));
			meshText.setText(rs.getString(3));
			ccText.setText(rs.getString(4));
			pcText.setText(rs.getString(5));
		}
    }
    
    public void showInfoZone() throws SQLException { //to show the info when the page is loaded
    	String sqlZone = "SELECT * FROM zone";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlZone);
		while (rs.next()) {
			xyzText.setText(rs.getString(2));
		}
    }

}
