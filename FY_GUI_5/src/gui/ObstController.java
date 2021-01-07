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

public class ObstController implements Initializable{
	/**
	 * Controller class for Obst.fxml
	 * @author 
	 */
	
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
	 * Go to the previous page (RAMP) + input validation
	 * @param event Back button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToRamp(ActionEvent event) throws IOException, SQLException { //PREVIOUS SCENE
    	doChecking();
    	
    	try {
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
    	}catch(Exception e) {
			Values.showError();
		}
    	
    }
    
    /**
	 * Go to the next page (DUMP) + input validation
	 * @param event Next button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void goToDump(ActionEvent event) throws IOException, SQLException { //NEXT SCENE
    	doChecking();
    	
    	try {
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
    	}catch(Exception e) {
			Values.showError();
		}
    }
    
    /**
	 * Add a new line for OBST namelist
	 * @param event The add button is clicked
	 * @throws IOException
	 * @throws SQLException
	 */
    @FXML
    public void newObstLine(ActionEvent event) throws SQLException { //ADD NEW OBST LINE
    	doCheckingObst();
    	
    	if(checkFloatPos && checkXb) {
    		//store the values
    		storeValuesObst();
    		
    		//confirmation message for success
			Values.printConfirmationMessage("OBST", true);
    		
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
    		//confirmation message for failure
			Values.printConfirmationMessage("OBST", false);
    		//System.out.println("Unable to add new OBST line");
    	}
    }
    
    /**
	 * Description of OBST namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openObstDesc(MouseEvent event) {
    	String content = "The OBST namelist is used to define obstructions. The entire geometry of the model "
    			+ "is made up entirely of rectangular solids, each one introduced on a single line in the input file. \n\n"
    			+ "XB: This solid is defined by two points (x1,y1,z1) and (x2,y2,z2) that are entered in terms of the real sextuplet.\n\n"
    			+ "Surf_ID: The boundary conditions for the obstruction can be specified with the parameter Surf_ID, which "
    			+ "designates which SURF line to apply at the surface of the obstruction.\n\n"
    			+ "Bulk density: This parameter is used to determine the combustible mass of the solid object in units of kg/m3.\n\n"
    			+ "Color: To set the colour of the obstruction.";
    	String namelist = "OBST";
		Values.openDesc(namelist, content);
    }
    
    /**
	 * Description of MISC namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openMiscDesc(MouseEvent event) {
    	String content = "The MISC namelist group of input parameters that do not fall into any one category. They "
    			+ "are typically global in extent, like the ambient temperature. \n\n"
    			+ "GVEC: By default, gravity points in the negative z direction, or more simply, downward. However, to change the "
    			+ "direction of gravity to model a sloping roof or tunnel, for example, specify the gravity vector with a triplet of numbers in units of m/s2.\n\n"
    			+ "Tmpa: Ambient temperature, the temperature of everything at the start of the simulation. The default is "
    			+ "20 degree Celsius.\n\n"
    			+ "Noise: FDS initializes the flow field with a very small amount of “noise” to prevent the development of a perfectly "
    			+ "symmetric flow when the boundary and initial conditions are perfectly symmetric. To turn this off, set Noise to FALSE.\n\n"
    			+ "Humidity: Humidity is the relative humidity of water vapor in units of %. It is 40 % by default.\n\n"
    			+ "Y_CO2_infty: Ambient mass fraction of carbon dioxide. Default for dry air is 0.000595.\n\n"
    			+ "Freeze_velocity: For certain types of diagnostic tests, it is useful to turn off the velocity field and exercise some other aspect "
    			+ "of the model, like radiation or particle transport. To do this, set Freeze_velocity to TRUE";
    	String namelist = "MISC";
		Values.openDesc(namelist, content);
    }

    /**
	 * Description of RADI namelist
	 * @param event Open the description label
	 */
    @FXML
    public void openRadiDesc(MouseEvent event) {
    	String content = "The RADI namelist contains parameters related to the radiation solver. \n\n"
    			+ "Radiation: It is possible to turn off the Radiation Transport Equation solver (saving roughly 20 % in CPU time) by adding the statement "
    			+ "RADIATION=.FALSE.";
    	String namelist = "RADI";
		Values.openDesc(namelist, content);
    }
    
