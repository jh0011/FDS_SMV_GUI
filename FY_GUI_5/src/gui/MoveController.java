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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MoveController implements Initializable{
	/**
	 * Controller class for Move.fxml
	 * @author 
	 */
	
	//move
	@FXML TextField idText; //string
    @FXML TextField xText; //float (+)
    @FXML TextField yText; //float (+)
    @FXML TextField zText; //float (+)
    @FXML TextField angleText; //float (0 - 360)
    @FXML TextField axisText; //float (3) (+)
    
    //prof
    @FXML TextField profIdText; //string
    @FXML TextField xyzText; //float (3) (+)
    @FXML TextField qtyText; //string
    @FXML ComboBox iorCombo;
    
    //radf
    @FXML TextField iText; //integer (+)
    @FXML TextField jText; //integer (+)
    @FXML TextField kText; //integer (+)
    @FXML TextField xbText; //float (6) (+)
    
    boolean checkFloatPos;
    boolean checkAngle;
    boolean checkAxis;
    boolean checkXyz;
    boolean checkIntPos;
    boolean checkXb;
    
    static String iorSelection = "";
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> iorList = FXCollections.observableArrayList("", "-1", "1", "-2", "2", "-3", "3");
		iorCombo.setItems(iorList);
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
	 * Go to the previous page (HVAC) + input validation
	 * @param event Back button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToHvac(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	try {
	    	if(checkFloatPos && checkAngle && checkAxis && checkXyz && checkIntPos && checkXb) {
	    		//store the values
	    		storeValues();
	    		
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Hvac.fxml"));
	    		Parent root = loader.load();
	    		
	    		HvacController hvacCont = loader.getController(); //Get the next page's controller
	    		hvacCont.showInfo(); //Set the values of the page 
	    		Scene hvacScene = new Scene(root);
	    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
	    		mainWindow.setScene(hvacScene);
	    		mainWindow.show();
	    	}
    	}catch(Exception e) {
			Values.showError();
		}
    }
    
    /**
	 * Go to the next page (TRNX) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToTrnx(ActionEvent event) throws IOException, SQLException { //NEXT SCENE
    	doChecking();
    	
    	try {
	    	if(checkFloatPos && checkAngle && checkAxis && checkXyz && checkIntPos && checkXb) {
	    		//store the values
	    		storeValues();
	    		
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Trnx.fxml"));
	    		Parent root = loader.load();
	    		
	    		TrnxController trnxCont = loader.getController(); //Get the next page's controller
	    		trnxCont.showInfo(); //Set the values of the page 
	    		Scene trnxScene = new Scene(root);
	    		Stage mainWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
	    		mainWindow.setScene(trnxScene);
	    		mainWindow.show();
	    	}
    	}catch(Exception e) {
			Values.showError();
		}
    }
    
    /**
	 * Description of MOVE namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openMoveDesc(MouseEvent event) {
    	String content = "The MOVE namelist can be used to rotate and/or translate various objects within an FDS simulation. \n\n"
    			+ "Rotation_angle: The angle of rotation about the axis.\n\n"
    			+ "Axis: The direction vectore from the origin point.\n\n"
    			+ "X0: X-coordinate of the origin point of the axis.\n\n"
    			+ "Y0: Y-coordinate of the origin point of the axis.\n\n"
    			+ "Z0: Z-coordinate of the origin point of the axis.";
    	String namelist = "MOVE";
		Values.openDesc(namelist, content);
    }

    /**
	 * Description of PROF namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openProfDesc(MouseEvent event) {
    	String content = "The PROF (profile output) namelist can be used to record the properties of the solid in depth at discrete intervals of time."
    			+ "FDS uses a fine one-dimensional grid at each boundary cell to calculate the heat conduction and reactions "
    			+ "within a solid. The solid can be a wall cell or a Lagrangian particle. \n\n"
    			+ "Quantity: The physical quantity to monitor.\n\n"
    			+ "XYZ: Triplet of coordinates, comma-separated.\n\n"
    			+ "IOR: The parameter IOR (Index of Orientation) is required for any device that is placed on the surface of a solid."
    			+ " The values +/-1 or +/-2 or +/-3 indicate the direction that the device “points.” For example, IOR=-1 means that the"
    			+ " device is mounted on a wall that faces in the negative x direction.";
    	String namelist = "PROF";
		Values.openDesc(namelist, content);
    }

    /**
	 * Description of RADF namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openRadfDesc(MouseEvent event) {
    	String content = "The RADF (radiation output file) namelist can be used to save data within rectangular blocks of cells. \n\n"
    			+ "I_Step: Number of cells to skip in the X-direction.\n\n"
    			+ "J_Step: Number of cells to skip in the Y-direction.\n\n"
    			+ "K_Step: Number of cells to skip in the Z-direction.\n\n"
    			+ "XB: A sextuplet of numbers, comma-separated.";
    	String namelist = "RADF";
		Values.openDesc(namelist, content);
    }
    
    @FXML
    public void iorSelect(ActionEvent event) {
    	iorSelection = iorCombo.getSelectionModel().getSelectedItem().toString();
    	iorCombo.setValue(iorSelection);
    }
    
    /**
	 * Call the checking methods for the different namelists
	 */
    public void doChecking() {
    	doCheckingMove();
    	doCheckingProf();
    	doCheckingRadf();
    }
    
    public void doCheckingMove() {
    	checkFloatPos = true;
        checkAngle = true;
        checkAxis = true;
        if(!xText.getText().equals("")) {
        	checkFloatPos = checkFloatPos && checkFloatPosValues(xText);
        }
        if(!yText.getText().equals("")) {
        	checkFloatPos = checkFloatPos && checkFloatPosValues(yText);
        }
        if(!zText.getText().equals("")) {
        	checkFloatPos = checkFloatPos && checkFloatPosValues(zText);
        }
        if(!angleText.getText().equals("")) {
        	checkAngle = checkAngle && checkAngleValues(angleText);
        }
        if(!axisText.getText().equals("")) {
        	checkAxis = checkAxis && checkAxisFormat(axisText);
        }
    }
    
    public void doCheckingProf() {
    	checkXyz = true;
    	if(!xyzText.getText().equals("")) {
    		checkXyz = checkXyz && checkXyzFormat(xyzText);
    	}
    }
    
    public void doCheckingRadf() {
    	checkIntPos = true;
    	checkXb = true;
    	if(!iText.getText().equals("")) {
    		checkIntPos = checkIntPos && checkPosIntValues(iText);
    	}
    	if(!jText.getText().equals("")) {
    		checkIntPos = checkIntPos && checkPosIntValues(jText);
    	}
    	if(!kText.getText().equals("")) {
    		checkIntPos = checkIntPos && checkPosIntValues(kText);
    	}
    	if(!xbText.getText().equals("")) {
    		checkXb = checkXb && checkXbFormat(xbText);
    	}
    }
    
    /**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatPosValues(TextField tempField) { //check if float is positive
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal <= 0){ //if it is not a positive float
				Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
				moveAlert.setTitle("Invalid value");
				moveAlert.setContentText("X0, Y0 and Z0 should be positive values. Please check again.");
				moveAlert.setHeaderText(null);
				moveAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Invalid value");
			moveAlert.setContentText("X0, Y0 and Z0 should be numerical values. Please check again.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			return false;
		}
    }
    
    /**
	 * Check if the float a value between 0 and 360 degrees 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkAngleValues(TextField tempField) { //check if float is between 0 and 360
    	try {
			String stringVal = tempField.getText();
			float floatVal = Float.valueOf(stringVal);
			if (floatVal < 0 || floatVal > 360){ //if it is not between 0 and 360
				Alert multAlert = new Alert(Alert.AlertType.INFORMATION);
				multAlert.setTitle("Invalid value");
				multAlert.setContentText("Angle should be a positive value, between 0 and 360. Please check again.");
				multAlert.setHeaderText(null);
				multAlert.show();
				return false;
			}
			tempField.setText(Float.toString(floatVal));
			return true;
		}
		catch (Exception e) { //if it is not a float
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Invalid value");
			moveAlert.setContentText("Angle should be a numerical value. Please check again.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			return false;
		}
    }
    
    /**
     * Check the Axis format: <br>
     * - No white spaces <br>
     * - 3 values <br>
     * - Positive float 
     * @param tempField TextField for user input
     * @return Boolean on whether the check was successful
     */
    public boolean checkAxisFormat(TextField tempField) { //check if 3 positive float
    	if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Incorrect Axis format");
			moveAlert.setContentText("There should not be any whitespaces.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			return false;
		}
		
		String[] axisValues = tempField.getText().split(",");
		String concatAxis = "";
		if (axisValues.length != 3){ //check if axis is the correct length
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Incorrect Axis format");
			moveAlert.setContentText("There should be 3 real values, comma-separated.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				float axisFloat = Float.valueOf(axisValues[i]);
				if (axisFloat < 0){ //check if axis is negative or zero
					Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
					moveAlert.setTitle("Incorrect Axis format");
					moveAlert.setContentText("The Axis values should be positive real values.");
					moveAlert.setHeaderText(null);
					moveAlert.show();
					return false;
				}
				if (i==0 || i==1){ //concatenate to format the axis string
					concatAxis = concatAxis + Float.toString(axisFloat) + ",";
				}
				else{
					concatAxis = concatAxis + Float.toString(axisFloat);
				}
			}
			tempField.setText(concatAxis);
			return true;
		}
		catch(Exception e){ //check if axis is a number
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Incorrect Axis format");
			moveAlert.setContentText("There should be 3 real values.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			e.printStackTrace();
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
    public boolean checkXyzFormat(TextField tempField) { //check if 3 positive float values
    	if (tempField.getText().contains(" ")){ //check if there are any white spaces
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Incorrect XYZ format");
			moveAlert.setContentText("There should not be any whitespaces.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			return false;
		}
		
		String[] xyzValues = tempField.getText().split(",");
		String concatXyz = "";
		if (xyzValues.length != 3){ //check if xyz is the correct length
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Incorrect XYZ format");
			moveAlert.setContentText("There should be 3 real values, comma-separated.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			return false;
		}
		
		try{
			for (int i=0; i<3; i++){
				float xyzFloat = Float.valueOf(xyzValues[i]);
				if (xyzFloat < 0){ //check if xyz is negative or zero
					Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
					moveAlert.setTitle("Incorrect XYZ format");
					moveAlert.setContentText("The XYZ values should be positive real values.");
					moveAlert.setHeaderText(null);
					moveAlert.show();
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
			Alert moveAlert = new Alert(Alert.AlertType.INFORMATION);
			moveAlert.setTitle("Incorrect Axis format");
			moveAlert.setContentText("There should be 3 real values.");
			moveAlert.setHeaderText(null);
			moveAlert.show();
			e.printStackTrace();
			return false;
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
				Alert radfAlert = new Alert(Alert.AlertType.INFORMATION);
				radfAlert.setTitle("Invalid integer value");
				radfAlert.setContentText("I_Step, J_Step and K_Step should be positive integers. Please check again.");
				radfAlert.setHeaderText(null);
				radfAlert.show();
				return false;
			}
			tempField.setText(stringVal);
			return true;
		}
		catch(Exception e){ //if it is not integer
			Alert radfAlert = new Alert(Alert.AlertType.INFORMATION);
			radfAlert.setTitle("Invalid integer value");
			radfAlert.setContentText("I_Step, J_Step and K_Step should be integers. Please check again.");
			radfAlert.setHeaderText(null);
			radfAlert.show();
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

    /**
	 * Store the values into the database after input validation
	 * @throws SQLException
	 */
    public void storeValues() throws SQLException { //store values into the database
    	storeValuesMove();
    	storeValuesProf();
    	storeValuesRadf();
    }
    
    public void storeValuesMove() throws SQLException { //store MOVE values into the database
		String sqlMove = "INSERT INTO move VALUES ('" + idText.getText() + "', '" + xText.getText() + "', '" + yText.getText() + "', '" + zText.getText() + 
    			"', '" + angleText.getText() + "', '" + axisText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlMove);
    }
    public void storeValuesProf() throws SQLException { //store PROF values into the database
		String sqlProf = "INSERT INTO prof VALUES ('" + profIdText.getText() + "', '" + xyzText.getText() + "', '" + qtyText.getText() + "', '" + iorSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlProf);
	}
	
    public void storeValuesRadf() throws SQLException { //store RADF values into the database
		String sqlRadf = "INSERT INTO radf VALUES ('" + iText.getText() + "', '" + jText.getText() + "', '" + kText.getText() + "', '" + xbText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlRadf);
	}
	
    /**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException
	 */
    public void showInfo() throws SQLException { //to show the info when the page is loaded
		showInfoMove();
		showInfoProf();
		showInfoRadf();
	}
	
    public void showInfoMove() throws SQLException { //to show the info when the page is loaded
		String sqlMove = "SELECT * FROM move";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlMove);
		while (rs.next()) {
			idText.setText(rs.getString(1));
			xText.setText(rs.getString(2));
			yText.setText(rs.getString(3));
			zText.setText(rs.getString(4));
			angleText.setText(rs.getString(5));
			axisText.setText(rs.getString(6));
		}
	}
	
    public void showInfoProf() throws SQLException { //to show the info when the page is loaded
		String sqlPrf = "SELECT * FROM prof";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlPrf);
		while (rs.next()) {
			profIdText.setText(rs.getString(1));
			xyzText.setText(rs.getString(2));
			qtyText.setText(rs.getString(3));
			iorSelection = rs.getString(4);
			iorCombo.setValue(iorSelection);
		}
	}
	
    public void showInfoRadf() throws SQLException { //to show the info when the page is loaded
		String sqlRadf = "SELECT * FROM radf";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sqlRadf);
		while (rs.next()) {
			iText.setText(rs.getString(1));
			jText.setText(rs.getString(2));
			kText.setText(rs.getString(3));
			xbText.setText(rs.getString(4));
		}
	}
}
