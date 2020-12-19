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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SurfController implements Initializable{
	/**
	 * Controller class for Surf.fxml
	 * @author 
	 */
	
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
    
    //vent
    @FXML TextField xbText; //float (+ / -)
    @FXML TextField surfIdText; //string
    @FXML ComboBox mbCombo;
   
    @FXML Button addSurfBtn;
    @FXML Button addVentBtn;
    
    boolean checkFloatPos;
    boolean checkFloat;
    boolean checkMatl;
    boolean checkXb;
    
    static String backingSelection = "";
    static String defaultSelection = "";
    static String geometrySelection = "";
    static String mbSelection = "";
    
    static int mainSurfId = 1;
    static int mainVentId = 1;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Tooltip surfTooltip = new Tooltip("Click to add another SURF field.");
		addSurfBtn.setTooltip(surfTooltip);
		Tooltip ventTooltip = new Tooltip("Click to add another VENT field.");
		addVentBtn.setTooltip(ventTooltip);
		
		ObservableList<String> backingList = FXCollections.observableArrayList("", "INSULATED", "EXPOSED", "VOID");
		backingCombo.setItems(backingList);
		
		ObservableList<String> defaultList = FXCollections.observableArrayList("", "TRUE", "FALSE");
		defaultCombo.setItems(defaultList);
		
		ObservableList<String> geometryList = FXCollections.observableArrayList("", "CYLINDRICAL", "SPHERICAL");
		geometryCombo.setItems(geometryList);
		
		ObservableList<String> mbList = FXCollections.observableArrayList("", "XMAX", "XMIN", "YMAX", "YMIN", "ZMAX", "ZMIN");
		mbCombo.setItems(mbList);
		
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
	 * Go to the previous page (DEVC) + input validation
	 * @param event Back button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToDevc(ActionEvent event) throws SQLException, IOException { //PREVIOUS SCENE
    	doChecking();
    	
    	try {
	    	if(checkFloatPos && checkFloat & checkMatl && checkXb) {
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
    	}catch(Exception e) {
			Values.showError();
		}
    	
    }
    
    /**
	 * Go to the next page (RAMP) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToRamp(ActionEvent event) throws SQLException, IOException { //NEXT SCENE
    	doChecking();
    	
    	try {
	    	if(checkFloatPos && checkFloat & checkMatl && checkXb) {
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
    	}catch(Exception e) {
			Values.showError();
		}
    }
    
    /**
	 * Add a new line for SURF namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void newSurfLine(ActionEvent event) throws SQLException { //ADD NEW SURF LINE
    	doCheckingSurf();
    	
    	if(checkFloatPos && checkFloat && checkMatl) {
    		//store the values
    		storeValuesSurf();
    		
    		//confirmation message for success
			Values.printConfirmationMessage("SURF", true);
    		
	    	mainSurfId++;
	    	String mainSurfIdString = Integer.toString(mainSurfId);
	    	String sqlSurf = "INSERT INTO surf (mainID, ID, PART_ID, MATL_ID, VEL, TMP_FRONT, BACKING, DEFAULT_SURF, GEOMETRY, " + 
					"COLOR, HRRPUA) VALUES ('" + mainSurfIdString + "', '', '', '', '', '', '', '', '', '', '');";
	    	ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlSurf);
			
			showInfoSurf();
    	}
    	else {
    		//confirmation message for failure
			Values.printConfirmationMessage("SURF", false);
    		//System.out.println("Unable to add new SURF line");
    	}
    }
    
    /**
	 * Add a new line for VENT namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void newVentLine(ActionEvent event) throws SQLException { //ADD NEW VENT LINE
    	doCheckingVent();
    	
    	if(checkXb) {
    		//store the values
    		storeValuesVent();
    		
    		//confirmation message for success
			Values.printConfirmationMessage("VENT", true);
    		
    		mainVentId++;
    		String mainVentIdString = Integer.toString(mainVentId);
    		String sqlVent = "INSERT INTO vent (mainID, XB, SURF_ID, MB) VALUES ('" + mainVentIdString + "', '', '', '');";
    		ConnectionClass connectionClass = new ConnectionClass();
			Connection connection = connectionClass.getConnection();
			Statement statement = connection.createStatement();
			statement = connection.createStatement();
			statement.executeUpdate(sqlVent);
			
			showInfoVent();
    	}
    	else{
    		//confirmation message for failure
			Values.printConfirmationMessage("VENT", false);
    		//System.out.println("Unable to add new VENT line");
    	}
    }
    
    /**
	 * Description of SURF namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openSurfDesc(MouseEvent event) {
    	String content = "The SURF namelist defines the structure of all solid surfaces or openings within or bounding the flow domain. \n\n"
    			+ "Tmp_front: Sometimes it is convenient to specify a fixed temperature boundary condition, in which case set Tmp_front "
    			+ "to be the surface temperature in units of degree Celsius.\n\n"
    			+ "Backing: The expression BACKING='INSULATED' prevents any heat loss from the back side of the material."
    			+ "If the wall is assumed to back up to the room on the other side of the wall and you want FDS to calculate "
    			+ "the heat transfer through the wall into the space behind the wall, the attribute BACKING='EXPOSED' should be used."
    			+ "If the wall is assumed to always back up to the ambient, then the attribute BACKING='VOID' should be set.\n\n"
    			+ "Default: If a particular SURF line is to be applied as the default boundary condition, set Default to TRUE.\n\n"
    			+ "Geometry: many objects, like cables, pipes, and ducts, are not flat. Even though these objects have to "
    			+ "be represented in FDS as boxes, you can still perform the internal heat transfer calculation as if the object were really cylindrical or spherical.\n\n"
    			+ "Color: If you define the COLOR by name, it is important that you type the name exactly as it is listed in the color tables.\n\n"
    			+ "HRRPUA: A specified fire is basically modeled as the ejection of gaseous fuel from a solid surface or vent. This is "
    			+ "essentially a burner, with a specified Heat Release Rate Per Unit Area, HRRPUA, in units of kW/m2.";
    	String namelist = "SURF";
		Values.openDesc(namelist, content);
    }
    
    /**
	 * Description of VENT namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openVentDesc(MouseEvent event) {
    	String content = "The VENT namelist is used to prescribe planes adjacent to obstructions or external walls.\n\n"
    			+ "Surf_ID: To specify the surface ID.\n\n"
    			+ "XB: the sextuplet XB denoting a plane abutting a solid surface. Two of the six coordinates "
    			+ "must be the same, denoting a plane as opposed to a solid.\n\n"
    			+ "MB: An easy way to specify an entire external wall is to replace XB with MB (mesh boundary).";
    	String namelist = "VENT";
		Values.openDesc(namelist, content);
    }
    
    @FXML
    public void backingSelect(ActionEvent event) {
    	backingSelection = backingCombo.getSelectionModel().getSelectedItem().toString();
    	backingCombo.setValue(backingSelection);
    }
    
    @FXML
    public void defaultSelect(ActionEvent event) {
    	defaultSelection = defaultCombo.getSelectionModel().getSelectedItem().toString();
    	defaultCombo.setValue(defaultSelection);
    }

    @FXML
    public void geometrySelect(ActionEvent event) {
    	geometrySelection = geometryCombo.getSelectionModel().getSelectedItem().toString();
    	geometryCombo.setValue(geometrySelection);
    }
    
    @FXML
    public void mbSelect(ActionEvent event) {
    	mbSelection = mbCombo.getSelectionModel().getSelectedItem().toString();
    	mbCombo.setValue(mbSelection);
    }
    
    /**
	 * Call the checking methods for the different namelists
	 */
    public void doChecking() {
    	doCheckingSurf();
    	doCheckingVent();
    }
    
    public void doCheckingSurf() {
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
    
    public void doCheckingVent() {
    	checkXb = true;
    	if(!xbText.getText().equals("")) {
    		checkXb = checkXb && checkXbFormat(xbText);
    	}
    }
    
    /**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatPosValues(TextField tempField) { //check if the float values are POSITIVE
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
    
    /**
	 * Check if the value is a float
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatValues(TextField tempField) { //check if values are a float
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
    
    /**
	 * Check if there is one Material ID
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkMatlValues(TextField tempField) {
    	String stringVal = tempField.getText();
    	if(stringVal.contains(",") || stringVal.contains(";")) {
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
    public void storeValues() throws SQLException{ //store values into the database
    	storeValuesSurf();
    	storeValuesVent();
    }
    
    public void storeValuesSurf() throws SQLException { //store SURF values into the database
    	String mainSurfIdString = Integer.toString(mainSurfId);
    	String sqlSurf = "INSERT INTO surf VALUES ('" + mainSurfIdString + "', '" + idText.getText() + "', '" + partIdText.getText() + "', '" + matlIdText.getText() + "', '" +
    			velText.getText() + "', '" + tmpFrontText.getText() + "', '" + backingSelection + "', '" + defaultSelection + "', '" + geometrySelection + "', '" + colourText.getText() +
    			"', '" + hrrpuaText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlSurf);
    }
    
    public void storeValuesVent() throws SQLException { //store VENT values into the database
    	String mainVentIdString = Integer.toString(mainVentId);
    	String sqlVent = "INSERT INTO vent VALUES ('" + mainVentIdString + "', '" + xbText.getText() + "', '" + surfIdText.getText() + "', '" + mbSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlVent);
    	
    }
    
    /**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException
	 */
    public void showInfo() throws SQLException { //to show the info when the page is loaded
    	showInfoSurf();
    	showInfoVent();
    }
    
    public void showInfoSurf() throws SQLException { //to show the info when the page is loaded
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
    
    public void showInfoVent() throws SQLException { //to show the info when the page is loaded
    	String sqlVent = "SELECT * FROM vent;";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		ResultSet rs2 = statement.executeQuery(sqlVent);
		while (rs2.next()) {
			xbText.setText(rs2.getString(2));
			surfIdText.setText(rs2.getString(3));
			mbSelection = rs2.getString(4);
			mbCombo.setValue(mbSelection);
		}
    }
}