    @FXML
    public void noiseSelect(ActionEvent event) {
    	noiseSelection = noiseCombo.getSelectionModel().getSelectedItem().toString();
    	noiseCombo.setValue(noiseSelection);
    }
    
    @FXML
    public void freezeSelect(ActionEvent event) {
    	freezeSelection = freezeCombo.getSelectionModel().getSelectedItem().toString();
    	freezeCombo.setValue(freezeSelection);
    }
    
    @FXML
    public void radiSelect(ActionEvent event) {
    	radiSelection = radiCombo.getSelectionModel().getSelectedItem().toString();
    	radiCombo.setValue(radiSelection);
    }
    
    /**
	 * Call the checking methods for the different namelists
	 */
    public void doChecking() {
    	doCheckingObst();
    	doCheckingMisc();
    }
    
    public void doCheckingObst() {
    	checkFloatPos = true;
    	checkXb = true;
    	if (!bulkText.getText().equals("")) {
    		checkFloatPos = checkFloatPos && checkFloatPosValues(bulkText);
    	}
    	if(!xbText.getText().equals("")) {
    		checkXb = checkXb && checkXbFormat(xbText);
    	}
    }
    
    public void doCheckingMisc() {
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
    
    /**
	 * Check if the float is a positive value 
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatPosValues(TextField tempField) { //check if float is positive
    	String param = "Bulk density and Y_CO2_Infty";
    	return Values.checkPosFloatValues(param, tempField);
    	
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
    	return Values.checkXbFormat(tempField);
    }
    
    /**
     * Check the Gvec format: <br>
     * - No white spaces <br>
     * - 3 values <br>
     * - Integers 
     * @param tempField TextField for user input
     * @return Boolean on whether the check was successful
     */
    public boolean checkGvecFormat(TextField tempField) {
    	return Values.checkGvecFormat(tempField);
    }
    
    /**
	 * Check if the value is a float
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkFloatValues(TextField tempField) { //check if the value is a float
    	String param = "Tmpa";
    	return Values.checkFloatValues(param, tempField);
    }
    
    /**
	 * Check if the value is a percentage
	 * @param tempField TextField for user input
	 * @return Boolean on whether the check was successful
	 */
    public boolean checkPercentValues(TextField tempField) { //check if float is a percentage
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
    
    /**
	 * Store the values into the database after input validation
	 * @throws SQLException
	 */
    public void storeValues() throws SQLException { //store values into the database
    	storeValuesObst();
    	storeValuesMisc();
    	storeValuesRadi();
    }
    
    public void storeValuesObst() throws SQLException { //store OBST values into the database
    	String mainObstIdString = Integer.toString(mainObstId);
    	String sqlObst = "INSERT INTO obst VALUES ('" + mainObstIdString + "', '" + bulkText.getText() + "', '" + colourText.getText() + "', '" + surfIdText.getText() +
    			"', '" + xbText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlObst);
    }
    
    public void storeValuesMisc() throws SQLException { //store MISC values into the database
    	String sqlMisc = "INSERT INTO misc VALUES ('" + noiseSelection + "', '" + freezeSelection + "', '" + humidityText.getText() + "', '" + co2Text.getText() +
    			"', '" + tmpaText.getText() + "', '" + gvecText.getText() + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlMisc);
    }
    
    public void storeValuesRadi() throws SQLException { //store RADI values into the database
    	String sqlRadi = "INSERT INTO radi VALUES ('" + radiSelection + "');";
    	ConnectionClass connectionClass = new ConnectionClass();
		Connection connection = connectionClass.getConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate(sqlRadi);
    }
    
    /**
	 * Display the saved input values when the page is loaded
	 * @throws SQLException
	 */
    public void showInfo() throws SQLException { //to show the info when the page is loaded
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
    
    public void showInfoMisc() throws SQLException { //to show the info when the page is loaded
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
    
    public void showInfoRadi() throws SQLException { //to show the info when the page is loaded
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
